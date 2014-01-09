package br.net.hexafun.PingTab;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;

public class PlayersPings {
	private ArrayList<PlayerPings> players;
	private int samplingAmount;
	private Logger logger;

	/**
	 * @param samples
	 *            int
	 * @param logger
	 *            Logger
	 */
	public PlayersPings(int samples, Logger logger) {
		players = new ArrayList<PlayerPings>();
		samplingAmount = samples;
		this.logger = logger;
	}

	/**
	 * @param playerName
	 *            String
	 */
	public void addPlayer(String playerName) {
		players.add(new PlayerPings(playerName, samplingAmount, this.logger));
	}

	/**
	 * @param playerName
	 *            String
	 * @return boolean
	 */
	public boolean removePlayer(String playerName) {
		PlayerPings pp;

		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				players.remove(pp);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return int
	 */
	public int getSamplingAmount() {
		return samplingAmount;
	}

	/**
	 * @param samplingAmount
	 *            int
	 */
	public void setSamplingAmount(int samplingAmount) {
		this.samplingAmount = samplingAmount;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			pp.setSamplingAmount(samplingAmount);
		}
	}

	/**
	 * @param player
	 *            CraftPlayer
	 */
	public void pingPlayer(CraftPlayer player) {
		int ping = player.getHandle().ping;
		String playerName = player.getName();

		PlayerPings pp;

		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				pp.addPing(ping);
				break;
			}
		}
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getLastPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = 0;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getLastPing();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getAveragePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;

		PlayerPings pp;
		ListIterator<PlayerPings> it = players.listIterator();
		for (; it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getAverage();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMedianPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -2;

		PlayerPings pp;
		ListIterator<PlayerPings> it = players.listIterator();
		for (; it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMedian();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getModePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -3;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMode();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMidrangerPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -4;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMidrange();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMixedAveragePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -5;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedAverage();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMixedMedianPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -6;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMedian();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMixedModePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -7;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMode();
			}
		}
		return ping;
	}

	/**
	 * @param player
	 *            CraftPlayer
	 * @return int
	 */
	public int getMixedMidrangePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -8;

		PlayerPings pp;
		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMidrange();
			}
		}
		return ping;
	}

	public int getPing(CraftPlayer player, int pingMode) {
		int ping;
		switch (pingMode) {
		case 0:
			ping = this.getLastPing((CraftPlayer) player);
			break;
		case 1:
			ping = this.getAveragePing((CraftPlayer) player);
			break;
		case 2:
			ping = this.getMedianPing((CraftPlayer) player);
			break;
		case 3:
			ping = this.getModePing((CraftPlayer) player);
			break;
		case 4:
			ping = this.getMidrangerPing((CraftPlayer) player);
			break;
		case 5:
			ping = this.getMixedAveragePing((CraftPlayer) player);
			break;
		case 6:
			ping = this.getMixedMedianPing((CraftPlayer) player);
			break;
		case 7:
			ping = this.getMixedModePing((CraftPlayer) player);
			break;
		case 8:
			ping = this.getMixedMidrangePing((CraftPlayer) player);
			break;
		default:
			ping = this.getMedianPing((CraftPlayer) player);
			break;
		}
		return ping;
	}

	/**
	 * @param player
	 * @return boolean
	 */
	public boolean playerExists(CraftPlayer player) {
		PlayerPings pp;

		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == player.getName()) {
				players.remove(pp);
				return true;
			}
		}
		return false;
	}
}