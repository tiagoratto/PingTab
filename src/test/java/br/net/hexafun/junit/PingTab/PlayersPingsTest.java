package br.net.hexafun.junit.PingTab;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import br.net.hexafun.PingTab.PlayersPings;

//@RunWith(MockitoJUnitRunner.class)
public class PlayersPingsTest {
	
	@SuppressWarnings("unused")
	private Logger logger() {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		Logger log = Logger.getLogger("PingTabUnitTest");
		log.setUseParentHandlers(false);
		log.addHandler(handler);
		return log;
	}
	
	@Test
	public void testPlayersPings() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		assertTrue(pp instanceof PlayersPings);
		assertSame("Samples", 20, pp.getSamplingAmount());
	}

	@Test
	public void testAddPlayer() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		assertSame("Playname",true,pp.playerExists("Player"));
	}

	@Test
	public void testRemovePlayer() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.removePlayer("Player");
		assertSame("Playname",false,pp.playerExists("Player"));
	}

	@Test
	public void testGetSamplingAmount() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		assertSame("Samples", 20, pp.getSamplingAmount());
	}

	@Test
	public void testSetSamplingAmount() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.setSamplingAmount(30);
		assertSame("Samples", 30, pp.getSamplingAmount());
	}

	@Test
	public void testPingPlayer() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetLastPing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetAveragePing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMedianPing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetModePing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMidrangerPing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMixedAveragePing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMixedMedianPing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMixedModePing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetMixedMidrangePing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getLastPing("Player"));
	}

	@Test
	public void testGetPing() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.pingPlayer("Player", 20);
		assertSame("PingPlayer", 20, pp.getPing("Player",0));
	}

	@Test
	public void testPlayerExists() {
		PlayersPings pp;
		pp = new PlayersPings(20, Logger.getLogger("PingTabUnitTest"));
		pp.addPlayer("Player");
		pp.removePlayer("Player");
		assertSame("Playname",false,pp.playerExists("Player"));
	}

}
