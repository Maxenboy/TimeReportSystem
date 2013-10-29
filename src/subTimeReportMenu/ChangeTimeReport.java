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

@WebServlet("/ChangeTimeReport")
public class ChangeTimeReport extends TimeReportingMenu{
	
	private static final long serialVersionUID = 162207957025267806L;
	private TimeReportGenerator trg = new TimeReportGenerator(new Database());
	private static final int FIRST = 0;
	private static final int SHOW_REPORT = 1;
	private static final int CHANGE_REPORT = 2;
	private static final int NOT_ENOUGH_DATA = 3;
	private static final int ILLEGAL_CHAR = 4;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(generateMainMenu(permission));
		out.print(generateSubMenu(permission));
		int userId = (Integer) session.getAttribute("id");
		int projId = (Integer) session.getAttribute("project_group_id");
		int state = 0;
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("ChangeReportState");
		}
		String s;
		switch(state) {
		case FIRST:
			switch(permission) {
			case PERMISSION_ADMIN:
			case PERMISSION_PROJ_LEADER:
				s = trg.showAllTimeReports(projId, TimeReportGenerator.CHANGE_PRJ_REPORT);
				if(s == null)
					out.print("<p> Nothing to show </p>");
				else 
					out.print(s);
				session.setAttribute("changeReportState", SHOW_REPORT);
				break;
			case PERMISSION_OTHER_USERS:
				s = trg.showAllTimeReports(userId, TimeReportGenerator.CHANGE_USER_REPORT);
				if(s == null)
					out.print("<p> Nothing to show </p>");
				else 
					out.print(s);
				session.setAttribute("changeReportState", SHOW_REPORT);
				break;
			}
			
		case NOT_ENOUGH_DATA:
			session.setAttribute("changeReportState", FIRST);
			out.print("<script> alert('Mandatory data has not been filled in. Please fill in week nr. and at least one activity') </script>");
			doGet(request, response);
			break;
		case ILLEGAL_CHAR:
			session.setAttribute("changeReportState", FIRST);
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
		int state = 0;
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("changeReportState");
		}
		String s;
		String reportId;
		switch(state) {
		case SHOW_REPORT:
			reportId = request.getParameter("reportId");
			if(reportId != null) {
				s = trg.showChangeTimeReport(Integer.valueOf(reportId));
				if(s == null) {
					out.print("<script>alert('Cannot change signed report!') </script>");
					session.setAttribute("state", FIRST);
					doGet(request, response);
				} else {
					out.print(s);
					session.setAttribute("changeReportState", CHANGE_REPORT);
					doGet(request, response);
				}
			}
			break;
		case CHANGE_REPORT:
			String week = request.getParameter("week");
			if(week.equals("")) {
				session.setAttribute("changeReportState", NOT_ENOUGH_DATA);
				doGet(request,response);
			} else {
				if(!isNumeric(week)) {
					session.setAttribute("changeReportState", ILLEGAL_CHAR);
					doGet(request, response);
				}
				reportId = request.getParameter("reportId");
				TimeReport timeReport = new TimeReport(Integer.valueOf(reportId),Integer.valueOf(week),false, 1, 1);
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
							session.setAttribute("changeReportState", ILLEGAL_CHAR);
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
					session.setAttribute("changeReportState", NOT_ENOUGH_DATA);
					doGet(request, response);
				} else {
					//update in database
					if(trg.changeTimeReport(timeReport, activities)) {
						out.print("<script>alert('Successfully updated time report.') </script>");
						session.setAttribute("changeReportState", FIRST);
						doGet(request, response);
					} else {
						out.print("<script>alert('Internal error - could not update time report.') </script>");
						session.setAttribute("changeReportState", FIRST);
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
