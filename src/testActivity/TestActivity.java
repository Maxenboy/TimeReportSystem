package testActivity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.Activity;

public class TestActivity {
	Activity activity1, activity2;

	@Before
	public void setUp() throws Exception {
		activity1 = new Activity(Activity.ACTIVITY_NR_SDDD, Activity.ACTIVITY_TYPE_DEVELOPMENT, 300, 1);
		activity2 = new Activity(1, Activity.ACTIVITY_NR_SRS, Activity.ACTIVITY_TYPE_REWORK, 100, 2);
	}

	@After
	public void tearDown() throws Exception {
		activity1 = null;
		activity2 = null;
	}

	@Test
	public void testGetId() {
		assertEquals(0, activity1.getId());
		assertEquals(1, activity2.getId());
	}

	@Test
	public void testSetId() {
		activity1.setId(2);
		activity2.setId(3);
		assertEquals(2, activity1.getId());
		assertEquals(3, activity2.getId());
	}

	@Test
	public void testGetActivityNr() {
		assertEquals(Activity.ACTIVITY_NR_SDDD, activity1.getActivityNr());
		assertEquals(Activity.ACTIVITY_NR_SRS, activity2.getActivityNr());
	}

	@Test
	public void testSetActivityNr() {
		activity1.setActivityNr(Activity.ACTIVITY_NR_SVVS);
		activity2.setActivityNr(Activity.ACTIVITY_NR_MEETING);
		assertEquals(Activity.ACTIVITY_NR_SVVS, activity1.getActivityNr());
		assertEquals(Activity.ACTIVITY_NR_MEETING, activity2.getActivityNr());
	}

	@Test
	public void testGetActivityType() {
		assertEquals(Activity.ACTIVITY_TYPE_DEVELOPMENT, activity1.getActivityType());
		assertEquals(Activity.ACTIVITY_TYPE_REWORK, activity2.getActivityType());
	}

	@Test
	public void testSetActivityType() {
		activity1.setActivityType(Activity.ACTIVITY_TYPE_INFORMAL_REVIEW);
		activity2.setActivityType(Activity.ACTIVITY_TYPE_FORMAL_REVIEW);
		assertEquals(Activity.ACTIVITY_TYPE_INFORMAL_REVIEW, activity1.getActivityType());
		assertEquals(Activity.ACTIVITY_TYPE_FORMAL_REVIEW, activity2.getActivityType());
	}

	@Test
	public void testGetTime() {
		assertEquals(300, activity1.getTime());
		assertEquals(100, activity2.getTime());
	}

	@Test
	public void testSetTime() {
		activity1.setTime(400);
		activity2.setTime(200);
		assertEquals(400, activity1.getTime());
		assertEquals(200, activity2.getTime());
	}

	@Test
	public void testGetTimeReportId() {
		assertEquals(1, activity1.getTimeReportId());
		assertEquals(2, activity2.getTimeReportId());
	}

	@Test
	public void testSetTimeReportId() {
		activity1.setTimeReportId(3);
		activity2.setTimeReportId(4);
		assertEquals(3, activity1.getTimeReportId());
		assertEquals(4, activity2.getTimeReportId());
	}

}
