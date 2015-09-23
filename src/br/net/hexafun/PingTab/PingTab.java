package br.net.hexafun.PingTab;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.mcstats.MetricsLite;


import br.net.hexafun.Updater.Updater;
import br.net.hexafun.Updater.Updater.UpdateResult;
import br.net.hexafun.Updater.Updater.UpdateType;

public final class PingTab extends JavaPlugin implements Listener {

	private boolean autoUpdate;
	private boolean autoDownloadUpdate;
	public BukkitTask PingTask;
	public BukkitTask AlertTask;
	public String PingString;
	private int goodPing;
	private int mediumPing;
	protected boolean disableTab;
	protected int alertThreshold;
	protected String alertMessage;
	private boolean showPluginName;
	private boolean coloredPing;
	private String ownPingMessage;
	private String pingMessage;
	private String version;
	private Class<?> craftClass;

	public PingTab() {
		goodPing = 200;
		mediumPing = 500;
		disableTab = true;
		alertThreshold = 500;
		version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".")+1, Bukkit.getServer().getClass().getPackage().getName().length());
		try {
			craftClass = Class.forName("org.bukkit.craftbukkit."+this.version+".entity.CraftPlayer");
		} catch (ClassNotFoundException e) {
			getLogger().info("Problems instatiating CraftPlayer");
			e.printStackTrace();
		}
	}
	
	private int pingPlayer(Player p) {
		Object craftPlayer = craftClass.cast(p);
        Method getHandle;
        Object ePlayer;
        Field ping;
		try {
			getHandle = craftPlayer.getClass().getMethod("getHandle", new Class[0]);
			ePlayer = getHandle.invoke(craftPlayer, new Object[0]);
			ping = ePlayer.getClass().getDeclaredField("ping");
			return ping.getInt(ePlayer);
		} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException e) {
			getLogger().info("Problems using CraftPlayer");
			e.printStackTrace();
		}
		return -1;        
	}

	private String formatPingColor(int ping) {
		String ret;
		if (coloredPing) {
			if (ping <= goodPing)
				ret = (new StringBuilder()).append(ChatColor.GREEN)
						.append(ping).append(ChatColor.RESET).toString();
			else if (ping <= mediumPing)
				ret = (new StringBuilder()).append(ChatColor.GOLD).append(ping)
						.append(ChatColor.RESET).toString();
			else
				ret = (new StringBuilder()).append(ChatColor.RED).append(ping)
						.append(ChatColor.RESET).toString();
		} else {
			ret = (new StringBuilder()).append(ping).toString();
		}
		return ret;
	}

	private String insertPluginName(String msg) {
		if (showPluginName)
			msg = (new StringBuilder()).append(ChatColor.DARK_GREEN)
					.append("[PingTab] ").append(ChatColor.RESET).append(msg)
					.toString();
		return msg;
	}

	private String formatMessage(String msg) {
		msg = insertPluginName(msg);
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	private String formatMessage(String msg, boolean showPluginName) {
		if (showPluginName)
			msg = insertPluginName(msg);
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

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
		if (showPluginName)
			msg = insertPluginName(msg);
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
		msg = msg.replaceAll("%threshold",
				(new StringBuilder()).append(threshold).toString());
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}

	@Override
	public void onEnable() {
		try {
			MetricsLite metrics = new MetricsLite(this);
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
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(configFile);
		
		config.addDefault("Interval",3);
		config.addDefault("AutoUpdate", true);
		config.addDefault("AutoDownloadUpdate", false);
		config.addDefault("DisableTab",false);
		config.addDefault("ShowPluginNameOnMessages",false);
		config.addDefault("ColoredPingParameter",true);
		config.addDefault("GoodPing",200);
		config.addDefault("MediumPing",500);
		config.addDefault("OwnPingMessage","Your ping is %ping ms");
		config.addDefault("PingMessage","%playername's ping is %ping ms");
		config.addDefault("AlertPlayers",true);
		config.addDefault("AlertThreshold",1000);
		config.addDefault("AlertInterval",5);
		config.addDefault("AlertMessage","%playername, your latency of %ping is above %threshold!");
		
		int timer;
		if (config.isSet("Interval")) {
			if (config.isInt("Interval")) {
				timer = config.getInt("Interval");
			} else {
				getLogger().info("Interval setting has wrong value type, using default");
				timer = config.getDefaults().getInt("Interval");
			}				
		} else {
			getLogger().info("Setting \"Interval\" not present, using default and saving to the current file."+config.getDefaults().getInt("Interval"));
			timer = config.getDefaults().getInt("Interval");
			saveConfig();
		}
		
		if (config.isInt("Interval"))
			timer = config.getInt("Interval");
		if (config.isBoolean("DisableTab"))
			disableTab = config.getBoolean("DisableTab");
		else
			disableTab = false;
		if (config.isBoolean("ShowPluginNameOnMessages"))
			showPluginName = config.getBoolean("ShowPluginNameOnMessages");
		else
			showPluginName = true;
		if (config.isBoolean("ColoredPingParameter"))
			coloredPing = config.getBoolean("ColoredPingParameter");
		else
			coloredPing = true;
		if (config.isInt("GoodPing"))
			goodPing = config.getInt("GoodPing");
		if (config.isInt("MediumPing"))
			mediumPing = config.getInt("MediumPing");
		if (config.isString("OwnPingMessage"))
			ownPingMessage = config.getString("OwnPingMessage");
		else
			ownPingMessage = "Your ping is %ping ms";
		if (config.isString("PingMessage"))
			pingMessage = config.getString("PingMessage");
		else
			pingMessage = "%playername's ping is %ping ms";
		boolean alertPlayers = true;
		if (config.isInt("AlertPlayers"))
			alertPlayers = config.getBoolean("AlertPlayers");
		int alertTimer = 5;
		if (config.isInt("AlertInterval"))
			alertTimer = config.getInt("AlertInterval");
		if (config.isInt("AlertThreshold"))
			alertThreshold = config.getInt("AlertThreshold");
		else
			alertThreshold = 500;
		if (config.isString("AlertMessage"))
			alertMessage = config.getString("AlertMessage");
		else
			alertMessage = "%playername, your latency of %ping is above %threshold!";
		if (config.isBoolean("AutoUpdate"))
			autoUpdate = config.getBoolean("AutoUpdate");
		else
			autoUpdate = true;
		if (config.isBoolean("AutoDownloadUpdate"))
			autoDownloadUpdate = config.getBoolean("AutoDownloadUpdate");
		else
			autoDownloadUpdate = true;

		if (autoUpdate) {
			getLogger().info("Checking for new versions...");
			UpdateType autoDownload;
			if (autoDownloadUpdate) {
				autoDownload = Updater.UpdateType.DEFAULT;
			} else {
				autoDownload = Updater.UpdateType.NO_DOWNLOAD;
			}
			Updater updater = new Updater(this, 71589, this.getFile(),
					autoDownload, false);

			String result = updateResult(updater.getResult(),updater.getLatestGameVersion());
			getLogger().info(result);
		}

		if (alertPlayers)
			AlertTask = Bukkit.getScheduler().runTaskTimer(this,
					new Runnable() {

						@Override
						public void run() {
							if (Bukkit.getOnlinePlayers().size() == 0)
								return;
							Collection<? extends Player> players;
							players = Bukkit.getOnlinePlayers();
							Iterator<? extends Player> it = players.iterator();
							while (it.hasNext()) {
								Player player = it.next();
								int ping = pingPlayer(player);
								if (ping > alertThreshold)
									player.sendMessage(formatMessage(
											alertMessage, ping, player,
											alertThreshold));
							}
						}
					}, 20 * alertTimer * 60, 20 * alertTimer * 60);
		if (!disableTab)
			PingTask = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

				@Override
				public void run() {
					if (Bukkit.getOnlinePlayers().size() == 0) {
						return;
					}
					
					Collection<? extends Player> players = Bukkit.getOnlinePlayers();
					Iterator<? extends Player> itPlayers = players.iterator();
					while (itPlayers.hasNext()) {
						Player player = itPlayers.next();
						
						if (player.getScoreboard().getObjective("PingTab") == null) {
							player.getScoreboard().registerNewObjective("PingTab", "dummy");
							player.getScoreboard().getObjective("PingTab")
									.setDisplaySlot(DisplaySlot.PLAYER_LIST);
							player.getScoreboard().getObjective("PingTab").setDisplayName("ms");
						}
						
						
						Collection<? extends Player> tmpPlayers = Bukkit.getOnlinePlayers();
						Iterator<? extends Player> itTmpPlayers = tmpPlayers.iterator();
						while (itTmpPlayers.hasNext()) {
							Player tmpPlayer = itTmpPlayers.next();
							int tmpPing = pingPlayer(player);
							if (!tmpPlayer.getPlayerListName().equals(tmpPlayer.getName())) {
								/*
								player.getScoreboard()
										.getObjective("PingTab")
										.getScore(
												Bukkit.getOfflinePlayer(tmpPlayer
														.getPlayerListName())).setScore(tmpPing);
								*/
								player.getScoreboard()
									.getObjective("PingTab")
									.getScore(tmpPlayer.getPlayerListName()).setScore(tmpPing);
							} else {
								player.getScoreboard().getObjective("PingTab").getScore(tmpPlayer.getName())
										.setScore(tmpPing);
							}

						}
					}
				}
			}, 20 * timer, 20 * timer);
	}

	private String updateResult(UpdateResult result, String lastestVersion) {
		String msg = "";
		switch (result) {
		case SUCCESS:
			// Success: The updater found an update, and has readied it to
			// be loaded the next time the server restarts/reloads
			msg = "Plugin has been updated, restart or reload the server to enable the new version.";
			break;
		case NO_UPDATE:
			// No Update: The updater did not find an update, and nothing
			// was downloaded.
			msg = "Plugin is up to date.";
			break;
		case DISABLED:
			msg = "Updater is disabled in its configuration file.";
			// Won't Update: The updater was disabled in its configuration
			// file.
			break;
		case FAIL_DOWNLOAD:
			msg = "Download failed!";
			// Download Failed: The updater found an update, but was unable
			// to download it.
			break;
		case FAIL_DBO:
			msg = "DBO Failed!";
			// dev.bukkit.org Failed: For some reason, the updater was
			// unable to contact DBO to download the file.
			break;
		case FAIL_NOVERSION:
			msg = "No file found with correct name format.";
			// No version found: When running the version check, the file on
			// DBO did not contain the a version in the format 'vVersion'
			// such as 'v1.0'.
			break;
		case FAIL_BADID:
			msg = "Cant find the project Id on DBO.";
			// Bad id: The id provided by the plugin running the updater was
			// invalid and doesn't exist on DBO.
			break;
		case FAIL_APIKEY:
			msg = "Invalid API key.";
			// Bad API key: The user provided an invalid API key for the
			// updater to use.
			break;
		case UPDATE_AVAILABLE:
			msg = "There's a new version available (" + lastestVersion
					+ "), please update!";
			// There was an update found, but because you had the UpdateType
			// set to NO_DOWNLOAD, it was not downloaded.
			break;
		default:
			msg = "Invalid UpdateResult, please report this error to plugin developers.";
			break;
		}
		return msg;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent playerJoin) {
		Player player = playerJoin.getPlayer();

		if (player.getScoreboard().getObjective("PingTab") == null) {
			player.getScoreboard().registerNewObjective("PingTab", "dummy");
			player.getScoreboard().getObjective("PingTab")
					.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			player.getScoreboard().getObjective("PingTab").setDisplayName("ms");
		}

		if (Bukkit.getOnlinePlayers().size() == 0) {
			return;
		}
		
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		Iterator<? extends Player> itPlayers = players.iterator();
		while (itPlayers.hasNext()) {
			Player tmpPlayer = itPlayers.next();
			int tmpPing = pingPlayer(player);
			if (!tmpPlayer.getPlayerListName().equals(tmpPlayer.getName())) {
				/*
				player.getScoreboard()
					.getObjective("PingTab")
					.getScore(
							Bukkit.getOfflinePlayer(tmpPlayer
										.getPlayerListName())).setScore(tmpPing);
				*/
				player.getScoreboard()
					.getObjective("PingTab")
					.getScore(tmpPlayer.getPlayerListName()).setScore(tmpPing);
			} else {
				player.getScoreboard().getObjective("PingTab").getScore(tmpPlayer.getName())
						.setScore(tmpPing);
			}

		}
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTask(PingTask.getTaskId());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String args[]) {
		boolean canPing = false;
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (args.length == 0)
				if (sender instanceof Player) {
					Player player = (Player) sender;
					int ping = pingPlayer(player);
					sender.sendMessage(formatMessage(ownPingMessage, ping));
					return true;
				} else {
					sender.sendMessage(formatMessage("Sorry, but you cannot ping yourself from console!"));
					return true;
				}
			if (args[0].equalsIgnoreCase("list")) {
				if (Bukkit.getOnlinePlayers().size() == 0)
					return true;
				Collection<? extends Player> players = Bukkit.getOnlinePlayers();
				Iterator<? extends Player> itPlayers = players.iterator();
				sender.sendMessage(formatMessage(
						"&3[&2PingTab&3]&3 Players List&r", false));
				if (sender instanceof Player) {
					while (itPlayers.hasNext()) {
						Player player = itPlayers.next();
						int ping = pingPlayer(player);
						if (((Player) sender).canSee(player) && player != null)
							sender.sendMessage(formatMessage(
									"%playername - %ping&r", ping, player,
									false));
					}

				} else {
					while (itPlayers.hasNext()) {
						Player player = itPlayers.next();
						int ping = pingPlayer(player);
						sender.sendMessage(formatMessage(
								"%playername - %ping&r", ping, player, false));
					}

				}
			} else {
				Player player = Bukkit.getPlayer(args[0]);
				if (sender instanceof Player) {
					if ((player != null) && ((Player) sender).canSee(player))
						canPing = true;
				} else if (player != null)
					canPing = true;
				if (canPing) {
					int ping = pingPlayer(player);
					sender.sendMessage(formatMessage(pingMessage, ping, player));
				} else {
					sender.sendMessage(formatMessage((new StringBuilder(
							"The player ")).append(args[0])
							.append(" was not found!").toString()));
				}
			}
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("pingtab")) {
			if (args.length != 0 && args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage(formatMessage("Reloading PingTab configuration file."));
				File configFile = new File(getDataFolder(), "config.yml");
				if (!configFile.exists()) {
					saveDefaultConfig();
				}
				FileConfiguration config = YamlConfiguration
						.loadConfiguration(configFile);
				if (config.isBoolean("DisableTab")) {
					disableTab = config.getBoolean("DisableTab");
				} else {
					disableTab = false;
				}
				if (config.isBoolean("ShowPluginNameOnMessages")) {
					showPluginName = config
							.getBoolean("ShowPluginNameOnMessages");
				} else {
					showPluginName = true;
				}
				if (config.isBoolean("ColoredPingParameter")) {
					coloredPing = config.getBoolean("ColoredPingParameter");
				} else {
					coloredPing = true;
				}
				if (config.isInt("GoodPing")) {
					goodPing = config.getInt("GoodPing");
				}
				if (config.isInt("MediumPing")) {
					mediumPing = config.getInt("MediumPing");
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
				sender.sendMessage(formatMessage("PingTab configuration file reloaded."));
			} else if (args.length != 0
					&& args[0].equalsIgnoreCase("checkupdate")) {
				Updater updater = new Updater(this, 71589, this.getFile(),
						Updater.UpdateType.NO_DOWNLOAD, false);
				String result = updateResult(updater.getResult(),
						updater.getLatestGameVersion());
				getLogger().info(result);
				sender.sendMessage(result);
			} else if (args.length != 0 && args[0].equalsIgnoreCase("update")) {
				Updater updater = new Updater(this, 71589, this.getFile(),
						Updater.UpdateType.DEFAULT, false);
				String result = updateResult(updater.getResult(),
						updater.getLatestGameVersion());
				getLogger().info(result);
				sender.sendMessage(result);
			} else {
				if (args.length == 0) {
					sender.sendMessage(formatMessage(new StringBuilder(
							"&2PingTab&r &6v"
									+ this.getDescription().getVersion()
									+ "&r.").toString(), showPluginName));
				} else {
					sender.sendMessage(formatMessage((new StringBuilder(
							"Unknown option "))
							.append(args[0])
							.append(". Valid options are &areload&r, &acheckupdate&r, &aupdate&r")
							.toString()));
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
