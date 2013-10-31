package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import database.Database;

@WebServlet("/AddMemberToProjectGroup")
public class AddMemberToProjectGroup extends gui.ProjectGroupsMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1961915720341016655L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		Database db = new Database();
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(generateMainMenu((Integer) session
				.getAttribute("user_permissions")));
		out.print(generateSubMenu((Integer) session
				.getAttribute("user_permissions")));
		if(request.getParameter("sucess") == null) {
			out.print(groups.addUserForm());
		}
		else if(request.getParameter("sucess").equals("true")) {
			out.print(groups.showProjectGroup(db.getUsers(Integer
							.parseInt(request.getParameter("groupid")))));
		}
		else if (request.getParameter("sucess").equals("false")) {
			out.print("<script>$(alert(\"Inkorrekt input.\"))</script>"
					+ groups.addUserForm());
		}
		out.print(getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		String user = request.getParameter("name");
		String groupid = request.getParameter("groupid");
		if (groups.addUserToProjectGroup(user, Integer.parseInt(groupid))) {
			response.sendRedirect(request.getRequestURI()
					+ "success=true&groupid=" + groupid);
		} else {
			response.sendRedirect(request.getRequestURI() + "sucess=false");
		}
	}

	
}
