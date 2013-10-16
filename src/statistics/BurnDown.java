package statistics;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import base.servletBase;

public class BurnDown extends StatisticsMenu {
	public BurnDown() {
		// Konstruktor
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	private String projectGroupForm() {
		
		return null; // returnera html-sträng.
	}
	
	private String printBurnDown(HashMap<String, Integer> burnDownData) {
		return null;
	}
}
