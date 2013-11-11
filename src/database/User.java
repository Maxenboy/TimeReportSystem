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
	private int projectGroupId;

	/**
	 * Konstruktor som ska anv\u00E4ndas av databasklassen f\u00F6r att skapa ett 
	 * objekt av en befintlig anv\u00E4ndare.
	 * @param id
	 * @param username
	 * @param password
	 * @param active
	 * @param role
	 * @param projectGroupId
	 */
	public User(int id, String username, String password, boolean active, int role, int projectGroupId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.active = active;
		this.role = role;
		this.projectGroupId = projectGroupId;
	}

	/**
	 * Konstruktor f\u00F6r att skapa en ny aktiv anv\u00E4ndare. L\u00F6senordet autogenereras, 
	 * rollen s\u00E4tts till ROLE_NO_ROLE och projectgruppsid s\u00E4tts till 0.
	 * @param username
	 */
	public User(String username) {
		this.username = username;
		this.role = ROLE_NO_ROLE;
		this.active = true;
		this.projectGroupId = 0;
		password = generatePassword();
	}

	/**
	 * Returnerar anv\u00E4ndarens id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * S\u00E4tter anv\u00E4ndarens id.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returnerar anv\u00E4ndarens anv\u00E4ndarnamn.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * S\u00E4tter anv\u00E4ndarens anv\u00E4ndarnamn.
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returnerar anv\u00E4ndarens l\u00F6senord.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * S\u00E4tter anv\u00E4ndarens l\u00F6senord.
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returnerar huruvida anv\u00E4ndaren \u00E4r aktiv eller inte.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * S\u00E4tter anv\u00E4ndaren till aktiv eller inaktiv.
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returnerar anv\u00E4ndarens roll.
	 */
	public int getRole() {
		return role;
	}

	/**
	 * S\u00E4tter anv\u00E4ndarens roll.
	 * @param role
	 */
	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * Returnerar anv\u00E4ndarens projektgruppsid.
	 */
	public int getProjectGroupId() {
		return projectGroupId;
	}

	/**
	 * S\u00E4tter anv\u00E4ndarens projektgruppsid.
	 * @param projectGroupId
	 */
	public void setProjectGroupId(int projectGroupId) {
		this.projectGroupId = projectGroupId;
	}
	
	/**
	 * Genererar l\u00F6senord.
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
		result = prime * result + projectGroupId;
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
		if (projectGroupId != other.projectGroupId)
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", active=" + active + ", role=" + role
				+ ", projectGroupId=" + projectGroupId + "]";
	}
}
