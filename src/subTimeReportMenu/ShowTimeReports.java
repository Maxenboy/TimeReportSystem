package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import base.servletBase;
import database.Database;

@WebServlet("/ShowTimeReports")
public class ShowTimeReports extends TimeReportingMenu {
	private static final long serialVersionUID = -1933766080948920247L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);

		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		
		// ÄNDRA TILL RÄTT ROLL I generateMainMenu (se servletBase.java för roller) 
		out.print(generateMainMenu(1));
		out.print(generateSubMenu(1));
		
		/**
		 * Change 1 into variable. The second in-parameter should depend 
		 * on user role
		 */
		String s = trg.showAllTimeReports(1,TimeReportGenerator.SHOW_USER_REPORT);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		String reportId = request.getParameter("reportId");
		if(reportId != null) {
			PrintWriter out = response.getWriter();
			String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_USER_REPORT);
			out.print(getPageIntro());
			out.print(s);
		} else {
			doGet(request,response);
		}
	}
}
