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
		
		// Kolla vilken roll user_Permission den inloggade anv�ndaren har.
		
		// Om admin, generera projectGroupForm
		// Annars, generera burn-down f�r den gruppen som inloggad anv�ndare tillh�r
		
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		//Hit kommer man efter att admin har valt projektgrupp.
		// Visa burndown
	}
	
	/**
	 * Genererar HTML-kod d�r admin f�r v�lja projektgrupp att se BurnDown f�r.
	 * 
	 * 
	 * @return Str�ng med HTML-kod som inneh�ller formul�ret
	 */
	private String projectGroupForm() {
		// kolla i databasen efter tillg�ngliga projektgrupper. generera lista med val �t admin.
		return null; // returnera html-str�ng som ger admin .
	}
	
	
	/**
	 * Genererar html-kod som inneh�ller BurnDown-plotten.
	 * @param burnDownData inneh�ller ***
	 * @return Str�ng som inneh�ller HTML-kod som visar BurnDown plotten.
	 */
	private String printBurnDown(HashMap<String, Integer> burnDownData) {
		// returnerar tabell med datan...
		return null;
	}
}
