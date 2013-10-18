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
	private static int SIGN = 1;
	private static int UNSIGN = 2;
	private static final long serialVersionUID = -4213845458306512233L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		StringBuilder sb = new StringBuilder();
		if(request.getParameter("sign") != null) {
			sb.append(trg.showAllTimeReports(1, TimeReportGenerator.SHOW_SIGNED));
			session.setAttribute("state", UNSIGN);
		} else if(request.getParameter("unsign") != null) {
			sb.append(trg.showAllTimeReports(1, TimeReportGenerator.SHOW_UNSIGNED));
			session.setAttribute("state", SIGN);
		} else {
			sb.append("<form method=get action = SignTimeReports>");
			sb.append("<input type=" + formElement("submit") + 
					" name=" + formElement("sign") + " value=" + formElement("Show signed time reports") + ">");
			sb.append("<input type=" + formElement("submit") + 
					" name=" + formElement("unsign") + " value=" + formElement("Show unsigned time reports") + ">");
			sb.append("</form>");
		}
		//TODO: Change the 1 into a variable!!
		out.print(sb.toString());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("hello");
		HttpSession session = request.getSession(true);
		if(session.isNew())
			System.out.println("hej");
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		if(request.getParameter("sign") != null) {
			System.out.println("in here");
		} else if(request.getParameter("unsign") != null) {
			System.out.println("or in here");
		}
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
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
}
