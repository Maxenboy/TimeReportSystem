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
		
		// Konstruktor. Ha n�got h�r �ht?
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Database db = new Database();
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		int userPermission = (Integer) session.getAttribute("user_Permissions");	// m�ste typecasta?
		int projectGroupID = (Integer) session.getAttribute("project_group_id");		
		String userName = (String) session.getAttribute("username");
		
		switch(userPermission) {
		case 1: // Administrator
			out.append(projectGroupForm());
			// efter att admin har valt projektgrupp och tryck p� submit kommer doPost-metod att anropas.
		
		case 2: // Project leader
				out.append(printFilter(db.getStatisticsFilter(projectGroupID)));
				// efter att anv�ndaren har fyllt i filter och tryckt submit kommer doPost-metoden att anropas.
				
		case 3: // Utan roll
			// Vad g�ra h�r?
			
		case 4:
			HashMap<String, ArrayList<String>> filters = db.getStatisticsFilter(projectGroupID); 
			// �ndra p� listan med users, ska endast vara anv�ndarens anv�ndarnamn.
		}
		

		out.println(getPageIntro());
		
		out.println("OK</body></html>");

		
		// 1. kolla vilken roll anv�ndaren har. Om admin, l�t admin v�lja pg
		// hantera vad som h�nder d� sidan laddas.
		
		// 2 om pl eller anv�ndare, generera filter.. Anv�ndare och PL har olika filter...
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
		return null; // returnera html-str�ng.
	}

	
	/**
	 * 
	 * @return
	 */
	private HashMap<String, ArrayList<String>> getStats() {
		//Metod som h�mtar anv�ndarens inmatade filter. Anropar sedan i sin tur en metod i databasklassen som returnerar Map:en med �nskad statistik.		
		return null;
	}
	
	/**
	 * 
	 * @param table
	 * @return
	 */
	private String printTable(HashMap<String, ArrayList<String>> table) {
		// Metod som skriver ut �nskad statistik p� tabellform. 
		return null;
	}
	
	/**
	 * 
	 * @param table
	 * @return
	 */
	private String printGraph(HashMap<String, ArrayList<String>> table) {
		//  Metod f�r att skriva ut ett stapeldiagram med vecka som x-axel och rapporterad tid p� y-axeln.  
		// HUR G�RA???
		return null;
		
	}
	private String printFilter(HashMap<String, ArrayList<String>> table) {
		// returnerar HTML-str�ng med filterl�dorna!
		return null;
	
	}
}