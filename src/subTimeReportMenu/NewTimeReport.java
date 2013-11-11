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
import database.TimeReport;
/**
 * Den här klassen är WebServlet för sidan som visas då en användare vill skapa en ny tidrapport.
 * Sidan ska ej vara tillgänglig för administratörer.
 * @author martin
 *
 */
@WebServlet("/NewTimeReport")
public class NewTimeReport extends TimeReportingMenu{
	private static final long serialVersionUID = 6091270182349510225L;
	public static final int FIRST = 1;
	TimeReportGenerator trg = new TimeReportGenerator(db);
	/**
	 * Denna metod används när användaren besöker sidan första gången. 
	 * Den ritar upp inmatningsfält som användaren kan mata med information om vad den 
	 * har jobbat med under en specifik vecka
	 * @param HttpServletRequest request, HttpServletResponse response
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			
			if(permission == PERMISSION_WITHOUT_ROLE) {
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.println("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och kan d\u00E4rf\u00F6r inte skapa n\u00E5gon tidrapport. Kontakta din projektledare.</p>");
				out.print(getPageOutro());
			} else if(permission == PERMISSION_ADMIN) {
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.println("<p style='color: red;'>Du \u00E4r systemadministrat\u00F6r och \u00E4r d\u00E4rf\u00F6r ej till\u00E5ten att skapa en ny tidrapport.</p>");
				out.print(getPageOutro());
			} else {
				String s;
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				s = trg.showNewTimeForm();
				if(s == null) {
					out.print("<p style='color: red;'>500 internt fel</p>");
				} else { 
					out.print(s);
				}
				out.print(getPageOutro());
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
	/**
	 * Denna metod samlar in de data som användaren matade in i inmatningsfälten i doGet. Skickar dessa data till databasen
	 * genom TimeReportGenerator, och på så sätt skapas en ny tidrapport.
	 * @param HttpServletRequest request, HttpServletResponse response
	 */
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
							out.print("<script>alert('Tidrapporten kunde ej sparas. Kontrollera s\u00E5 att det ej finns en tidigare registrerad tidrapport f\u00F6r samma vecka') </script>");
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
