package subTimeReportMenu;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;
import database.TimeReport;


@WebServlet("/SignTimeReports")
public class SignTimeReports extends HttpServlet {
	private static int UNSUCCESSFUL_SIGNING = 1;
	private static int NO_REPORTS_ENTERED = 2;
	private static final long serialVersionUID = -4213845458306512233L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		
		//TODO: Change the 1 into a variable!!
		String s = trg.showAllTimeReports(1, TimeReportGenerator.SHOW_UNSIGNED);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String reportId = request.getParameter("reportId");
		if(reportId != null) {
			String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_UNSIGNED);
			out.print(getPageIntro());
			out.print(s);
		} else {
			doGet(request,response);
		}
	}
	
	/**
	 * Only for development purposes.
	 * @return
	 */
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}
}
