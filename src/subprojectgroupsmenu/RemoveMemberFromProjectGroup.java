package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;
import subusersmenu.Users;

@WebServlet("/RemoveMemberFromProjectGroup")
public class RemoveMemberFromProjectGroup extends gui.ProjectGroupsMenu {

	private static final long serialVersionUID = 4921473121575805469L;
	private ProjectGroups group = new ProjectGroups(db);
	private Users users = new Users(db);
	/**
	 * doGet-metoden anropas alla g\u00E5nger d\u00E5 en adminstrat\u00F6r vill ta bort en anv\u00E4ndare fr\u00E5n en projektgrupp.
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession();
			int userPermission = (Integer) session.getAttribute("user_permissions");
			if (userPermission == PERMISSION_ADMIN) {
				PrintWriter out = response.getWriter();
				out.print(getPageIntro());
				out.append(generateMainMenu(userPermission, request));
				out.print(generateSubMenu(userPermission));
				if (request.getParameter("username") == null) {
					out.print(users.showUsers(db.getUsers()));
					out.print("<script>$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"\u00C4r du s\u00E4ker?\");if (confirmed) {$(this).submit();}});</script>");
				} else {
					User user = db.getUser(Integer.parseInt(request
							.getParameter("username")));
					if (user.getProjectGroupId() != 0) {
						HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
						map.put(user.getId(), User.ROLE_NO_ROLE);
						db.setUserRoles(map);
						db.removeUserFromProjectGroup(user.getId(),
								user.getProjectGroupId());
						if (db.getUsers(user.getProjectGroupId()).isEmpty()) {
							out.print(db.getProjectGroup(user.getProjectGroupId()).getProjectName() + " \u00E4r tom.");
						} else {
							out.print(group.showProjectGroup(db.getUsers(user
									.getProjectGroupId())));
						}
					} else {
						out.print("<script>$(alert(\"Anv\u00E4ndaren har ingen projektgrupp och kan d\u00E4rf\u00F6r inte tas bort ur en\"))</script>");
					}
				}
				out.print(getPageOutro());
			} else {
				response.sendRedirect("");
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}

}
