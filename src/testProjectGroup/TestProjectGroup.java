package testProjectGroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.ProjectGroup;

public class TestProjectGroup {
	private ProjectGroup projectGroup1, projectGroup2;

	@Before
	public void setUp() throws Exception {
		projectGroup1 = new ProjectGroup(1, "puss1302", true, 1, 10, 6000);
		projectGroup2 = new ProjectGroup("puss1303", 5, 20, 8000); 
	}

	@After
	public void tearDown() throws Exception {
		projectGroup1 = null;
		projectGroup2 = null;
	}
	
	@Test
	public void testGetId() {
		assertEquals(1, projectGroup1.getId());
		assertEquals(0, projectGroup2.getId());
	}

	@Test
	public void testSetId() {
		projectGroup1.setId(2);
		projectGroup2.setId(3);
		assertEquals(2, projectGroup1.getId());
		assertEquals(3, projectGroup2.getId());
	}

	@Test
	public void testGetProjectName() {
		assertEquals("puss1302", projectGroup1.getProjectName());
		assertEquals("puss1303", projectGroup2.getProjectName());
	}

	@Test
	public void testSetProjectName() {
		projectGroup1.setProjectName("puss1301");
		projectGroup2.setProjectName("puss1304");
		assertEquals("puss1301", projectGroup1.getProjectName());
		assertEquals("puss1304", projectGroup2.getProjectName());
	}

	@Test
	public void testIsActive() {
		assertTrue(projectGroup1.isActive());
		assertTrue(projectGroup2.isActive());
	}

	@Test
	public void testSetActive() {
		projectGroup1.setActive(false);
		projectGroup2.setActive(false);
		assertFalse(projectGroup1.isActive());
		assertFalse(projectGroup2.isActive());
	}

	@Test
	public void testGetStartWeek() {
		assertEquals(1, projectGroup1.getStartWeek());
		assertEquals(5, projectGroup2.getStartWeek());
	}

	@Test
	public void testSetStartWeek() {
		projectGroup1.setStartWeek(2);
		projectGroup2.setStartWeek(6);
		assertEquals(2, projectGroup1.getStartWeek());
		assertEquals(6, projectGroup2.getStartWeek());
	}

	@Test
	public void testGetEndWeek() {
		assertEquals(10, projectGroup1.getEndWeek());
		assertEquals(20, projectGroup2.getEndWeek());
	}

	@Test
	public void testSetEndWeek() {
		projectGroup1.setEndWeek(11);
		projectGroup2.setEndWeek(21);
		assertEquals(11, projectGroup1.getEndWeek());
		assertEquals(21, projectGroup2.getEndWeek());
	}

	@Test
	public void testGetEstimatedTime() {
		assertEquals(6000, projectGroup1.getEstimatedTime());
		assertEquals(8000, projectGroup2.getEstimatedTime());
	}

	@Test
	public void testSetEstimatedTime() {
		projectGroup1.setEstimatedTime(7000);
		projectGroup2.setEstimatedTime(9000);
		assertEquals(7000, projectGroup1.getEstimatedTime());
		assertEquals(9000, projectGroup2.getEstimatedTime());
	}

}
