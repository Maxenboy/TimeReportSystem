package subusersmenu;
import gui.UsersMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import base.*;
import database.Database;

@WebServlet("/ShowUsers")
public class ShowUsers extends UsersMenu {
/**
	 * 
	 */
	private static final long serialVersionUID = -2190552005706112015L;
private Users users;
private Database db;
	public ShowUsers() {
		users= new Users();
		db=new Database();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		
		// ÄNDRA TILL RÄTT ROLL I generateMainMenu (se servletBase.java för roller) 
		out.print(generateMainMenu(1));
		out.print(generateSubMenu(1));
		String s = users.showUsers(db.getUsers());
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
		
		out.print(getPageOutro());
	
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
}
