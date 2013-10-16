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
	 * Genererar HTML-kod där admin får välja projektgrupp att se BurnDown för.
	 * 
	 * 
	 * @return Sträng med HTML-kod som innehåller formuläret
	 */
	private String projectGroupForm() {
		
		return null; // returnera html-sträng.
	}
	
	
	/**
	 * Genererar html-kod som innehåller BurnDown-plotten.
	 * @param burnDownData innehåller ***
	 * @return Sträng som innehåller HTML-kod som visar BurnDown plotten.
	 */
	private String printBurnDown(HashMap<String, Integer> burnDownData) {
		return null;
	}
}
