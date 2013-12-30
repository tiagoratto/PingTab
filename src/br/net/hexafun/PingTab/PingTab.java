package br.net.hexafun.PingTab;


import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.mcstats.MetricsLite;

public final class PingTab extends JavaPlugin implements Listener {

	public BukkitTask PingTask;
	public String PingString;
	public Scoreboard PingScoreboard;

	public PingTab() {
		super();
	}

	public void onEnable() {
		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException ioexception) {
			getLogger()
					.warning(
							(new StringBuilder("MetricsLite failed!"))
									.toString());
		}

		getServer().getPluginManager().registerEvents(this, this);
		File configFile = new File(getDataFolder(), "config.yml");

		if (!configFile.exists()) {
			saveDefaultConfig();
		}

		// Configuration File Parsing
		FileConfiguration config = YamlConfiguration
				.loadConfiguration(configFile);
		int timer = config.getInt("Interval");

		// Create the Scoreboard and assign an dummy objective to it
		PingScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		PingScoreboard.registerNewObjective("PingTab", "dummy");

		// Assign the new scoreboard to the player list "section"
		if (PingScoreboard.getObjective(DisplaySlot.PLAYER_LIST) == null) {
			PingScoreboard.getObjective("PingTab").setDisplaySlot(
					DisplaySlot.PLAYER_LIST);
			PingScoreboard.getObjective("PingTab").setDisplayName("ms");
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
						getLogger()
								.warning(
										(new StringBuilder(
												"Objective IS NULL"))
												.toString());
					}
				}

				// Assign the populated Scoreboard to all players
				for (int k = 0; k < j; k++) {
					Player player = players[k];
					player.setScoreboard(PingScoreboard);
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
}
