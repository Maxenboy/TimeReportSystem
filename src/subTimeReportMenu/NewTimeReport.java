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
			out.print("<script> alert('Du har inte blivit tilldelad någon roll och kan därför inte skapa en ny tidrapport. Var vänlig kontakta din projektledare')");
			response.sendRedirect("ShowTimeReports");
		} else if(permission == PERMISSION_ADMIN) {
			out.print("<script> alert('Du är systemadministratör och är därför ej tillåten att skapa en ny tidrapport')");
			response.sendRedirect("ShowTimeReports");
		}
		int state = (Integer) session.getAttribute("newReportState");
		String s;
		switch(state) {
		case FIRST:
			out.print(generateMainMenu(permission));
			out.print(generateSubMenu(permission));
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internt fel");
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
			out.print("<script> alert('Otillåten symbol. Använd bara numeriska symboler.') </script>");
			session.setAttribute("newReportState", FIRST);
			doGet(request, response);
		} else {
			if(week.equals("")) {
				System.out.println("break 1");
				out.print("<script> alert('Obligatoriska data saknas. Var vänlig fyll i veckonummer och åtminstone en aktivitet') </script>");
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
					out.print("<script> alert('Otillåten symbol. Använd bara numeriska symboler.') </script>");
					session.setAttribute("newReportState", FIRST);
					doGet(request, response);
				} else if(!filledIn) {
					System.out.println("break4");
					out.print("<script> alert('Obligatoriska data saknas. Var vänlig fyll i veckonummer och åtminstone en aktivitet') </script>");
					session.setAttribute("newReportState", FIRST);
					doGet(request, response);
				} else {
					//save in database
					if(trg.addNewTimeReport(timeReport, activities)) {
						out.print("<script>alert('Tidrapport uppdaterad') </script>");
						session.setAttribute("newReportState", FIRST);
						doGet(request, response);
					} else {
						out.print("<script>alert('Internt fel - tidrapporten kunde ej uppdateras) </script>");
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
