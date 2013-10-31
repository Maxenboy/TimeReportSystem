package subusersmenu;
import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import base.*;

@WebServlet("/ShowUsers")
public class ShowUsers extends UsersMenu {
/**
	 * 
	 */
	private static final long serialVersionUID = -2190552005706112015L;
private Users users;
	public ShowUsers() {
		users= new Users();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		
		out.print(generateMainMenu((int)session.getAttribute("user_permissions")));
		out.print(generateSubMenu((int)session.getAttribute("user_permissions")));
		String s = users.showUsers(db.getUsers());
		if(s == null)
			out.print("<p> Inget att visa </p>");
		else 
			out.print(s);
		
		out.print(getPageOutro());
	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
}
