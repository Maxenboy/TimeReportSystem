package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import database.Database;

public class HandleProjectLeader {

	Database db = new Database();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		switch (request.getParameter("session")) {
		case "null":
			break;
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

	}

	private String leaderForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Which group : <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<p> Which user : <input type=" + formElement("text") + " user="
				+ formElement("user") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("make leader") + '>';
		html += "</form>";
		return html;
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
}
