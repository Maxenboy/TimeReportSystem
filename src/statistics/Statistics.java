// HALL� D�R!

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
		// 1. kolla vilken roll anv�ndaren har. Om admin, l�t admin v�lja 
		// hantera vad som h�nder d� sidan laddas.
	}
	
	public void doPost() {
		// hantera om anv. matat in val.
	}
	
	private String projectGroupForm() {
		// Samam metod i BurnDown
		return null; // returnera html-str�ng.
	}

	
	private HashMap<String, ArrayList<String>> getFilter(int projectGroup) {

		return null;
	}
	
	private HashMap<String, ArrayList<String>> getStats() {
		//Metod som h�mtar anv�ndarens inmatade filter. Anropar sedan i sin tur en metod i databasklassen som returnerar Map:en med �nskad statistik.		
		return null;
	}
	
	private String printTable(HashMap<String, ArrayList<String>> table) {
		// Metod som skriver ut �nskad statistik p� tabellform. 
		return null;
	}
	
	private String printGraph(HashMap<String, ArrayList<String>> table) {
		//  Metod f�r att skriva ut ett stapeldiagram med vecka som x-axel och rapporterad tid p� y-axeln.  
		return null;
	}

	
}