package br.net.hexafun.PingTab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;

final class PlayerPings {
	private String playerName;
	private ArrayList<Integer> pings;
	private int average;
	private int median;
	private int mode;
	private int midrange;
	private int mixedAverage;
	private int mixedMedian;
	private int mixedMidrange;
	private int mixedMode;
	private int lastPing;
	private int smallestPing;
	private int biggestPing;
	private int samplingAmount;
	private int logger;
	
	// public PlayerPings(CraftPlayer player, int samples) {
	public PlayerPings(String playerName, int samples, Logger logger) {
		this.playerName = playerName;
		this.pings = new ArrayList<Integer>();
		this.average = 0;
		this.median = 0;
		this.midrange = 0;
		this.mode = 0;
		this.mixedAverage = 0;
		this.mixedMedian = 0;
		this.mixedMidrange = 0;
		this.mixedMode = 0;
		this.lastPing = 0;
		this.smallestPing = 100000;
		this.biggestPing = -1;
		this.samplingAmount = samples;
		this.logger = logger;
	}

	public void addPing(int ping) {

		if (this.smallestPing > ping) {
			this.smallestPing = ping;
		}

		if (this.biggestPing < ping) {
			this.biggestPing = ping;
		}

		if (this.pings.size() < this.samplingAmount) {
			this.pings.add(ping);
		} else {
			this.pings.remove(1);
			this.pings.add(ping);
		}

		this.average = this.calculateAverage(this.pings);
		this.median = this.calculateMedian(this.pings);
		this.midrange = this.calculateMidrange(this.pings);
		this.mode = this.calculateMode(this.pings);
		
		ArrayList<Integer> mixed = new ArrayList<Integer>();
		
		mixed.add(this.average);
		mixed.add(this.median);
		mixed.add(this.midrange);
		mixed.add(this.mode);
		
		this.mixedAverage = this.calculateAverage(mixed);
		this.mixedMedian = this.calculateMedian(mixed);
		this.mixedMidrange = this.calculateMidrange(mixed);
		this.mixedMode = this.calculateMode(mixed);
	}

	private int calculateAverage(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			int sum = 0;
			for (Iterator<Integer> it = pings.iterator(); it.hasNext();) {
				sum += it.next();
			}
			return sum / pings.size();
		} else {
			return 0;
		}
	}

	private int calculateMedian(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			ArrayList<Integer> tempList = pings;
			Collections.sort(tempList);
			int middle = tempList.size() / 2;
			if (tempList.size() % 2 == 1) {
				return tempList.get(middle);
			} else {
				return (int) Math.ceil(tempList.get(middle - 1)
						+ tempList.get(middle) / 2);
			}
		} else {
			return 0;
		}
	}

	private int calculateMode(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			ArrayList<Integer> tempList = pings;
			Collections.sort(tempList);
			int maxValue = -1, maxCount = 0;
			int ping1, ping2;
			for (Iterator<Integer> it = tempList.iterator(); it.hasNext();) {
				int count = 0;
				ping1 = it.next();
				for (Iterator<Integer> itt = tempList.iterator(); it.hasNext();) {
					ping2 = itt.next();
					if (ping1 == ping2) {
						count++;
					}
				}
				if (count > maxCount) {
					maxCount = count;
					maxValue = ping1;
				} else if ((count == maxCount) && (ping1 > maxValue)) {
					maxValue = ping1;
				}
				if (maxCount == 1) {
					// If there's isn't a repeated measurement value then uses
					// median as result, that seems fair to me
					maxValue = this.calculateMedian(this.pings);
				}
			}
			return maxValue;
		} else {
			return 0;
		}
	}

	private int calculateMidrange(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			if (pings.size() > 1) {
				ArrayList<Integer> tempList = pings;
				Collections.sort(tempList);
				int minValue = tempList.get(1);
				int maxValue = tempList.get(tempList.size());
				return (int) Math.ceil(minValue + maxValue / 2);
			} else {
				return pings.get(1);
			}
		} else {
			return 0;
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getAverage() {
		return average;
	}

	public int getMedian() {
		return median;
	}

	public int getMode() {
		return mode;
	}

	public int getMidrange() {
		return midrange;
	}
	
	public int getMixedAverage() {
		return mixedAverage;
	}

	public int getMixedMedian() {
		return mixedMedian;
	}

	public int getMixedMode() {
		return mixedMode;
	}

	public int getMixedMidrange() {
		return mixedMidrange;
	}

	public int getLastPing() {
		return lastPing;
	}

	public int getSamplingAmount() {
		return samplingAmount;
	}

	public void setSamplingAmount(int samplingAmount) {
		this.samplingAmount = samplingAmount;
	}

}
