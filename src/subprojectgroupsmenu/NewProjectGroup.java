package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;
import database.ProjectGroup;

public class NewProjectGroup {
	private ProjectGroups projectgroup;
	private Database db;

	public void NewProjectGroup() {
		projectgroup = new ProjectGroups();
		db = new Database();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String s = addGroupForm();
		if (s == null) {
			out.print("<p> Nothing to show </p>");
		} else {
			switch (request.getParameter("success")) {
			case "null":
				out.print(s);
				break;
			case "true":
				out.print("g");
				break;
			case "false":
				out.print(getPageIntro()
						+ "$(alert(\"The project name stated is already in use\"))" + s);
			}

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession(true);
		String projectname = request.getParameter("name");
		String startweek = request.getParameter("startweek");
		String endweek = request.getParameter("endweek");
		String estim = request.getParameter("estimatedhours");
		boolean found = false;
		for (ProjectGroup pg : db.getProjectGroups()) {
			if (pg.getProjectName().equals(projectname)) {
				found = true;
			}
		}
		if (found|| !validateInput(projectname, startweek, endweek, estim)) {
			response.sendRedirect(request.getRequestURI() + "success=false");
		} else {
			projectgroup.createProjectGroup(projectname, startweek, endweek,
					estim);
			response.sendRedirect(request.getRequestURI() + "success=true");

		}
	}

	private String addGroupForm() {
		String html;
		html = "<p> <form name=" + formElement("input") + "id="
				+ formElement("addusertogroup") + '>';
		html += " method=" + formElement("get");
		html += "<p> Project name: <input type=" + formElement("text")
				+ " name=" + formElement("name") + '>';
		html += "<p> Startweek: <input type=" + formElement("text") + " name="
				+ formElement("startweek") + '>';
		html += "<p> Endweek: <input type=" + formElement("text") + " name="
				+ formElement("endweek") + '>';
		html += "<p> Estimated hours: <input type=" + formElement("text")
				+ " name=" + formElement("estimatedhours") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Add Group") + '>';
		html += "</form>" + "</body></html>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Add member to project group </title>"
				+ getPageJs() + "</head>" + "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}
	private boolean validateInput(String name, String startWeek, String endWeek, String estimatedHours){
		boolean notOK=false;
		if (name.length()>10
				&& !Pattern.matches("\\w", name)) {
			notOK= true;
		}
		if(!Pattern.matches("\\d", startWeek)){
			notOK=true;
		}
		if(!Pattern.matches("\\d", endWeek)){
			notOK=true;
		}
		return !notOK;
	}
}
