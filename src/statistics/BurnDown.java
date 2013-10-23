package statistics;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/BurnDown")
public class BurnDown extends StatisticsMenu {
	public BurnDown() {
		// Konstruktor
	}
	
	
	/** 
	 * 
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		// Kolla vilken roll user_Permission den inloggade användaren har.
		
		// Om admin, generera projectGroupForm
		// Annars, generera burn-down för den gruppen som inloggad användare tillhör
		
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		//Hit kommer man efter att admin har valt projektgrupp.
		// Visa burndown
	}
	
	/**
	 * Genererar HTML-kod där admin får välja projektgrupp att se BurnDown för.
	 * 
	 * 
	 * @return Sträng med HTML-kod som innehåller formuläret
	 */
	private String projectGroupForm() {
		// kolla i databasen efter tillgängliga projektgrupper. generera lista med val åt admin.
		return null; // returnera html-sträng som ger admin .
	}
	
	
	/**
	 * Genererar html-kod som innehåller BurnDown-plotten.
	 * @param burnDownData innehåller ***
	 * @return Sträng som innehåller HTML-kod som visar BurnDown plotten.
	 */
	private String printBurnDown(HashMap<String, Integer> burnDownData) {
		// returnerar tabell med datan...
		return null;
	}
}
