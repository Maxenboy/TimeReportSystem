package subTimeReportMenu;

import gui.TimeReportingMenu;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import database.Database;

@WebServlet("/ShowTimeReports")
public class ShowTimeReports extends TimeReportingMenu {
	private static final long serialVersionUID = -1933766080948920247L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
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
			System.out.println(projId);
			s = trg.showAllTimeReports(projId, TimeReportGenerator.SHOW_ALL);
			break;
		case PERMISSION_OTHER_USERS:
		case PERMISSION_WITHOUT_ROLE:
			s = trg.showAllTimeReports(userId,TimeReportGenerator.SHOW_USER_REPORT);
			break;
		}
		if(s == null)
			out.print("<script> alert('Det finns inga tidrapporter att visa')");
		else 
			out.print(s);
		out.print(getPageOutro());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int permission = (Integer) session.getAttribute("user_permissions");
		out.print(generateMainMenu(permission));
		out.print(generateSubMenu(permission));
		String reportId = request.getParameter("reportId");
		if(reportId != null) {
			String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_USER_REPORT);
			out.print(getPageIntro());
			out.print(s);
		} else {
			doGet(request,response);
		}
		out.print(getPageOutro());
	}
}
