package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import database.Database;

@WebServlet("/ShowTimeReports")
public class ShowTimeReports extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1933766080948920247L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String s = trg.showTimeReports(1);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		String reportId = request.getParameter("reportId");
		String s = trg.showTimeReport(Integer.valueOf(reportId));
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		out.print(s);
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}

}
