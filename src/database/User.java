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

	private int id;
	private String username;
	private String password;
	private boolean active;
	private int role;
	private int projectGroup;

	/**
	 * Constructor used by database
	 * @param id
	 * @param username
	 * @param password
	 * @param active
	 * @param role
	 * @param projectGroup
	 */
	public User(int id, String username, String password, boolean active,
			int role, int projectGroup) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.active = active;
		this.role = role;
		this.projectGroup = projectGroup;
	}

	/**
	 * Constructor used for creating a new record.
	 * Auto generates password, sets role to ROLE_NO_ROLE, active to true and projectGroup to 0
	 * @param username
	 */
	public User(String username) {
		this.username = username;
		this.role = ROLE_NO_ROLE;
		this.active = true;
		this.projectGroup = 0;
		password = generatePassword();
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role
	 */
	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * @return
	 */
	public int getProjectGroup() {
		return projectGroup;
	}

	/**
	 * @param projectGroup
	 */
	public void setProjectGroup(int projectGroup) {
		this.projectGroup = projectGroup;
	}
	
	/**
	 * @return
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + projectGroup;
		result = prime * result + role;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (active != other.active)
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (projectGroup != other.projectGroup)
			return false;
		if (role != other.role)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	
}
