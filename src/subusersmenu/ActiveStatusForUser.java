package subusersmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

public class ActiveStatusForUser {
	private Users users;

	public ActiveStatusForUser() {
		users = new Users();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String s = groupForm();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Add member to project group </title>"
				+ getPageJs() + "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Are you sure?\");if (confirmed) {$(this).submit();}});";
	}

	public String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Username : <input type=" + formElement("text") + " name="
				+ formElement("username") + '>';
		html += "<p> Group id : <input type=" + formElement("text") + " group="
				+ formElement("groupid") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;

	}

	private String formElement(String par) {
		return '"' + par + '"';
	}
}
