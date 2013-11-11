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
 * Den här klassen är WebServlet för sidan som användaren besöker när den vill ändra i tidrapporter.
 * @author martin
 *
 */
@WebServlet("/ChangeTimeReport")
public class ChangeTimeReport extends TimeReportingMenu{
	
	private static final long serialVersionUID = 162207957025267806L;
	private TimeReportGenerator trg = new TimeReportGenerator(db);
	public static final int FIRST = 0;
	public static final int SHOW_REPORT = 1;
	public static final int CHANGE_REPORT = 2;
	public static final int ADMINISTRATOR = 3;
/**
 * Den här metoden används första gången då användaren besöker sidan. 
 * Kontaktar TimeReportGenerator för att hämta data som visas på skärm 
 * @param HttpServletRequest request, HttpServletResponse response
 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int permission = (Integer) session.getAttribute("user_permissions");
			
			int userId = (Integer) session.getAttribute("id");
			int projId = (Integer) session.getAttribute("project_group_id");
			String s;
			switch(permission) {
			case PERMISSION_WITHOUT_ROLE:
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.println("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och har d\u00E4rf\u00F6r inte tillg\u00E5ng till den h\u00E4r funktionen. Kontakta din projektledare.</p>");
				out.print(getPageOutro());
				break;
			case PERMISSION_ADMIN:
				s = trg.showAllTimeReports(projId, TimeReportGenerator.CHANGE_PRJ);
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				if(s != null)
					out.print(s);
				else 
					out.print("Det finns inga projektgrupper att visa");
				out.print(getPageOutro());
				session.setAttribute("changeReportState", ADMINISTRATOR);
				break;
			case PERMISSION_PROJ_LEADER:
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				s = trg.showAllTimeReports(projId, TimeReportGenerator.CHANGE_PRJ_REPORT);
				if(s == null)
					out.print("<p> Inga tidrapporter att visa </p>");
				else 
					out.print(s);
				out.print(getPageOutro());
				session.setAttribute("changeReportState", SHOW_REPORT);
				break;
			case PERMISSION_OTHER_USERS:
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				s = trg.showAllTimeReports(userId, TimeReportGenerator.CHANGE_USER_REPORT);
				if(s == null)
					out.print("<p> Inga tidrapporter att visa </p>");
				else 
					out.print(s);
				out.print(getPageOutro());
				session.setAttribute("changeReportState", SHOW_REPORT);
				break;
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
/**
 * Den här metoden används då användaren börjar ändra i en tidrapport, eller vill ha tillgång till en speciell 
 * projektgrupps tidrapporter.
 * @param HttpServletRequest request, HttpServletResponse response
 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			int state = (Integer) session.getAttribute("changeReportState");
			String s;
			String reportId;
			switch(state) {
			case ADMINISTRATOR:
				if(permission != PERMISSION_ADMIN) {
					out.print("<script> alert('Otill\u00E5ten handling. Du saknar administrat\u00F6rsr\u00E4ttigheter') </script>");
					session.setAttribute("changeReportState", FIRST);
					doGet(request, response);
				} else {
					String prjGroup = request.getParameter("prjGroup");
					if(prjGroup != null) {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						out.print(getPageIntro());
						s = trg.showAllTimeReports(Integer.valueOf(prjGroup), TimeReportGenerator.CHANGE_PRJ_REPORT);
						if(s == null)
							out.print("Det finns inga tidrapporter att visa");
						else 
							out.print(s);
						out.print(getPageOutro());
						session.setAttribute("changeReportState", SHOW_REPORT);
					} else {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						out.print(getPageIntro());
						out.print("Internt fel - inga tidrapporter kunde visas");
						out.print(getPageOutro());
						session.setAttribute("changeReportState", FIRST);
					}
				}
				break;
			case SHOW_REPORT:
				reportId = request.getParameter("reportId");
				if(reportId != null) {
					s = trg.showChangeTimeReport(Integer.valueOf(reportId));
					if(s == null) {
						out.print("<script>alert('Du kan inte \u00E4ndra i en signerad tidrapport') </script>");
						session.setAttribute("changeReportstate", FIRST);
						doGet(request, response);
					} else {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						out.print(s);
						session.setAttribute("changeReportState", CHANGE_REPORT);
						out.print(getPageOutro());
					}
				}
				break;
			case CHANGE_REPORT:
				String week = request.getParameter("week");
				if(!isNumeric(week) && !week.equals("")) {
					out.print("<script> alert('Otill\u00E5ten symbol. Anv\u00E4nd bara numeriska symboler.') </script>");
					session.setAttribute("changeReportState", FIRST);
					doGet(request, response);
				} else if(week.equals("")) {
					out.print("<script> alert('Obligatoriska data saknas. Var v\u00E4nlig fyll i veckonummer och \u00E5tminstone en aktivitet') </script>");
					session.setAttribute("changeReportState", FIRST);
					doGet(request,response);
				} else {
					reportId = request.getParameter("reportId");
					int userId = (Integer) session.getAttribute("id");
					int projId = (Integer) session.getAttribute("project_group_id");
					TimeReport timeReport = new TimeReport(Integer.valueOf(reportId),Integer.valueOf(week),false, userId, projId);
					String[] fields = request.getParameter("FormFields").split(",");
					ArrayList<Activity> activities = new ArrayList<Activity>();
					boolean filledIn = false;
					boolean nonNumerical = false;
					int activityNbr = 10;
					for(int i = 0; i < fields.length; i++) {
						String field = fields[i];
						String time = request.getParameter(field);
						if(i % 4 == 0)
							activityNbr++;
						if(!isNumeric(time) && !time.equals("")) {
							nonNumerical = true;
							break;
						}
						if(!time.equals("")) {
							filledIn = true;
							if(i < 36) {
								activities.add(new Activity(activityNbr, Activity.mapIntToActivityType(i % 4), Integer.valueOf(time), timeReport.getId()));
							} else {
								activities.add(new Activity(Integer.valueOf(field.trim()), Activity.ACTIVITY_TYPE_DEVELOPMENT, Integer.valueOf(time), timeReport.getId()));
							}
						}
					}
					if(nonNumerical) {
						out.print("<script> alert('Otill\u00E5ten symbol. Anv\u00E4nd bara numeriska symboler.') </script>");
						session.setAttribute("changeReportState", FIRST);
						doGet(request, response);
					} else if(!filledIn) {
						out.print("<script> alert('Obligatoriska data saknas. Var v\u00E4nlig fyll i veckonummer och \u00E5tminstone en aktivitet') </script>");
						session.setAttribute("changeReportState", FIRST);
						doGet(request, response);
					} else {
						//update in database
						if(trg.changeTimeReport(timeReport, activities)) {
							out.print("<script>alert('Tidrapport uppdaterad') </script>");
							session.setAttribute("changeReportState", FIRST);
							doGet(request, response);
						} else {
							out.print("<script>alert('Tidrapporten kunde ej uppdateras. Kontrollera att det ej redan finns en tidrapport med den veckan.') </script>");
							session.setAttribute("changeReportState", FIRST);
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
