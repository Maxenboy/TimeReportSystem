package statistics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.*;

public class Statistics extends StatisticsMenu {
	public Statistics() {
		//Database db = new Database();
		
		// Konstruktor
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		// 1. kolla vilken roll användaren har. Om admin, låt admin välja 
		// hantera vad som händer då sidan laddas.
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// hantera om anv. matat in val.
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private String projectGroupForm() {
		// Samam metod i BurnDown
		return null; // returnera html-sträng.
	}

	/**
	 * 
	 * 
	 * @param projectGroup
	 * @return
	 */
	private HashMap<String, ArrayList<String>> getFilter(int projectGroup) {

		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap<String, ArrayList<String>> getStats() {
		//Metod som hämtar användarens inmatade filter. Anropar sedan i sin tur en metod i databasklassen som returnerar Map:en med önskad statistik.		
		return null;
	}
	
	/**
	 * 
	 * @param table
	 * @return
	 */
	private String printTable(HashMap<String, ArrayList<String>> table) {
		// Metod som skriver ut önskad statistik på tabellform. 
		return null;
	}
	
	/**
	 * 
	 * @param table
	 * @return
	 */
	private String printGraph(HashMap<String, ArrayList<String>> table) {
		//  Metod för att skriva ut ett stapeldiagram med vecka som x-axel och rapporterad tid på y-axeln.  
		return null;
	}

	
}