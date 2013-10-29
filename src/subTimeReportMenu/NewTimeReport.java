package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Activity;
import database.Database;
import database.TimeReport;

@WebServlet("/NewTimeReport")
public class NewTimeReport extends TimeReportingMenu{
	private static final long serialVersionUID = 6091270182349510225L;
	private static final int FIRST = 1;
	private static final int NOT_ENOUGH_DATA = 3;
	private static final int ILLEGAL_CHAR = 4;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(generateMainMenu(permission));
		out.print(generateSubMenu(permission));
		
		if(permission == PERMISSION_WITHOUT_ROLE) {
			out.print("<script> alert('You are currently not assigned to any role in the project" + 
		" and are therefore unable to create a new time report. Please contact your project leader')");
			response.sendRedirect("ShowTimeReports");
		}
		int state = 0;
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("newReportState");
		}
		String s;
		switch(state) {
		case FIRST:
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internal error");
			} else { 
				out.print(s);
			}
			break;
		case NOT_ENOUGH_DATA:
			session.setAttribute("newReportState", FIRST);
			out.print("<script> alert('Mandatory data has not been filled in. Please fill in week nr. and at least one activity') </script>");
			doGet(request, response);
			break;
		case ILLEGAL_CHAR:
			session.setAttribute("newReportState", FIRST);
			out.print("<script> alert('Illegal character. Only numerical chararcters are allowed') </script>");
			doGet(request, response);
			break;
		}
		out.print(getPageOutro());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(generateMainMenu(permission));
		out.print(generateSubMenu(permission));
		String week = request.getParameter("week");
		if(week.equals("")) {
			session.setAttribute("newReportState", NOT_ENOUGH_DATA);
			doGet(request,response);
		} else {
			if(!isNumeric(week)) {
				session.setAttribute("newReportState", ILLEGAL_CHAR);
				doGet(request, response);
			}
			TimeReport timeReport = new TimeReport(Integer.valueOf(week), 1, 1);
			String[] fields = request.getParameter("FormFields").split(",");
			ArrayList<Activity> activities = new ArrayList<Activity>();
			boolean filledIn = false;
			int activityNbr = 10;
			for(int i = 0; i < fields.length; i++) {
				String field = fields[i];
				String time = request.getParameter(field);
				if(i % 4 == 0)
					activityNbr++;
				if(!time.equals("")) {
					if(!isNumeric(time)) {
						session.setAttribute("newReportState", ILLEGAL_CHAR);
						doGet(request, response);
					}
					filledIn = true;
					if(i < 36) {
						activities.add(new Activity(activityNbr, Activity.mapIntToActivityType(i % 4), Integer.valueOf(time), timeReport.getId()));
					} else {
						activities.add(new Activity(Integer.valueOf(field.trim()), Activity.ACTIVITY_TYPE_DEVELOPMENT, Integer.valueOf(time), timeReport.getId()));
					}
				}
			}
			if(!filledIn) {
				session.setAttribute("newReportState", NOT_ENOUGH_DATA);
				doGet(request, response);
			} else {
				//save in database
				if(trg.addNewTimeReport(timeReport, activities)) {
					out.print("<script>alert('Successfully saved time report.') </script>");
					session.setAttribute("newReportState", FIRST);
					doGet(request, response);
				} else {
					out.print("<script>alert('Internal error - could not save time report.') </script>");
					session.setAttribute("newReportState", FIRST);
					doGet(request, response);
				}
			}
		}
	}
	
	private boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
