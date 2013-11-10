package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RemoveTimeReport")
public class RemoveTimeReport extends TimeReportingMenu{
	
	private static final long serialVersionUID = 2619824858072375910L;
	TimeReportGenerator trg = new TimeReportGenerator(db);
	public static final int FIRST = 0;
	public static final int REMOVE_REPORT = 1;
	public static final int SHOW_REPORT = 2;
	public static final int ADMINISTRATOR = 3;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			out.print(getPageIntro());
			int permission = (Integer) session.getAttribute("user_permissions");
			int userId = (Integer) session.getAttribute("id");
			int projId = (Integer) session.getAttribute("project_group_id");
			String s = null;
			switch(permission) {
			case PERMISSION_WITHOUT_ROLE:
				out.print(getPageIntro());
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				out.println("<p style='color: red;'>Du \u00E4r inte tilldelad n\u00E5gon roll i projektet och har d\u00E4rf\u00F6r inte tillg\u00E5ng till den h\u00E4r funktionen. Kontakta din projektledare.</p>");
				out.print(getPageOutro());
				break;
			case PERMISSION_ADMIN:
				s = trg.showAllTimeReports(projId, TimeReportGenerator.REMOVE_PRJ);
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				if(s != null)
					out.print(s);
				else 
					out.print("Det finns inga projektgrupper att visa");
				out.print(getPageOutro());
				session.setAttribute("removeReportState", ADMINISTRATOR);
				break;
			case PERMISSION_PROJ_LEADER:
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				s = trg.showAllTimeReports(projId, TimeReportGenerator.REMOVE_PRJ_REPORT);
				if(s == null) {
					out.print("<script> alert('Det finns inga tidrapporter att visa</script>");
				} else {
					out.print(s);
					session.setAttribute("removeReportState", SHOW_REPORT);
				}
				out.print(getPageOutro());
				break;
			case PERMISSION_OTHER_USERS:
				out.print(generateMainMenu(permission, request));
				out.print(generateSubMenu(permission));
				s = trg.showAllTimeReports(userId,TimeReportGenerator.REMOVE_USER_REPORT);
				if(s == null) {
					out.print("Det finns inga tidrapporter att visa");
				} else {
					out.print(s);
					session.setAttribute("removeReportState", SHOW_REPORT);
				}
				out.print(getPageOutro());
				break;
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			int state = (Integer) session.getAttribute("removeReportState");
			String reportId;
			String s;
			System.out.println("state: " + state);
			switch(state) {
			case ADMINISTRATOR:
				System.out.println("hello");
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
						s = trg.showAllTimeReports(Integer.valueOf(prjGroup), TimeReportGenerator.REMOVE_PRJ_REPORT);
						if(s == null)
							out.print("Det finns inga tidrapporter att visa");
						else 
							out.print(s);
						out.print(getPageOutro());
						session.setAttribute("removeReportState", SHOW_REPORT);
					} else {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						out.print("Internt fel - inga tidrapporter kunde visas");
						out.print(getPageOutro());
						session.setAttribute("removeReportState", FIRST);
					}
				}
				break;
			case FIRST:
				doGet(request, response);
				break;
			case SHOW_REPORT:
				reportId = request.getParameter("reportId");
				if(reportId != null) {
					s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.REMOVE_REPORT);
					out.print(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					if(s == null) {
						out.print("Det finns inga tidrapporter att visa");
					} else { 
						out.print(s);
						session.setAttribute("removeReportState", REMOVE_REPORT);
					}
					out.print(getPageOutro());
				} else {
					session.setAttribute("removeReportState", FIRST);
					doGet(request,response);
				}
				break;
			case REMOVE_REPORT:
				reportId = request.getParameter("reportId");
				if(trg.removeTimeReport(reportId)) {
					out.print("<script>alert('Tidrapport borttagen: " +  reportId + "')</script>");
				} else {
					out.print("<script>alert('Internt fel - Tidrapporten kunde ej raderas')</script>");
				}
				session.setAttribute("removeReportState", FIRST);
				doGet(request, response);
				break;
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
}
