package statistics;
import database.*;

import base.servletBase; // OBS Ska extenda StatisticsMenu egentligen..!

public class Statistics extends servletBase {
	public Statistics() {
		//Database db = new Database();
		
		// Konstruktor
	}
	
	public void doGet() {
		// 1. kolla vilken roll användaren har. Om admin, låt admin välja 
		// hantera vad som händer då sidan laddas.
	}
	
	public void doPost() {
		// hantera om anv. matat in val.
	}
	
	private String projectGroupForm() {
		
		return null; // returnera html-sträng.
	}

}