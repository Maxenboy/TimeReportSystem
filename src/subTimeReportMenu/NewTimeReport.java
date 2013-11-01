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
	public static final int SUCCESS = 5;
	public static final int FAIL = 6;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		
		if(permission == PERMISSION_WITHOUT_ROLE) {
			out.print("<script> alert('Du har inte blivit tilldelad n\u00E5gon roll och kan d\u00E4rf\u00F6r inte skapa en ny tidrapport. Var v\u00E4nlig kontakta din projektledare') </script>");
			response.sendRedirect("ShowTimeReports");
		} else if(permission == PERMISSION_ADMIN) {
			out.print("<script> alert('Du \u00E4r systemadministrat\u00F6r och \u00E4r d\u00E4rf\u00F6r ej till\u00E5ten att skapa en ny tidrapport') </script>");
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
				out.print("500 internt fel");
			} else { 
				out.print(s);
			}
			out.print(getPageOutro());
			break;
		case ILLEGAL_CHAR:
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			out.print("<script> alert('Otillåten symbol. Anv\u00E4nd bara numeriska symboler.') </script>");
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internt fel");
			} else { 
				out.print(s);
			}
			out.print(getPageOutro());
			session.setAttribute("newReportState", FIRST);
			break;
		case NOT_ENOUGH_DATA:
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			out.print("<script> alert('Obligatoriska data saknas. Var v\u00E4nlig fyll i veckonummer och \u00E5tminstone en aktivitet') </script>");
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internt fel");
			} else { 
				out.print(s);
			}
			out.print(getPageOutro());
			session.setAttribute("newReportState", FIRST);
			break;
		case FAIL:
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			out.print("<script>alert('Tidrapporten kunde ej sparas. Kontrollera så att det ej finns en tidigare registrerad tidrapport för samma vecka') </script>");
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internt fel");
			} else { 
				out.print(s);
			}
			out.print(getPageOutro());
			session.setAttribute("newReportState", FIRST);
			break;
		case SUCCESS:
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			out.print("<script>alert('Tidrapport sparad') </script>");
			session.setAttribute("newReportState", FIRST);
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
			out.print("<script> alert('Otill\u00E5ten symbol. Anv\u00E4nd bara numeriska symboler.') </script>");
			session.setAttribute("newReportState", FIRST);
			doGet(request, response);
		} else {
			if(week.equals("")) {
				System.out.println("break 1");
				out.print("<script> alert('Obligatoriska data saknas. Var v\u00E4nlig fyll i veckonummer och \u00E5tminstone en aktivitet') </script>");
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
					session.setAttribute("newReportState", ILLEGAL_CHAR);
					doGet(request, response);
				} else if(!filledIn) {
					session.setAttribute("newReportState", NOT_ENOUGH_DATA);
					doGet(request, response);
				} else {
					//save in database
					if(trg.addNewTimeReport(timeReport, activities)) {
						session.setAttribute("newReportState", SUCCESS);
						doGet(request, response);
					} else {
						session.setAttribute("newReportState", FAIL);
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
