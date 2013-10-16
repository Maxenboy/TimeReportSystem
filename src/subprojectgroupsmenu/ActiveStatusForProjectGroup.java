package subprojectgroupsmenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import subProjectMembersMenu.ProjectMembers;

@WebServlet("/ActiveStatusForProjectGroup")
public class ActiveStatusForProjectGroup {

	ProjectGroups group;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro() + groupForm() + getPageOutro());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
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
				+ " name=" + formElement("addname") + '>';
		html += "<input type=" + formElement("submit") + "value="
				+ formElement("Get group") + '>';
		html += "</form>";
		return html;
	}
	
}
