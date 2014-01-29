package br.net.hexafun.PingTab;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;

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
	 * @param playerName
	 * @param ping
	 */
	public void pingPlayer(String playerName, int ping) {
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
	 * @param playerName
	 * @return
	 */
	public int getLastPing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getAveragePing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getMedianPing(String playerName) {
		int ping = -2;

		PlayerPings pp;
		ListIterator<PlayerPings> it = players.listIterator();
		for (; it.hasNext();) {
			pp = it.next();
			if (playerName.equals(pp.getPlayerName())) {
				ping = pp.getMedian();
			}
		}
		return ping;
	}

	/**
	 * @param playerName
	 * @return
	 */
	public int getModePing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getMidrangerPing(String playerName) {
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

	public int getMixedAveragePing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getMixedMedianPing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getMixedModePing(String playerName) {
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
	 * @param playerName
	 * @return
	 */
	public int getMixedMidrangePing(String playerName) {
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

	/**
	 * @param playerName
	 * @param pingMode
	 * @return
	 */
	public int getPing(String playerName, int pingMode) {
		int ping;
		switch (pingMode) {
		case 0:
			ping = this.getLastPing(playerName);
			break;
		case 1:
			ping = this.getAveragePing(playerName);
			break;
		case 2:
			ping = this.getMedianPing(playerName);
			break;
		case 3:
			ping = this.getModePing(playerName);
			break;
		case 4:
			ping = this.getMidrangerPing(playerName);
			break;
		case 5:
			ping = this.getMixedAveragePing(playerName);
			break;
		case 6:
			ping = this.getMixedMedianPing(playerName);
			break;
		case 7:
			ping = this.getMixedModePing(playerName);
			break;
		case 8:
			ping = this.getMixedMidrangePing(playerName);
			break;
		default:
			ping = this.getMedianPing(playerName);
			break;
		}
		return ping;
	}

	/**
	 * @param playerName
	 * @return
	 */
	public boolean playerExists(String playerName) {
		PlayerPings pp;

		for (ListIterator<PlayerPings> it = players.listIterator(); it
				.hasNext();) {
			pp = it.next();
			if (playerName.equals(pp.getPlayerName())) {
				return true;
			}
		}
		return false;
	}
}