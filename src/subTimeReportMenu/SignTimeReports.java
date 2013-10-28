package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Database;


@WebServlet("/SignTimeReports")
public class SignTimeReports extends HttpServlet {
	private static final int FIRST = 0;
	private static final int SIGN = 1;
	private static final int LIST = 2;
	private static final long serialVersionUID = -4213845458306512233L;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		StringBuilder sb = new StringBuilder();
//		session.invalidate();
		//check if we are suppose to sign or unsign a reportId.
		int state;
		if(session.isNew()) {
			state = FIRST;
		} else {
			state = (Integer) session.getAttribute("state");
		}
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
			System.out.println(request.getParameter("confirmUnsign"));
			System.out.println(request.getParameter("confirmSign"));
			System.out.println(request.getParameter("confirmRemove"));
			System.out.println(request.getParameter("reportId"));
			String reportId = (String) request.getParameter("reportId");
			String s = trg.signOrUnsignReport(reportId);
			if(s == null) {
				sb.append("<script type=text/javascript> alert(" + formElement("Internal error - please try again later") + ");</script>");
				session.setAttribute("state",FIRST);
				doGet(request, response);
			} else {
				sb.append(s);
				session.invalidate();
			}
			break;
		}
		if(state == SIGN) {
			System.out.println(request.getAttribute("reportId"));
			
		}
		out.print(sb.toString());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Signing/unsigning of time reports
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		String reportId = request.getParameter("reportId");
		if(reportId != null) {
			String s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_SIGN);
			session.setAttribute("state", SIGN);
			out.print(getPageIntro());
			out.print(s);
		} else {
			doGet(request,response);
		}
	}
	
	/**
	 * For development purposes only.
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
