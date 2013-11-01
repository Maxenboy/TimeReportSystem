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
	public static final int FIRST = 1;
	public static final int NOT_ENOUGH_DATA = 3;
	public static final int ILLEGAL_CHAR = 4;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		
		if(permission == PERMISSION_WITHOUT_ROLE) {
			out.print("<script> alert('You are currently not assigned to any role in the project" + 
		" and are therefore unable to create a new time report. Please contact your project leader')");
			response.sendRedirect("ShowTimeReports");
		} else if(permission == PERMISSION_ADMIN) {
			out.print("<script> alert('You are a system administrator, and are not allowed to create time reports')");
			response.sendRedirect("ShowTimeReports");
		}
		int state = (Integer) session.getAttribute("newReportState");
		String s;
		switch(state) {
		case FIRST:
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internal error");
			} else { 
				out.print(s);
			}
			out.print(getPageOutro());
			break;
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		String week = request.getParameter("week");
		int userId = (Integer) session.getAttribute("id");
		int projId = (Integer) session.getAttribute("project_group_id");
		if(!isNumeric(week) && !week.equals("")) {
			out.print("<script> alert('Illegal character. Only numerical chararcters are allowed') </script>");
			session.setAttribute("newReportState", FIRST);
			doGet(request, response);
		} else {
			if(week.equals("")) {
				System.out.println("break 1");
				out.print("<script> alert('Mandatory data has not been filled in. Please fill in week nr. and at least one activity') </script>");
				session.setAttribute("newReportState", FIRST);
				doGet(request,response);
			} else {
				TimeReport timeReport = new TimeReport(Integer.valueOf(week), userId, projId);
				String[] fields = request.getParameter("FormFields").split(",");
				ArrayList<Activity> activities = new ArrayList<Activity>();
				boolean filledIn = false;
				boolean nonNumeric = false;
				int activityNbr = 10;
				for(int i = 0; i < fields.length; i++) {
					String field = fields[i];
					String time = request.getParameter(field);
					if(i % 4 == 0)
						activityNbr++;
					if(!isNumeric(time) && !time.equals("")) {
						nonNumeric = true;
						break;
					} else {
						if(!time.equals("")) {
							filledIn = true;
							if(i < 36) {
								activities.add(new Activity(activityNbr, Activity.mapIntToActivityType(i % 4), Integer.valueOf(time), timeReport.getId()));
							} else {
								activities.add(new Activity(Integer.valueOf(field.trim()), Activity.ACTIVITY_TYPE_DEVELOPMENT, Integer.valueOf(time), timeReport.getId()));
							}
						}
					}
				}
				if(nonNumeric) {
					out.print("<script> alert('Illegal character, only numerical values are allowed') </script>");
					session.setAttribute("newReportState", FIRST);
					doGet(request, response);
				} else if(!filledIn) {
					System.out.println("break4");
					out.print("<script> alert('Mandatory data has not been filled in. Please fill in week nr. and at least one activity') </script>");
					session.setAttribute("newReportState", FIRST);
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
