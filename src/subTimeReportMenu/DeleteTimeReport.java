package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

public class DeleteTimeReport extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619824858072375910L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	private static int UNSUCCESSFUL_DELETION = 1;
	private static int NO_REPORTS_ENTERED = 2;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		
		if(!session.isNew()) {
		Object state = session.getAttribute("delete");
			if(state != null) {
				int delete = (Integer) state;
				if(delete == UNSUCCESSFUL_DELETION) {
					out.print("<script>window.alert('Deletion of time Reports was unsuccessful')</script>");
				} else if(delete == NO_REPORTS_ENTERED) {
					out.print("<script>window.alert('No reports entered')</script>");
				}
			}
		}
		/**
		 * This should be changed so it depends if user is proj-leader or regular
		 * user.
		 */
		String s = null;
		//trg.showUnsignedTimeReports(1);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String[] reportIds = request.getParameterValues("reportIds");
		if(reportIds != null) {
			System.out.println("Reports");
			//TODO: delete report-functionality
			String s = null;
			if(s != null) {
				out.print(s);
				session.invalidate();
			} else {
				session.setAttribute("sign", UNSUCCESSFUL_DELETION);
				doGet(request,response);
			}
		} else {
			session.setAttribute("sign", NO_REPORTS_ENTERED);
			doGet(request,response);
		}
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}

}
