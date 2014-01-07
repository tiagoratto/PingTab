package br.net.hexafun.PingTab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;

final class PlayersPings {
	private ArrayList<PlayerPings> players;
	private int samplingAmount;
	private Logger logger;
	
	public PlayersPings(int samples, Logger logger) {
		players = new ArrayList<PlayerPings>();
		samplingAmount = samples;
		this.logger = logger;
	}
	
	public void addPlayer(String playerName) {
		players.add(new PlayerPings(playerName,samplingAmount,this.logger));
	}
	
	public boolean removePlayer(String playerName) {
		PlayerPings pp;
		
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				players.remove(pp);
				return true;
			}
		}		
		return false;
	}

	public int getSamplingAmount() {
		return samplingAmount;
	}

	public void setSamplingAmount(int samplingAmount) {
		this.samplingAmount = samplingAmount;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			pp.setSamplingAmount(samplingAmount);
		}
	}
	
	public void pingPlayer(CraftPlayer player) {
		int ping = player.getHandle().ping;
		String playerName = player.getName();
		
		PlayerPings pp;
		
		logger.info("Iterator:" + players.iterator().toString());
		
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				pp.addPing(ping);
				break;
			}
		}
	}
	
	public int getLastPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getLastPing();
			}
		}
		return ping;
	}

	public int getAveragePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getAverage();
			}
		}
		return ping;
	}

	public int getMedianPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMedian();
			}
		}
		return ping;
	}

	public int getModePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMode();
			}
		}
		return ping;
	}

	public int getMidrangerPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMidrange();
			}
		}
		return ping;
	}

	public int getMixedAveragePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedAverage();
			}
		}
		return ping;
	}

	public int getMixedMedianPing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMedian();
			}
		}
		return ping;
	}

	public int getMixedModePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMode();
			}
		}
		return ping;
	}

	public int getMixedMidrangePing(CraftPlayer player) {
		String playerName = player.getName();
		int ping = -1;
		
		PlayerPings pp;
		for (Iterator<PlayerPings> it = players.iterator(); it.hasNext();) {
			pp = it.next();
			if (pp.getPlayerName() == playerName) {
				ping = pp.getMixedMidrange();
			}
		}
		return ping;
	}	
}