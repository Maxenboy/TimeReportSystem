package subProjectMembersMenu;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

public class ShowProjectMembers  extends HttpServlet{
/**
	 * 
	 */
	private static final long serialVersionUID = -6458493959837089759L;
private ProjectMembers pm;
private Database db;
	public ShowProjectMembers() {
		db=new Database();
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String s = groupForm();
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else if(request.getParameter("success").equals(false)){
			out.print("<p> THe group specefied does not exist </p>");
			}else{
				pm=new ProjectMembers(request.getParameter("groupname"));
				out.print(pm.showMembers(db.getUsers(Integer.valueOf(request.getParameter("groupname")))));
			}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		String groupname=request.getParameter("groupname");
		if(db.getProjectGroup(Integer.valueOf(groupname))!=null){
			response.sendRedirect(request.getRequestURI() + "success=true"+"groupname="+groupname);
		}else{
			response.sendRedirect(request.getRequestURI() + "success=false");
		}
	}
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> Users </title></head>"
				+ "<body>";
		return intro;
	}
	private String groupForm() {
		String html;
		html = "<p> <form name=" + formElement("input");
		html += " method=" + formElement("POST");
		html += "<p> Username : <input type=" + formElement("text") + " name="
				+ formElement("groupname") + '>';
		html += "<input type=" + formElement("submit") + '>';
		html += "</form>";
		return html;
	}

	private String formElement(String par) {
		return '"' + par + '"';
	}
}
