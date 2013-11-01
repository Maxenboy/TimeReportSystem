package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;


@WebServlet("/SignTimeReports")
public class SignTimeReports extends TimeReportingMenu {
	public static final int FIRST = 0;
	public static final int SIGN = 1;
	public static final int LIST = 2;
	private static final long serialVersionUID = -4213845458306512233L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("fuck off");
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		int permission = (Integer) session.getAttribute("user_permissions");
		out.append(getPageIntro());
		out.print(generateMainMenu(permission, request));
		out.print(generateSubMenu(permission));
		
		switch(permission) {
		case PERMISSION_ADMIN:
		case PERMISSION_PROJ_LEADER:
			StringBuilder sb = new StringBuilder();
			int state= (Integer) session.getAttribute("signState");
			switch(state) {
			case FIRST:
				//First time visiting page.
				sb.append("<form method=get action = SignTimeReports>");
				sb.append("<input type=" + formElement("submit") + 
						" name=" + formElement("sign") + " value=" + formElement("Show signed time reports") + ">");
				sb.append("<input type=" + formElement("submit") + 
						" name=" + formElement("unsign") + " value=" + formElement("Show unsigned time reports") + ">");
				sb.append("</form>");
				session.setAttribute("state", LIST);
				break;
			case LIST:
				//List signed or unsigned time reports
				if(request.getParameter("sign") != null) {
					sb.append(trg.showAllTimeReports(1, TimeReportGenerator.SHOW_SIGN));
				} else if(request.getParameter("unsign") != null){
					sb.append(trg.showAllTimeReports(1, TimeReportGenerator.SHOW_UNSIGNED));
				}
				break;
			case SIGN:
				// Do signing/unsigning of timeReport
				System.out.println(request.getParameter("reportId"));
				String reportId = (String) request.getParameter("reportId");
				boolean success = trg.signOrUnsignReport(reportId);
				if(success) {
					out.print("<script> alert(" + formElement("Internal error - please try again later") + ");</script>");
					session.setAttribute("signState",FIRST);
					doGet(request, response);
				} else {
					out.print("<script> alert('Successful signing/unsigning of report!')</script>");
					session.setAttribute("signState",FIRST);
					doGet(request, response);
				}
				break;
			}
			break;
		default:
			out.append("<script>alert('Permission denied')</script>");
		}
		out.print(getPageOutro());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Signing/unsigning of time reports
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String reportId = request.getParameter("reportId");
		if(reportId != null) {
			String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_SIGN);
			session.setAttribute("signState", SIGN);
			out.print(getPageIntro());
			out.print(s);
		} else {
			doGet(request,response);
		}
	}
}
