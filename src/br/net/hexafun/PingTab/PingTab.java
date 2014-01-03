package br.net.hexafun.PingTab;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.mcstats.MetricsLite;

public final class PingTab extends JavaPlugin implements Listener {

	public BukkitTask PingTask;
	public BukkitTask AlertTask;
	public String PingString;
	public Scoreboard PingScoreboard;
	public Scoreboard NormalScoreboard;
	private int goodPing = 200; // Default GreenPing value
	private int mediumPing = 500; // Default OrangePing value
	protected boolean enabledByDefault = false;
	protected int alertThreshold = 500;
	protected String alertMessage;

	public PingTab() {
		super();
	}

	private String formatPingColor(int ping) {
		String ret;

		if (ping <= this.goodPing) {
			ret = (new StringBuilder(ChatColor.GREEN + "" + ChatColor.BOLD)
					.append(ping).append(ChatColor.RESET + "")).toString();
		} else if (ping <= this.mediumPing) {
			ret = (new StringBuilder(ChatColor.GOLD + "" + ChatColor.BOLD)
					.append(ping).append(ChatColor.RESET + "")).toString();
		} else {
			ret = (new StringBuilder(ChatColor.RED + "" + ChatColor.BOLD)
					.append(ping).append(ChatColor.RESET + "")).toString();
		}

		return ret;
	}

	protected String formatAlertMessage(String msg, int ping, Player player,
			int threshold) {
		msg = msg.replaceAll("%ping", new StringBuilder(""+ping).toString());
		msg = msg.replaceAll("%playername", player.getName());
		msg = msg.replaceAll("%threshold",new StringBuilder(""+threshold).toString());
		msg = ChatColor.translateAlternateColorCodes('&', msg);

		return msg;
	}

	public void onEnable() {
		try {
			MetricsLite metrics = new MetricsLite(this);
			;
			metrics.start();
			getLogger().info(
					(new StringBuilder("Plugin metrics enabled!")).toString());
		} catch (IOException ioexception) {
			getLogger().warning(
					(new StringBuilder("Plugin netrics failed!")).toString());
		}

		getServer().getPluginManager().registerEvents(this, this);
		File configFile = new File(getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			saveDefaultConfig();
		}

		// Configuration File Parsing
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(configFile);

		int timer = 3;
		if (config.isInt("Interval")) {
			timer = config.getInt("Interval");
		}

		if (config.isInt("GoodPing")) {
			this.goodPing = config.getInt("GoodPing");
		}

		if (config.isInt("MediumPing")) {
			this.mediumPing = config.getInt("MediumPing");
		}

		if (config.isBoolean("EnabledByDefault")) {
			enabledByDefault = config.getBoolean("EnabledByDefault");
		} else {
			enabledByDefault = false;
		}

		boolean alertPlayers = true;
		if (config.isInt("AlertPlayers")) {
			alertPlayers = config.getBoolean("AlertPlayers");
		}

		int alertTimer = 5;
		if (config.isInt("AlertInterval")) {
			alertTimer = config.getInt("AlertInterval");
		}

		if (config.isInt("AlertThreshold")) {
			alertThreshold = config.getInt("AlertThreshold");
		} else {
			alertThreshold = 500;
		}

		if (config.isString("AlertMessage")) {
			alertMessage = config.getString("AlertMessage");
		} else {
			alertMessage = (new StringBuilder(" Your latency of "
					+ ChatColor.DARK_RED + ChatColor.BOLD + "%p"
					+ ChatColor.RESET + ChatColor.DARK_RED + " is too high!"
					+ ChatColor.RESET).toString());
		}

		// Create the Scoreboard and assign an dummy objective to it
		PingScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		NormalScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		PingScoreboard.registerNewObjective("PingTab", "dummy");

		// Assign the new scoreboard to the player list "section"
		if (PingScoreboard.getObjective(DisplaySlot.PLAYER_LIST) == null) {
			PingScoreboard.getObjective("PingTab").setDisplaySlot(
					DisplaySlot.PLAYER_LIST);
			PingScoreboard.getObjective("PingTab").setDisplayName("ms");
		}

		// The plugin can be configured to alert the player when latency to high
		if (alertPlayers) {
			// Create a task so we can send the alerts
			AlertTask = Bukkit.getScheduler().runTaskTimer(this,
					new Runnable() {
						public void run() {
							// Nothing to do, no one online
							if (Bukkit.getOnlinePlayers().length == 0) {
								return;
							}

							// Measure player ping and send the messages
							Player players[];
							int j = (players = Bukkit.getOnlinePlayers()).length;
							for (int i = 0; i < j; i++) {
								Player player = players[i];
								int ping = ((CraftPlayer) player).getHandle().ping;
								String formatedAlertMessage = formatAlertMessage(
										alertMessage, ping, player,
										alertThreshold);

								if (ping > alertThreshold) {
									player.sendMessage((new StringBuilder(""
											+ ChatColor.DARK_RED
											+ ChatColor.BOLD)
											.append("[PingTab]"
													+ ChatColor.RESET
													+ ChatColor.DARK_RED)
											.append(" " + formatedAlertMessage))
											.toString());
								}
							}
						}
					}, 20 * alertTimer * 60, 20 * alertTimer * 60);
		}

		// Create a task so we can update ping values
		PingTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

			public void run() {

				// Nothing to do, no one online
				if (Bukkit.getOnlinePlayers().length == 0) {
					return;
				}

				// Measure player ping and populate the Scoreboard
				Player players[];
				int j = (players = Bukkit.getOnlinePlayers()).length;
				for (int i = 0; i < j; i++) {
					Player player = players[i];

					int ping = ((CraftPlayer) player).getHandle().ping;

					Objective PingListObjective = PingScoreboard
							.getObjective("PingTab");

					if (PingListObjective != null) {
						if (!player.getPlayerListName()
								.equals(player.getName())) {
							PingScoreboard
									.getObjective("PingTab")
									.getScore(
											Bukkit.getOfflinePlayer(player
													.getPlayerListName()))
									.setScore(ping);
						} else {
							PingScoreboard.getObjective("PingTab")
									.getScore(player).setScore(ping);
						}
					} else {
						getLogger().warning(
								(new StringBuilder("Objective IS NULL"))
										.toString());
					}
				}

				// Assign the populated Scoreboard to allowed players
				for (int k = 0; k < j; k++) {
					Player player = players[k];
					if (enabledByDefault) {
						player.setScoreboard(PingScoreboard);
					} else if (player.hasPermission("pingtab.showscoreboard")) {
						player.setScoreboard(PingScoreboard);
					} else {
						if (player.getScoreboard() != null) {
							player.setScoreboard(null);
						}
					}
				}
			}
		}, 20 * timer, 20 * timer);
	}

	public void onJoin(PlayerJoinEvent playerJoin) {
		Player player = playerJoin.getPlayer();
		int ping = ((CraftPlayer) player).getHandle().ping;
		PingScoreboard.getObjective("PingTab").getScore(player).setScore(ping);
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTask(PingTask.getTaskId());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					int ping = ((CraftPlayer) player).getHandle().ping;
					sender.sendMessage((new StringBuilder("" + ChatColor.WHITE
							+ ChatColor.BOLD + "[PingTab]" + ChatColor.RESET
							+ ChatColor.WHITE + " Your ping is ").append(this
							.formatPingColor(ping))).toString());
					return true;
				} else {
					sender.sendMessage("Sorry, but you cannot ping yourself from console!");
					return true;
				}
			} else {
				Player player = (Player) Bukkit.getPlayer(args[0]);
				if (player != null) {
					int ping = ((CraftPlayer) player).getHandle().ping;
					sender.sendMessage((new StringBuilder("")
							.append("" + ChatColor.WHITE + ChatColor.BOLD
									+ "[PingTab]" + ChatColor.RESET
									+ ChatColor.WHITE)
							.append(player.getName())
							.append(" " + ChatColor.RESET + ChatColor.WHITE
									+ "'s ping is ").append(this
							.formatPingColor(ping))).toString());
					return true;
				} else {
					sender.sendMessage((new StringBuilder("" + ChatColor.WHITE
							+ ChatColor.BOLD + "[PingTab]" + ChatColor.RESET
							+ ChatColor.WHITE + "The player ").append(args[0])
							.append(" was not found!")).toString());
					return true;
				}
			}
		}
		return false;
	}
}
