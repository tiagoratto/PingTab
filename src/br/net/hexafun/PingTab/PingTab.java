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
import org.bukkit.event.player.PlayerQuitEvent;
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
	private int goodPing;
	private int mediumPing;
	protected boolean disableTab;
	protected int alertThreshold;
	protected String alertMessage;
	private boolean showPluginName;
	private boolean coloredPing;
	private String ownPingMessage;
	private String pingMessage;
	private String goodPingColor;
	private String mediumPingColor;
	private String badPingColor;
	FileConfiguration config;
	private int pingTimer;
	private int alertTimer;
	private boolean alertPlayers;
	private int samplingAmount;
	private int tabMode;
	private int alertMode;

	PlayersPings playersPings;

	public PingTab() {
		super();
	}

	/*
	 * Message Related Functions
	 */

	private String formatPingColor(int ping) {
		String ret;

		if (this.coloredPing) {
			if (ping <= this.goodPing) {
				ret = goodPingColor + "" + ping + ChatColor.RESET;
			} else if (ping <= this.mediumPing) {
				ret = mediumPingColor + "" + ping + ChatColor.RESET;
			} else {
				ret = badPingColor + "" + ping + ChatColor.RESET;
			}
		} else {
			ret = "" + ping;
		}

		return ret;
	}

	private String insertPluginName(String msg) {
		if (this.showPluginName) {
			msg = ChatColor.DARK_GREEN + "[PingTab] " + ChatColor.RESET + msg;
		}
		return msg;
	}

	private String formatMessage(String msg) {
		msg = insertPluginName(msg);
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	private String formatMessage(String msg, boolean showPluginName) {
		if (showPluginName) {
			msg = insertPluginName(msg);
		}
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	/*
	 * private String formatMessage(String msg, Player player) { msg =
	 * insertPluginName(msg); msg = msg.replaceAll("%playername",
	 * player.getName()); msg = ChatColor.translateAlternateColorCodes('&',
	 * msg); return msg; }
	 */

	private String formatMessage(String msg, int ping) {
		msg = insertPluginName(msg);
		msg = msg.replaceAll("%ping", formatPingColor(ping));
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	private String formatMessage(String msg, int ping, Player player) {
		msg = insertPluginName(msg);
		msg = msg.replaceAll("%ping", formatPingColor(ping));
		msg = msg.replaceAll("%playername", player.getPlayerListName());
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	private String formatMessage(String msg, int ping, Player player,
			boolean showPluginName) {
		if (showPluginName) {
			msg = insertPluginName(msg);
		}
		msg = msg.replaceAll("%ping", formatPingColor(ping));
		msg = msg.replaceAll("%playername", player.getPlayerListName());
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	protected String formatMessage(String msg, int ping, Player player,
			int threshold) {
		msg = insertPluginName(msg);
		msg = msg.replaceAll("%ping", formatPingColor(ping));
		msg = msg.replaceAll("%playername", player.getPlayerListName());
		msg = msg.replaceAll("%threshold", ("" + threshold));
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	/*
	 * Config related functions
	 */

	private boolean loadStaticParamsConfig() {

		if (config.isInt("Interval")) {
			pingTimer = config.getInt("Interval");
		} else {
			pingTimer = 3;
		}

		if (config.isInt("AlertInterval")) {
			alertTimer = config.getInt("AlertInterval");
		} else {
			alertTimer = 5;
		}

		return true;
	}

	private boolean loadParamsConfig() {
		if (config.isBoolean("DisableTab")) {
			disableTab = config.getBoolean("DisableTab");
		} else {
			disableTab = false;
		}

		if (config.isBoolean("ShowPluginNameOnMessages")) {
			showPluginName = config.getBoolean("ShowPluginNameOnMessages");
		} else {
			showPluginName = true;
		}

		if (config.isInt("SamplingAmount")) {
			samplingAmount = config.getInt("SamplingAmount");
		} else {
			samplingAmount = 20;
		}

		playersPings = new PlayersPings(this.samplingAmount,this.getLogger());

		String tabModeConfig;
		if (config.isString("TabMode")) {
			tabModeConfig = config.getString("TabMode");
			if (tabModeConfig == "Instant") {
				tabMode = 0;
			} else if (tabModeConfig == "Average") {
				tabMode = 1;
			} else if (tabModeConfig == "Median") {
				tabMode = 2;
			} else if (tabModeConfig == "Mode") {
				tabMode = 3;
			} else if (tabModeConfig == "Midrange") {
				tabMode = 4;
			} else if (tabModeConfig == "MixedAverage") {
				tabMode = 5;
			} else if (tabModeConfig == "MixedMedian") {
				tabMode = 6;
			} else if (tabModeConfig == "MixedMode") {
				tabMode = 7;
			} else if (tabModeConfig == "MixedMidrange") {
				tabMode = 8;
			} else {
				tabMode = 2;
			}
		} else {
			tabMode = 1;
		}

		if (config.isBoolean("ColoredPingParameter")) {
			coloredPing = config.getBoolean("ColoredPingParameter");
		} else {
			coloredPing = true;
		}

		if (config.isInt("GoodPing")) {
			goodPing = config.getInt("GoodPing");
		} else {
			goodPing = 200;
		}

		if (config.isString("GoodPingColor")) {
			goodPingColor = config.getString("GoodPingColor");
		} else {
			goodPingColor = "&2";
		}

		if (config.isInt("MediumPing")) {
			mediumPing = config.getInt("MediumPing");
		} else {
			mediumPing = 500;
		}

		if (config.isInt("MediumPingColor")) {
			mediumPingColor = config.getString("MediumPingColor");
		} else {
			mediumPingColor = "&6";
		}

		if (config.isInt("BadPingColor")) {
			badPingColor = config.getString("BadPingColor");
		} else {
			mediumPingColor = "&c";
		}

		if (config.isString("OwnPingMessage")) {
			ownPingMessage = config.getString("OwnPingMessage");
		} else {
			ownPingMessage = "Your ping is %ping ms";
		}

		if (config.isString("PingMessage")) {
			pingMessage = config.getString("PingMessage");
		} else {
			pingMessage = "%playername's ping is %ping ms";
		}

		if (config.isInt("AlertPlayers")) {
			alertPlayers = config.getBoolean("AlertPlayers");
		}

		String alertModeConfig;
		if (config.isString("AlertMode")) {
			alertModeConfig = config.getString("AlertMode");
			if (alertModeConfig == "Instant") {
				alertMode = 0;
			} else if (alertModeConfig == "Average") {
				alertMode = 1;
			} else if (alertModeConfig == "Median") {
				alertMode = 2;
			} else {
				alertMode = 2;
			}
		} else {
			alertMode = 2;
		}

		if (config.isInt("AlertThreshold")) {
			alertThreshold = config.getInt("AlertThreshold");
		} else {
			alertThreshold = 500;
		}

		if (config.isString("AlertMessage")) {
			alertMessage = config.getString("AlertMessage");
		} else {
			alertMessage = "%playername, your latency of %ping is above %threshold!";
		}
		return true;
	}

	/*
	 * Plugin Section
	 */

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
		config = YamlConfiguration.loadConfiguration(configFile);

		if (!loadStaticParamsConfig()) {
			getLogger().warning(
					"Error loading timers config, using default settings");
		}

		if (!loadParamsConfig()) {
			getLogger().warning(
					"Error loading parameters config, using default settings");
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
								CraftPlayer player = (CraftPlayer) players[i];
								int ping = -2;
								switch (alertMode) {
								case 0:
									ping = playersPings.getLastPing(player);
									break;
								case 1:
									ping = playersPings.getAveragePing(player);
									break;
								case 2:
									ping = playersPings.getMedianPing(player);
									break;
								case 3:
									ping = playersPings.getModePing(player);
									break;
								case 4:
									ping = playersPings.getMidrangerPing(player);
									break;
								case 5:
									ping = playersPings.getMixedAveragePing(player);
									break;
								case 6:
									ping = playersPings.getMixedMedianPing(player);
									break;
								case 7:
									ping = playersPings.getMixedModePing(player);
									break;
								case 8:
									ping = playersPings.getMixedMidrangePing(player);
									break;
								default:
									ping = playersPings.getMedianPing(player);
									break;	
								}
								
								if (ping > alertThreshold) {
									player.sendMessage(formatMessage(
											alertMessage, ping, player,
											alertThreshold));
								}
							}
						}
					}, 20 * alertTimer * 60, 20 * alertTimer * 60);
		}

		if (!disableTab) {

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
						CraftPlayer player = (CraftPlayer) players[i];
						playersPings.pingPlayer(player);
						int ping = -3;
						
						switch (tabMode) {
						case 0:
							ping = playersPings.getLastPing(player);
							break;
						case 1:
							ping = playersPings.getAveragePing(player);
							break;
						case 2:
							ping = playersPings.getMedianPing(player);
							break;
						case 3:
							ping = playersPings.getModePing(player);
							break;
						case 4:
							ping = playersPings.getMidrangerPing(player);
							break;
						case 5:
							ping = playersPings.getMixedAveragePing(player);
							break;
						case 6:
							ping = playersPings.getMixedMedianPing(player);
							break;
						case 7:
							ping = playersPings.getMixedModePing(player);
							break;
						case 8:
							ping = playersPings.getMixedMidrangePing(player);
							break;
						default:
							ping = playersPings.getMedianPing(player);
							break;	
						}

						Objective PingListObjective = PingScoreboard
								.getObjective("PingTab");

						if (PingListObjective != null) {
							if (!player.getPlayerListName().equals(
									player.getName())) {
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
						if (player.hasPermission("pingtab.showscoreboard")) {
							player.setScoreboard(PingScoreboard);
						} else {
							if (player.getScoreboard() != null) {
								player.setScoreboard(null);
							}
						}
					}
				}
			}, 20 * pingTimer, 20 * pingTimer);
		}
	}

	public void onJoin(PlayerJoinEvent playerJoin) {
		CraftPlayer player = (CraftPlayer) playerJoin.getPlayer();
		playersPings.addPlayer(player.getName());
		playersPings.pingPlayer(player);
		int ping = playersPings.getLastPing(player);
		PingScoreboard.getObjective("PingTab").getScore(player).setScore(ping);
	}

	public void onQuit(PlayerQuitEvent playerQuit) {
		CraftPlayer player = (CraftPlayer) playerQuit.getPlayer();
		playersPings.removePlayer(player.getName());
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTask(PingTask.getTaskId());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		boolean canPing = false;

		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (args.length == 0) {
				// Ping yourself, but not from console
				if (sender instanceof Player) {
					sender.sendMessage(formatMessage(ownPingMessage,
							playersPings.getLastPing((CraftPlayer) sender)));
					return true;
				} else {
					sender.sendMessage(formatMessage("Sorry, but you cannot ping yourself from console!"));
					return true;
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				if (Bukkit.getOnlinePlayers().length == 0) {
					return true;
				}

				Player players[];
				int j = (players = Bukkit.getOnlinePlayers()).length;
				// String finalList = new String();

				sender.sendMessage(formatMessage(
						"&3============= &2PingTab List &3=============&r",
						false));

				if (sender instanceof Player) {
					// From game
					for (int i = 0; i < j; i++) {
						if ((((Player) sender).canSee(players[i]))
								&& (players[i] != null)) {
							sender.sendMessage(formatMessage(
									"%playername - %ping&r",
									playersPings
											.getLastPing((CraftPlayer) players[i]),
									players[i], false));
						}

					}
				} else {
					// From console
					for (int i = 0; i < j; i++) {
						sender.sendMessage(formatMessage(
								"%playername - %ping&r", playersPings
										.getLastPing((CraftPlayer) players[i]),
								players[i], false));
					}
				}
			} else {
				// Ping anyone else
				Player player = (Player) Bukkit.getPlayer(args[0]);
				if (sender instanceof Player) {
					// From game
					if ((((Player) sender).canSee(player)) && (player != null)) {
						canPing = true;
					}
				} else {
					// From console
					if (player != null) {
						canPing = true;
					}
				}

				if (canPing) {
					sender.sendMessage(formatMessage(pingMessage,
							playersPings.getLastPing((CraftPlayer) player),
							player));
				} else {
					sender.sendMessage(formatMessage("The player " + args[0]
							+ " was not found!"));
				}
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("pingtab")) {
			if ((args.length != 0) && (args[0].equalsIgnoreCase("reload"))) {

				sender.sendMessage(formatMessage("Reloading PingTab configuration file."));

				if (loadParamsConfig()) {
					sender.sendMessage(formatMessage("PingTab configuration file reloaded."));
					getLogger().info("Configuration reloaded.");
				} else {
					sender.sendMessage(formatMessage("PingTab configuration file reloaded error."));
					getLogger()
							.info("PingTab configuration reload error, using defaults.");
				}
			} else {
				sender.sendMessage(formatMessage("Unknown option " + args[0]
						+ "."));
			}
			return true;
		}
		return false;
	}
}
