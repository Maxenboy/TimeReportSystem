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

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Which group : <input type=" + formElement("text")
				+ " name=" + formElement("addname") + '>';
		html += "<p> Which user : <input type=" + formElement("text") + " status="
				+ formElement("status") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Get group") + '>';
		html += "</form>";
		return html;
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
}
