package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import database.*;
import subProjectMembersMenu.ProjectMembers;

@WebServlet("/ActiveStatusForProjectGroup")
public class ActiveStatusForProjectGroup extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2713701817050035720L;
	/**
	 * 
	 */
	ProjectGroups group;
	Database db;
	
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		db = new Database();
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		if(request.getParameter("sucess") == null) {
			out.print(getPageIntro() + groupForm() + getPageOutro());
		}
		else if (request.getParameter("sucess").equals("true")) {
			out.print(getPageIntro() + "Sucess! </body></html>");
		}
		else if(request.getParameter("sucess").equals("false")) {
			out.print(getPageIntro() + "<script>$(alert(\"Incorrect input.\"))</script>" + groupForm() + getPageOutro());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		String name = request.getParameter("name");
		String status = request.getParameter("status");
		if (checkGroup(name, status)) {
			response.sendRedirect(request.getRequestURI() + "sucess=true");
		} else {
			response.sendRedirect(request.getRequestURI() + "sucess=false");
		}
	}

	private boolean checkGroup(String name, String status) {
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g : groups) {
			if (g.getProjectName().equals(name)) {
				if (status.equals("true")) {
					group.toggleActiveProjectGroup(g.getId(), true);
					return true;
				} else if (status.equals("false")) {
					group.toggleActiveProjectGroup(g.getId(), false);
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}

	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title>"
				+ getPageJs() +"</head>"
				+ "<body>";
		return intro;
	}

	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>";
	}

	private String getPageOutro() {
		String outro = "</body></html>";
		return outro;
	}

	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Which group : <input type=" + formElement("text")
				+ " name=" + formElement("name") + '>';
		html += "<p> Status : <input type=" + formElement("text") + " status="
				+ formElement("status") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Get group") + '>';
		html += "</form>";
		return html;
	}

}
