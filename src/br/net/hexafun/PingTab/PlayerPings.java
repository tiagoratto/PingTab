package br.net.hexafun.PingTab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.logging.Logger;

public class PlayerPings {
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
	@SuppressWarnings("unused")
	private Logger logger;
	
	/**
	 * @param playerName
	 * @param samples
	 * @param logger
	 */
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
	
	public int getCurrentSamples() {
		return this.pings.size();
	}

	/**
	 * @param ping
	 */
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

		this.lastPing = ping;
		this.average = this.calculateAverage(this.pings);
		this.median = this.calculateMedian(this.pings);
		this.midrange = this.calculateMidrange(this.pings);
		this.mode = this.calculateMode(this.pings);
		
//		ArrayList<Integer> mixed = new ArrayList<Integer>();
//		
//		mixed.add(this.average);
//		mixed.add(this.median);
//		mixed.add(this.midrange);
//		mixed.add(this.mode);
//		
//		this.mixedAverage = this.calculateAverage(mixed);
//		this.mixedMedian = this.calculateMedian(mixed);
//		this.mixedMidrange = this.calculateMidrange(mixed);
//		this.mixedMode = this.calculateMode(mixed);
	}

	/**
	 * @param pings
	 * @return int
	 */
	private int calculateAverage(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			int sum = 0;
			for (ListIterator<Integer> it = pings.listIterator(); it.hasNext();) {
				sum += it.next();
			}
			return sum / pings.size();
		} else {
			return 0;
		}
	}

	/**
	 * @param pings
	 * @return int
	 */
	private int calculateMedian(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			ArrayList<Integer> tempList = pings;
			Collections.sort(tempList);
			int middle = (int) (tempList.size() / 2);
			if (tempList.size() == 1) {
				return tempList.get(0);
			} else if (tempList.size() % 2 == 1) {
				return tempList.get(middle);
			} else {
				return (int) Math.round((tempList.get(middle - 1)
						+ tempList.get(middle)) / 2);
			}
		} else {
			return 0;
		}
	}

	/**
	 * @param pings
	 * @return int
	 */
	private int calculateMode(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			ArrayList<Integer> tempList1 = pings;
			Collections.sort(tempList1);
			
			int maxValue = -1, maxCount = 0;
			int ping;
			ListIterator<Integer> it = tempList1.listIterator();
			
			int count = 0;
			int previousRead = -10000;
			
			for (; it.hasNext();) {
				ping = it.next();
				if (ping == previousRead) {
					count++;
				} else {
					count = 1;
				}
				
				if (count > maxCount) {
					maxCount = count;
					maxValue = ping;
				} else if ((count == maxCount) && (ping > maxValue)) {
					maxValue = ping;
				}
				previousRead = ping;
			}
			if (maxCount == 1) {
				// If there's isn't a repeated measurement value then uses
				// median as result, that seems fair to me
				maxValue = this.calculateMedian(this.pings);
			}
			return maxValue;
		} else {
			return 0;
		}
	}

	/**
	 * @param pings
	 * @return int
	 */
	private int calculateMidrange(ArrayList<Integer> pings) {
		if (!pings.isEmpty()) {
			if (pings.size() > 1) {
				ArrayList<Integer> tempList = pings;
				Collections.sort(tempList);
				int minValue = tempList.get(0);
				int maxValue = tempList.get(tempList.size()-1);
				return (int) (minValue + maxValue) / 2;
			} else {
				return pings.get(0);
			}
		} else {
			return 0;
		}
	}

	/**
	 * @return String
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param playerName
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * @return int
	 */
	public int getAverage() {
		return average;
	}

	/**
	 * @return int
	 */
	public int getMedian() {
		return median;
	}

	/**
	 * @return int
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @return int
	 */
	public int getMidrange() {
		return midrange;
	}
	
	/**
	 * @return int
	 */
	public int getMixedAverage() {
		return mixedAverage;
	}

	/**
	 * @return int
	 */
	public int getMixedMedian() {
		return mixedMedian;
	}

	/**
	 * @return int
	 */
	public int getMixedMode() {
		return mixedMode;
	}

	/**
	 * @return int
	 */
	public int getMixedMidrange() {
		return mixedMidrange;
	}

	/**
	 * @return int
	 */
	public int getLastPing() {
		return lastPing;
	}

	/**
	 * @return int
	 */
	public int getSamplingAmount() {
		return samplingAmount;
	}

	/**
	 * @param samplingAmount
	 */
	public void setSamplingAmount(int samplingAmount) {
		this.samplingAmount = samplingAmount;
	}

}
