package statistics;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BurnDown extends StatisticsMenu {
	public BurnDown() {
		// Konstruktor
	}
	
	
	/** 
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	/**
	 * Genererar HTML-kod d�r admin f�r v�lja projektgrupp att se BurnDown f�r.
	 * 
	 * 
	 * @return Str�ng med HTML-kod som inneh�ller formul�ret
	 */
	private String projectGroupForm() {
		
		return null; // returnera html-str�ng.
	}
	
	
	/**
	 * Genererar html-kod som inneh�ller BurnDown-plotten.
	 * @param burnDownData inneh�ller ***
	 * @return Str�ng som inneh�ller HTML-kod som visar BurnDown plotten.
	 */
	private String printBurnDown(HashMap<String, Integer> burnDownData) {
		return null;
	}
}
