package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;

@WebServlet("/ChangeTimeReport")
public class ChangeTimeReport extends HttpServlet{
	
	private static final long serialVersionUID = 162207957025267806L;
	private TimeReportGenerator trg = new TimeReportGenerator(new Database());
	private static final int FIRST = 0;
	private static final int SHOW_REPORT = 1;
	private static final int CHANGE_REPORT = 2;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int state = 0;
//		session.invalidate();
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("state");
		}
		String s;
		switch(state) {
		case FIRST:
			s = trg.showAllTimeReports(1, TimeReportGenerator.CHANGE_USER_REPORT);
			if(s == null)
				out.print("<p> Nothing to show </p>");
			else 
				out.print(s);
			session.setAttribute("state", SHOW_REPORT);
			break;
		}
		
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		int state = 0;
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("state");
		}
		String s;
		switch(state) {
		case SHOW_REPORT:
			String reportId = request.getParameter("reportId");
			if(reportId != null) {
				s = trg.showChangeTimeReport(Integer.valueOf(reportId));
				if(s == null) {
					out.print("<script>alert('Cannot change signed report!') </script>");
					session.setAttribute("state", FIRST);
					doGet(request, response);
				} else {
					out.print(s);
					session.setAttribute("state", CHANGE_REPORT);
					doGet(request, response);
				}
			}
			break;
		case CHANGE_REPORT:
			
		}
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}
}
