package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/SignTimeReports")
/**
 * This class is a servlet page where project-leaders can sign TimeReports.
 * This page is only available if the user is logged in as project-leader/admin.
 * @author martin
 *
 */
public class SignTimeReports extends TimeReportMenu{
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		//TODO: Change the 1 into a variable!!
		String s = trg.showTimeReports(1);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	/**
	 * Only for development purposes.
	 * @return
	 */
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}
}
