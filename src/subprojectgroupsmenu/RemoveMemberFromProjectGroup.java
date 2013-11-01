package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;
import subusersmenu.Users;

@WebServlet("/RemoveMemberFromProjectGroup")
public class RemoveMemberFromProjectGroup extends gui.ProjectGroupsMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4921473121575805469L;
	ProjectGroups group = new ProjectGroups();
	Users users = new Users();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int userPermission = (Integer) session.getAttribute("user_permissions");
		out.append(generateMainMenu(userPermission, request));
		out.print(generateSubMenu(userPermission));
		if (request.getParameter("username") == null) {
			out.print(users.showUsers(db.getUsers()));
			out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00E4ker?\");if (confirmed) {$(this).submit();}});</script>");
		} else {
			User user = db.getUser(Integer.parseInt(request
					.getParameter("username")));
			if (user.getProjectGroup() != 0) {
				db.removeUserFromProjectGroup(user.getId(),
						user.getProjectGroup());
				if (db.getUsers(user.getProjectGroup()).isEmpty()) {
					out.print(db.getProjectGroup(user.getProjectGroup()).getProjectName() + " \u00E4r tom.");
				} else {
					out.print(group.showProjectGroup(db.getUsers(user
							.getProjectGroup())));
				}
			} else {
				out.print("<script>$(alert(\"Anv\u00E4ndaren har ingen projektgrupp och kan d\u00E4rf\u00F6r inte tas bort ur en\"))</script>");
			}
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

	}

}
