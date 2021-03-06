package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Denna WebServlet används för att visa tidrapporter som ligger inmatade i systemet.
 * @author martin
 *
 */
@WebServlet("/ShowTimeReports")
public class ShowTimeReports extends TimeReportingMenu {
	private static final long serialVersionUID = -1933766080948920247L;
	public static final int FIRST = 0;
	public static final int ADMINISTRATOR = 1;
	TimeReportGenerator trg = new TimeReportGenerator(db);
/**
 * Denna metod används första gången användaren besöker sidan. Visar antingen en lista över projektgrupper eller 
 * tidrapporter
 * @param HttpServletRequest request, HttpServletResponse response
 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			out.print(getPageIntro());
			out.print(generateMainMenu(permission, request));
			out.print(generateSubMenu(permission));
			
			int userId = (Integer) session.getAttribute("id");
			int projId = (Integer) session.getAttribute("project_group_id");
			String s = null;
			switch(permission) {
			case PERMISSION_ADMIN:
				s = trg.showAllTimeReports(projId, TimeReportGenerator.SHOW_PRJ);
				if(s != null)
					out.print(s);
				else 
					out.print("Det finns inga projektgrupper att visa");
				session.setAttribute("showReportState", ADMINISTRATOR);
				break;
			case PERMISSION_PROJ_LEADER:
				s = trg.showAllTimeReports(projId, TimeReportGenerator.SHOW_ALL_FOR_PRJ);
				if(s == null)
					out.print("Det finns inga tidrapporter att visa");
				else 
					out.print(s);
				out.print(getPageOutro());
				session.setAttribute("showReportState", FIRST);
				break;
			case PERMISSION_OTHER_USERS:
				s = trg.showAllTimeReports(userId,TimeReportGenerator.SHOW_USER_REPORT);
				if(s == null)
					out.print("<script> alert('Det finns inga tidrapporter att visa')");
				else 
					out.print(s);
				out.print(getPageOutro());
				session.setAttribute("showReportState", FIRST);
				break;
			case PERMISSION_WITHOUT_ROLE:
				out.println("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och har d\u00E4rf\u00F6r inte tillg\u00E5ng till den h\u00E4r funktionen. Kontakta din projektledare.</p>");
				out.print(getPageOutro());
				break;
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
/**
 * Denna metod visar en vald tidrapport, den används även för att kontakta TimeReportGenerator för att ta bort tidrapporten.
 * @param HttpServletRequest request, HttpServletResponse response
 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			int state = (Integer) session.getAttribute("showReportState");
			switch(state) {
			case ADMINISTRATOR:
				String prjGroup = request.getParameter("prjGroup");
				if(prjGroup != null) {
					out.print(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					out.print(getPageIntro());
					String s = trg.showAllTimeReports(Integer.valueOf(prjGroup), TimeReportGenerator.SHOW_ALL_FOR_PRJ);
					if(s == null)
						out.print("Det finns inga tidrapporter att visa");
					else 
						out.print(s);
					out.print(getPageOutro());
					session.setAttribute("showReportState", FIRST);
				} else {
					out.print(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					out.print(getPageIntro());
					out.print("Internt fel - inga tidrapporter kunde visas");
					out.print(getPageOutro());
					session.setAttribute("showReportState", FIRST);
				}
				break;
			case FIRST:
				String reportId = request.getParameter("reportId");
				if(reportId != null) {
					out.print(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					out.print(getPageIntro());
					String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_USER_REPORT);
					out.print(s);
					out.print(getPageOutro());
					session.setAttribute("showReportState", FIRST);
				} else {
					doGet(request,response);
					session.setAttribute("showReportState", FIRST);
				}
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
}
