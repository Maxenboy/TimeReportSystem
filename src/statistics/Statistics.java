package statistics;
import java.util.*;
import java.io.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.*;


@WebServlet("/Statistics")

public class Statistics extends StatisticsMenu {
	public Statistics() {
		//Database db = new Database();
		
		// Konstruktor. Ha något här öht?
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		int userPermission = (Integer) session.getAttribute("user_Permissions");	// måste typecasta?
		int projectGroupID = (Integer) session.getAttribute("project_group_id");		
		String userName = (String) session.getAttribute("username");
		
		switch(userPermission) {
		case 1: // Administrator
			out.append(projectGroupForm());
			// efter att admin har valt projektgrupp och tryck på submit kommer doPost-metod att anropas.
		
		case 2: // Project leader
				out.append(printFilter(db.getStatisticsFilter(projectGroupID)));
				// efter att användaren har fyllt i filter och tryckt submit kommer doPost-metoden att anropas.
				
		case 3: // Utan roll
			// Vad göra här?
			
		case 4:
			HashMap<String, ArrayList<String>> filters = db.getStatisticsFilter(projectGroupID); 
			// ändra på listan med users, ska endast vara användarens användarnamn.
		}
		

		out.println(getPageIntro());
		
		out.println("OK</body></html>");

		
		// 1. kolla vilken roll användaren har. Om admin, låt admin välja pg
		// hantera vad som händer då sidan laddas.
		
		// 2 om pl eller användare, generera filter.. Användare och PL har olika filter...
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// hantera om anv. matat in val.
		// kolla vilken data som postats...
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
		// HUR GÖRA???
		return null;
		
	}
	private String printFilter(HashMap<String, ArrayList<String>> table) {
		// returnerar HTML-sträng med filterlådorna!
		return null;
	
	}
}