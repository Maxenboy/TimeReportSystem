// HALL D€R!

package statistics;
import java.util.ArrayList;
import java.util.HashMap;

import database.*;
import base.servletBase; // OBS Ska extenda StatisticsMenu egentligen..!

public class Statistics extends StatisticsMenu {
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
		// Samam metod i BurnDown
		return null; // returnera html-sträng.
	}

	
	private HashMap<String, ArrayList<String>> getFilter(int projectGroup) {

		return null;
	}
	
	private HashMap<String, ArrayList<String>> getStats() {
		//Metod som hämtar användarens inmatade filter. Anropar sedan i sin tur en metod i databasklassen som returnerar Map:en med önskad statistik.		
		return null;
	}
	
	private String printTable(HashMap<String, ArrayList<String>> table) {
		// Metod som skriver ut önskad statistik på tabellform. 
		return null;
	}
	
	private String printGraph(HashMap<String, ArrayList<String>> table) {
		//  Metod för att skriva ut ett stapeldiagram med vecka som x-axel och rapporterad tid på y-axeln.  
		return null;
	}

	
}