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
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			
			if(permission == PERMISSION_WITHOUT_ROLE) {
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.print("<script> alert('Du har inte blivit tilldelad n\u00E5gon roll och kan d\u00E4rf\u00F6r inte skapa en ny tidrapport. Var v\u00E4nlig kontakta din projektledare') </script>");
				out.print(getPageOutro());
			} else if(permission == PERMISSION_ADMIN) {
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.print("<script> alert('Du \u00E4r systemadministrat\u00F6r och \u00E4r d\u00E4rf\u00F6r ej till\u00E5ten att skapa en ny tidrapport') </script>");
				out.print(getPageOutro());
			} else {
				int state = (Integer) session.getAttribute("newReportState");
				String s;
				switch(state) {
				case FIRST:
					out.print(getPageIntro());
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
				}
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
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
						out.print("<script> alert('Otill\u00E5ten symbol. Anv\u00E4nd bara numeriska symboler.') </script>");
						session.setAttribute("newReportState", FIRST);
						doGet(request, response);
					} else if(!filledIn) {
						out.print("<script> alert('Obligatoriska data saknas. Var v\u00E4nlig fyll i veckonummer och \u00E5tminstone en aktivitet') </script>");
						session.setAttribute("newReportState", FIRST);
						doGet(request, response);
					} else {
						//save in database
						if(trg.addNewTimeReport(timeReport, activities)) {
							out.print("<script>alert('Tidrapport sparad') </script>");
							session.setAttribute("newReportState", FIRST);
							doGet(request, response);
						} else {
							out.print("<script>alert('Tidrapporten kunde ej sparas. Kontrollera så att det ej finns en tidigare registrerad tidrapport för samma vecka') </script>");
							session.setAttribute("newReportState", FIRST);
							doGet(request, response);
						}
					}
				}
			}
		} else {
			response.sendRedirect("LogIn");
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
