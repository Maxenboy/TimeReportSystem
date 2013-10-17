package testTimeReport;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.TimeReport;

public class TestTimeReport {
	private TimeReport timeReport1, timeReport2;

	@Before
	public void setUp() throws Exception {
		timeReport1 = new TimeReport(1, 2, 3);
		timeReport2 = new TimeReport(4, 5, true, 6, 7);
	}

	@After
	public void tearDown() throws Exception {
		timeReport1 = null;
		timeReport2 = null;
	}

	@Test
	public void testGetId() {
		assertEquals(0, timeReport1.getId());
		assertEquals(4, timeReport2.getId());
	}

	@Test
	public void testSetId() {
		timeReport1.setId(2);
		timeReport2.setId(5);
		assertEquals(2, timeReport1.getId());
		assertEquals(5, timeReport2.getId());
	}

	@Test
	public void testGetWeek() {
		assertEquals(1, timeReport1.getWeek());
		assertEquals(5, timeReport2.getWeek());
	}

	@Test
	public void testSetWeek() {
		timeReport1.setWeek(2);
		timeReport2.setWeek(6);
		assertEquals(2, timeReport1.getWeek());
		assertEquals(6, timeReport2.getWeek());
	}

	@Test
	public void testIsSigned() {
		assertFalse(timeReport1.isSigned());
		assertTrue(timeReport2.isSigned());
	}

	@Test
	public void testSetSigned() {
		timeReport1.setSigned(true);
		timeReport2.setSigned(false);
		assertTrue(timeReport1.isSigned());
		assertFalse(timeReport2.isSigned());
	}

	@Test
	public void testGetUserId() {
		assertEquals(2, timeReport1.getUserId());
		assertEquals(6, timeReport2.getUserId());
	}

	@Test
	public void testSetUserId() {
		timeReport1.setUserId(3);
		timeReport2.setUserId(7);
		assertEquals(3, timeReport1.getUserId());
		assertEquals(7, timeReport2.getUserId());
	}

	@Test
	public void testGetProjectGroupId() {
		assertEquals(3, timeReport1.getProjectGroupId());
		assertEquals(7, timeReport2.getProjectGroupId());
	}

	@Test
	public void testSetProjectGroupId() {
		timeReport1.setProjectGroupId(4);
		timeReport2.setProjectGroupId(8);
		assertEquals(4, timeReport1.getProjectGroupId());
		assertEquals(8, timeReport2.getProjectGroupId());
	}

}
