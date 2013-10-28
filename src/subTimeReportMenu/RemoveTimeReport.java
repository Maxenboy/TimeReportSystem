package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/RemoveTimeReport")
public class RemoveTimeReport extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619824858072375910L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	private static final int FIRST = 0;
	private static final int DO_REMOVAL = 1;
	private static final int REMOVE_REPORT = 3;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("doGet");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());

		/**
		 * This should be changed so it depends if user is proj-leader or regular
		 * user.
		 */
		String s = trg.showAllTimeReports(1, TimeReportGenerator.REMOVE_USER_REPORT);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
		session.setAttribute("state", REMOVE_REPORT);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("doPost");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		int state = FIRST;
		String reportId;
//		session.invalidate();
		if(!session.isNew()) 
			state = (Integer) session.getAttribute("state");
		
		switch(state) {
		case FIRST:
			doGet(request, response);
			break;
		case REMOVE_REPORT:
			reportId = request.getParameter("reportId");
			if(reportId != null) {
				String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.REMOVE_REPORT);
				out.print(getPageIntro());
				out.print(s);
				session.setAttribute("state", DO_REMOVAL);
			} else {
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
			session.setAttribute("state", FIRST);
			doGet(request, response);
			break;
		}
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}

}
