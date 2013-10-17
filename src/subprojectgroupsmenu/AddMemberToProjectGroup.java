package subprojectgroupsmenu;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import database.Database;
public class AddMemberToProjectGroup {

	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro() + groups.addUserForm() + getPageOutro());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ProjectGroups groups = new ProjectGroups();
		HttpSession session = request.getSession(true);
		Database db = new Database();
		PrintWriter out = response.getWriter();
		String user = request.getParameter("name");
		String groupid = request.getParameter("groupid");
		if(groups.addUserToProjectGroup(user, Integer.parseInt(groupid))) {
			out.print(getPageIntro() + groups.showProjectGroup(db.getUsers(Integer.parseInt(groupid))) + getPageOutro());
		}
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Add member to project group </title>"
				+ getPageJs()
				+ "</head>"
				+ "<body>";
		return intro;
	}
	
	private String getPageJs() {
		return "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>"
				+ "$('#addusertogroup').submit(function (e) { e.preventDefault(); var confirmed = confirm(\"Are you sure?\");if (confirmed) {$(this).submit();}});";
	}
	
	private String getPageOutro() {
		String outro = "</body></html>";
		return outro;
	}
}
