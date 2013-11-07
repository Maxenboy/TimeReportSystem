package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import database.*;

@WebServlet("/ActiveStatusForProjectGroup")
public class ActiveStatusForProjectGroup extends gui.ProjectGroupsMenu {

	private static final long serialVersionUID = -2713701817050035720L;
	private ProjectGroups group = new ProjectGroups(db);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int userPermission = (Integer) session
					.getAttribute("user_permissions");
			out.print(getPageIntro());
			out.append(generateMainMenu(userPermission, request));
			out.print(generateSubMenu(userPermission));
			if (request.getParameter("thegroup") == null) {
				out.print("<script>$('#Activate/Inactivategroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00E4ker?\");if (confirmed) {$(this).submit();}});</script>"
						+ showProjectGroups());
			} else {
				if (!hasActiveMembers(request.getParameter("thegroup"))) {
					if (checkGroup(request.getParameter("thegroup"))) {
						out.print(showProjectGroups());
					} else {
						out.print("<script>$(alert(\"Inkorrekt input.\"))</script>"
								+ "<script>$('#Activate/Inactivategroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00E4ker?\");if (confirmed) {$(this).submit();}});</script>"
								+ showProjectGroups());
					}
				} else {
					out.print("<script>$(alert(\"Du kan inte inaktivera en grupp med aktiva medlemmar. Dessa m\u00E5ste f\u00F6rst inaktiveras eller tas bort fr\u00E5n projektgrupp.\"))</script>"
							+ showProjectGroups());
				}
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}

	private boolean hasActiveMembers(String name) {
		ArrayList<User> users = db.getUsers(Integer.parseInt(name));
		for (User u : users) {
			if (u.isActive()) {
				return true;
			}
		}
		return false;
	}

	private boolean checkGroup(String name) {
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g : groups) {
			if (g.getId() == Integer.parseInt(name)) {
				if (g.isActive()) {
					if (group.toggleActiveProjectGroup(g.getId(), false)) {
						return true;
					}
				} else {
					group.toggleActiveProjectGroup(g.getId(), true);
					return true;
				}
			}
		}
		return false;
	}

	private String showProjectGroups() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get")
				+ "onsubmit=\"return confirm('\u00C4r du s\u00E4ker?')\"" + ">");
		sb.append(buildProjectGroupsTable());
		List<ProjectGroup> groups = db.getProjectGroups();
		for (ProjectGroup g : groups) {
			sb.append("<tr>");
			sb.append("<td>" + g.getProjectName() + "</td>");
			sb.append("<td>" + g.getStartWeek() + "</td>");
			sb.append("<td>" + g.getEndWeek() + "</td>");
			sb.append("<td>" + g.getEstimatedTime() + "</td>");
			if (g.isActive()) {
				sb.append("<td>" + "Aktiv" + "</td>");
			} else {
				sb.append("<td>" + "Inaktiv" + "</td>");
			}
			sb.append("<td>" + createRadio(g.getId()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		sb.append("<input type=" + formElement("submit") + "value="
				+ formElement("Aktivera/Inaktivera") + ">");
		sb.append("</form>");
		return sb.toString();
	}

	private String buildProjectGroupsTable() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"table table-bordered table-hover\">");
		sb.append("<tr>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Starvecka</th>");
		sb.append("<th>Slutvecka</th>");
		sb.append("<th>Estimerat antal timmar</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("<th>V\u00E4lj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("thegroup") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

}
