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
		
		if(!session.isNew()) {
		Object state = session.getAttribute("sign");
			if(state != null) {
				int sign = (Integer) state;
				if(sign == UNSUCCESSFUL_SIGNING) {
					out.print("<script>window.alert('Signing of time Reports was unsuccessful')</script>");
				} else if(sign == NO_REPORTS_ENTERED) {
					out.print("<script>window.alert('No reports entered')</script>");
				}
			}
		}
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
		String[] reportIds = request.getParameterValues("reportIds");
		if(reportIds != null) {
			String s = trg.signOrUnsignReports(reportIds, false);
			if(s != null) {
				out.print(s);
				session.invalidate();
			} else {
				session.setAttribute("sign", UNSUCCESSFUL_SIGNING);
				doGet(request,response);
			}
		} else {
			session.setAttribute("sign", NO_REPORTS_ENTERED);
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
