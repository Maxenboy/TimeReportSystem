package database;

import java.util.Random;

public class User {
	public static final int ROLE_ADMIN = 1;
	public static final int ROLE_PROJECT_LEADER = 2;
	public static final int ROLE_NO_ROLE = 3;
	public static final int ROLE_SYSTEM_GROUP = 4;
	public static final int ROLE_SYSTEM_LEADER = 5;
	public static final int ROLE_DEVELOPMENT_GROUP = 6;
	public static final int ROLE_TEST_GROUP = 7;
	public static final int ROLE_TEST_LEADER = 8;

	public int id;
	public String username;
	public String password;
	public boolean active;
	public int role;
	public int projectGroup;

	public User(int id, String username, String password, boolean active,
			int role, int projectGroup) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.active = active;
		this.role = role;
		this.projectGroup = projectGroup;
	}

	public User(String username) {
		this.username = username;
		password = generatePassword();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getProjectGroup() {
		return projectGroup;
	}

	public void setProjectGroup(int projectGroup) {
		this.projectGroup = projectGroup;
	}
	
	private String generatePassword() {
		Random ran = new Random();
		int top = 5;
		char data = ' ';
		String dat = "";

		for (int i=0; i<=top; i++) {
		  data = (char)(ran.nextInt(25)+97);
		  dat = data + dat;
		}
		
		return dat;
	}

}
