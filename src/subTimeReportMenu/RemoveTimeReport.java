package subTimeReportMenu;

import gui.TimeReportingMenu;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import database.Database;

@WebServlet("/RemoveTimeReport")
public class RemoveTimeReport extends TimeReportingMenu{
	
	private static final long serialVersionUID = 2619824858072375910L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	private static final int FIRST = 0;
	private static final int DO_REMOVAL = 1;
	private static final int REMOVE_REPORT = 3;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(generateMainMenu(permission));
		out.print(generateSubMenu(permission));
		
		int userId = (Integer) session.getAttribute("id");
		int projId = (Integer) session.getAttribute("project_group_id");
		String s = null;
		switch(permission) {
		case PERMISSION_ADMIN:
		case PERMISSION_PROJ_LEADER:
			s = trg.showAllTimeReports(projId, TimeReportGenerator.REMOVE_PRJ_REPORT);
			break;
		case PERMISSION_OTHER_USERS:
			s = trg.showAllTimeReports(userId,TimeReportGenerator.REMOVE_USER_REPORT);
			break;
		}
		if(s == null) {
			out.print("<script> alert('There are no time reports in the system.</script>");
		} else {
			out.print(s);
			session.setAttribute("removeReportState", REMOVE_REPORT);
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
		int state = FIRST;
		String reportId;
		if(!session.isNew()) 
			state = (Integer) session.getAttribute("removeReportState");
		
		switch(state) {
		case FIRST:
			doGet(request, response);
			break;
		case REMOVE_REPORT:
			reportId = request.getParameter("reportId");
			if(reportId != null) {
				String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.REMOVE_REPORT);
				out.print(s);
				session.setAttribute("removeReportState", DO_REMOVAL);
			} else {
				session.setAttribute("removeReportState", FIRST);
				doGet(request,response);
			}
			break;
		case DO_REMOVAL:
			reportId = request.getParameter("reportId");
			if(trg.removeTimeReport(reportId)) {
				out.print("<script>alert('Removed report " +  reportId + "')</script>");
			} else {
				out.print("<script>alert('Internal error - could not remove report')</script>");
			}
			session.setAttribute("removeReportState", FIRST);
			doGet(request, response);
			break;
		}
		out.print(getPageOutro());
	}
}
