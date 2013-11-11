package testUser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import database.User;

public class TestUser {
	User user1, user2;
	
	@Before
	public void setUp() throws Exception {
		user1 = new User("ada10xyz");
		user2 = new User(2, "ain10xyz", "hejhej", false, 6, 1);
	}

	@After
	public void tearDown() throws Exception {
		user1 = null;
		user2 = null;
	}

	@Test
	public void testGetId() {
		assertEquals(0, user1.getId());
		assertEquals(2, user2.getId());
	}

	@Test
	public void testSetId() {
		user1.setId(1);
		user2.setId(3);
		assertEquals(1, user1.getId());
		assertEquals(3, user2.getId());
	}

	@Test
	public void testGetUsername() {
		assertEquals("ada10xyz", user1.getUsername());
		assertEquals("ain10xyz", user2.getUsername());
	}

	@Test
	public void testSetUsername() {
		user1.setUsername("ada10zyx");
		user2.setUsername("ain10zyx");
		assertEquals("ada10zyx", user1.getUsername());
		assertEquals("ain10zyx", user2.getUsername());
	}

	@Test
	public void testGetPassword() {
		assertEquals("hejhej", user2.getPassword());
	}

	@Test
	public void testSetPassword() {
		user1.setPassword("jejeje");
		user2.setPassword("jehjeh");
		assertEquals("jejeje", user1.getPassword());
		assertEquals("jehjeh", user2.getPassword());
	}

	@Test
	public void testIsActive() {
		assertTrue(user1.isActive());
		assertFalse(user2.isActive());
	}

	@Test
	public void testSetActive() {
		user1.setActive(false);
		user2.setActive(true);
		assertEquals(false, user1.isActive());
		assertEquals(true, user2.isActive());
	}

	@Test
	public void testGetRole() {
		assertEquals(3, user1.getRole());
		assertEquals(6, user2.getRole());
	}

	@Test
	public void testSetRole() {
		user1.setRole(4);
		user2.setRole(7);
		assertEquals(4, user1.getRole());
		assertEquals(7, user2.getRole());
	}

	@Test
	public void testGetProjectGroup() {
		assertEquals(0, user1.getProjectGroupId());
		assertEquals(1, user2.getProjectGroupId());
	}

	@Test
	public void testSetProjectGroup() {
		user1.setProjectGroupId(2);
		user2.setProjectGroupId(3);
		assertEquals(2, user1.getProjectGroupId());
		assertEquals(3, user2.getProjectGroupId());
	}

}
