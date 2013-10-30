package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

import database.*;

@WebServlet("/HandleProjectLeader")
public class HandleProjectLeader extends gui.ProjectGroupsMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2692792293983643296L;
	Database db = new Database();
	ProjectGroups group = new ProjectGroups();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(generateMainMenu((Integer) session.getAttribute("user_permissions")));
		out.print(generateSubMenu((Integer) session.getAttribute("user_permissions")));
		if (request.getParameter("session") == null) {
			out.print(leaderForm());
		} else if (request.getParameter("session").equals("one")) {
			ArrayList<User> list = db.getUsers(Integer.parseInt(request
					.getParameter("groupid")));
			if (list != null) {
				group.showProjectGroup(list);
			}
		} else {
			out.print("<script>$(alert(\"Incorrect input.\"))</script"
					+ leaderForm());
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		String id = request.getParameter("name");
		if (id != null) {
			response.sendRedirect(request.getRequestURI()
					+ "session=one&groupid=" + id);
		} else {
			id = request.getParameter("reportId");
			User user = db.getUser(Integer.parseInt(id));
			user.setRole(user.ROLE_PROJECT_LEADER);
		}
	}

	private String leaderForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("get");
		html += "<p> Which group : <input type=" + formElement("text")
				+ " name=" + formElement("name") + '>';
		html += "<p> Which user : <input type=" + formElement("text")
				+ " user=" + formElement("user") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("make leader") + '>';
		html += "</form>";
		return html;
	}

	

}
