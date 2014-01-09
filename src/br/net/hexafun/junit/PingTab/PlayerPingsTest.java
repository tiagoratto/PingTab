package br.net.hexafun.junit.PingTab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import br.net.hexafun.PingTab.PlayerPings;

public class PlayerPingsTest {
	
	private Logger logger() {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		Logger log = Logger.getLogger("PingTabUnitTest");
		log.setUseParentHandlers(false);
		log.addHandler(handler);
		return log;
	}
	
	@Test
	public void testPlayerPings() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		assertTrue(pp instanceof PlayerPings);
		assertSame("Playername", "PingTabUnitTest", pp.getPlayerName());
		assertSame("Sampling must be", 20, pp.getSamplingAmount());
	}
	
	@Test
	public void testCurentSamples() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 5, Logger.getLogger("PingTabUnitTest"));
		pp.addPing(100);
		assertSame("First current pings stored", 1, pp.getCurrentSamples());
		pp.addPing(100);
		pp.addPing(100);
		pp.addPing(100);
		pp.addPing(100);
		assertSame("Second current pings stored", 5, pp.getCurrentSamples());
		pp.addPing(100);
		assertSame("Third current pings stored", 5, pp.getCurrentSamples());
	}

	@Test
	public void testAddPing() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		pp.addPing(100);
		assertSame("Last ping", 100, 100);
	}

	@Test
	public void testGetPlayerName() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		assertSame("Playername", "PingTabUnitTest", pp.getPlayerName());
	}

	@Test
	public void testSetPlayerName() {
		PlayerPings pp;
		pp = new PlayerPings("", 20, Logger.getLogger("PingTabUnitTest"));
		pp.setPlayerName("PingTabUnitTest");
		assertSame("Playername", "PingTabUnitTest", pp.getPlayerName());
	}

	@Test
	public void testGetAverage() {
		PlayerPings pp;
		pp = new PlayerPings("", 20, Logger.getLogger("PingTabUnitTest"));
		pp.addPing(10);
		pp.addPing(20);
		pp.addPing(30);
		assertTrue("Average calculation", pp.getAverage() == 20);
	}

	@Test
	public void testGetMedian() {
		PlayerPings pp;
		pp = new PlayerPings("", 20, this.logger());
		pp.addPing(10);
		assertEquals("Median calculation with one sample",10, pp.getMedian());
		pp.addPing(20);
		pp.addPing(30);
		pp.addPing(40);
		pp.addPing(50);
		assertEquals("Median calculation with odd samples", 30, pp.getMedian());
		pp.addPing(60);
		assertEquals("Median calculation with even samples", 35, pp.getMedian());
	}

	@Test
	public void testGetMode() {
		PlayerPings pp;
		pp = new PlayerPings("", 20, this.logger());
		pp.addPing(10);
		assertEquals("Mode calculation with one sample",10, pp.getMode());
		pp.addPing(20);
		assertEquals("Mode calculation with no repeat",15, pp.getMode());
		pp.addPing(20);
		assertEquals("Mode calculation first repeat", 20, pp.getMode());
		pp.addPing(30);
		pp.addPing(30);
		pp.addPing(30);
		assertEquals("Mode calculation second repeat", 30, pp.getMode());
	}

	@Test
	public void testGetMidrange() {
		PlayerPings pp;
		pp = new PlayerPings("", 20, this.logger());
		pp.addPing(10);
		assertEquals("Midrange calculation with one sample",10, pp.getMidrange());
		pp.addPing(20);
		assertEquals("Midrange calculation with two samples",15, pp.getMidrange());
		pp.addPing(12);
		assertEquals("Midrange calculation with three samples", 15, pp.getMidrange());
		pp.addPing(70);
		assertEquals("Midrange calculation with four samples", 40, pp.getMidrange());
	}

	@Test
	public void testGetLastPing() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		pp.addPing(100);
		assertSame("Last ping", 100, pp.getLastPing());
	}

	@Test
	public void testGetSamplingAmount() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		assertSame("SampingAmount", 20, pp.getSamplingAmount());
	}

	@Test
	public void testSetSamplingAmount() {
		PlayerPings pp;
		pp = new PlayerPings("PingTabUnitTest", 20, Logger.getLogger("PingTabUnitTest"));
		pp.setSamplingAmount(30);
		assertSame("SampingAmount", 30, pp.getSamplingAmount());
	}

}
