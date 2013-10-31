package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.ProjectGroup;
import database.User;

@WebServlet("/AddMemberToProjectGroup")
public class AddMemberToProjectGroup extends gui.ProjectGroupsMenu {

	private String groupName = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1961915720341016655L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(generateMainMenu((Integer) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((Integer) session
				.getAttribute("user_permissions")));
		if (request.getParameter("thegroup") == null && groupName == "") {
			out.print(showProjectGroups());
		} else {
			if (request.getParameter("reportId") == null) {
				if (groupName == "") {
					groupName = request.getParameter("thegroup");
				}
				ArrayList<User> users = new ArrayList<User>();
				for (User u : db.getUsers()) {
					if (u.getProjectGroup() == 0) {
						users.add(u);
					}
				}
				if (users.isEmpty()) {
					out.print("<script>$(alert(\"Finns inga användare utan projektgrupp!\"))</script>");
				} else {
					out.print(groups.showProjectGroup(users));
				}
			} else {
				if (groups.addUserToProjectGroup(
						db.getUser(Integer.parseInt(request.getParameter("reportId")))
								.getUsername(), Integer.parseInt(groupName))) {
					out.print(groups.showProjectGroup(db.getUsers(Integer
							.parseInt(groupName))));
					groupName = "";
				} else {
					out.print("<script>$(alert(\"Användaren är redan med i en projektgrupp!\"))</script>"
							+ groups.showProjectGroup(db.getUsers(Integer
									.parseInt(groupName))));
					groupName = "";
				}
			}
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
	}

	private String showProjectGroups() {
		StringBuilder sb = new StringBuilder();
		sb.append("<FORM METHOD=" + formElement("get") + ">");
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
		sb.append("<table border=" + formElement("1") + ">");
		sb.append("<tr>");
		sb.append("<th>Projektgrupp</th>");
		sb.append("<th>Starvecka</th>");
		sb.append("<th>Slutvecka</th>");
		sb.append("<th>Estimerat antal timmar</th>");
		sb.append("<th>Aktiv</th>");
		sb.append("<th>Välj</th>");
		sb.append("</tr>");
		return sb.toString();
	}

	private String createRadio(int id) {
		return "<input type=" + formElement("radio") + "name="
				+ formElement("thegroup") + "value="
				+ formElement(Integer.toString(id)) + ">";
	}

}
