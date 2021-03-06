package subTimeReportMenu;

import gui.TimeReportingMenu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Denna WebServlet används för att signera eller avsignera tidrapporter i systemet
 * @author martin
 *
 */
@WebServlet("/SignTimeReports")
public class SignTimeReports extends TimeReportingMenu {
	public static final int FIRST = 0;
	public static final int SIGN = 1;
	public static final int LIST = 2;
	public static final int ADMINISTRATOR = 3;
	public static final int SHOW = 4;
	private static final long serialVersionUID = -4213845458306512233L;
	TimeReportGenerator trg = new TimeReportGenerator(db);
	/**
	 * Denna metod används första gången användaren besöker sidan, visar antingen två knappar 
	 * (välja att visa signerade eller osignerade tidrapporter), eller en lista med projektgrupper. 
	 * @param HttpServletRequest request, HttpServletResponse response
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			int state= (Integer) session.getAttribute("signState");
			
			switch(permission) {
			case PERMISSION_ADMIN:
				switch(state) {
				case FIRST:
					String s;
					out.append(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					s = trg.showAllTimeReports(0, TimeReportGenerator.SIGN_PRJ);
					if(s != null)
						out.print(s);
					else
						out.print("Det finns inga projektgrupper att visa");
					out.print(getPageOutro());
					session.setAttribute("signState", ADMINISTRATOR);
					break;
				default:
					session.setAttribute("signState", FIRST);
					doGet(request, response);
					break;
				}
				break;
			case PERMISSION_PROJ_LEADER:
				StringBuilder sb = new StringBuilder();
				switch(state) {
				case FIRST:
					out.append(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					//First time visiting page.
					sb.append("<form method=post action = SignTimeReports>");
					sb.append("<input type=" + formElement("submit") + 
							" name=" + formElement("sign") + " value=" + formElement("Visa signerade tidrapporter") + ">");
					sb.append("<input type=" + formElement("submit") + 
							" name=" + formElement("unsign") + " value=" + formElement("Visa osignerade tidrapporter") + ">");
					sb.append("</form>");
					out.print(sb.toString());
					out.print(getPageOutro());
					session.setAttribute("signState", LIST);
					break;
				default:
					session.setAttribute("signState", FIRST);
					doGet(request, response);
					break;
				}
				break;
			default:
				out.append("<script>alert('\u00C5tkomst nekad')</script>");
			}
			out.print(getPageOutro());
		} else {
			response.sendRedirect("LogIn");
		}
	}
	/**	
	 * Denna metod används för att signera/avsignera tidrapporter, kontaktar TimeReportGenerator för att signera/avsignera.
	 * @param HttpServletRequest request, HttpServletResponse response
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (loggedIn(request)) {
			//Signing/unsigning of time reports
			String reportId = new String();
			HttpSession session = request.getSession(true);
			PrintWriter out = response.getWriter();
			int permission = (Integer) session.getAttribute("user_permissions");
			int state = (Integer) session.getAttribute("signState");
			StringBuilder sb = new StringBuilder();
			String s;
			switch(permission) {
			case PERMISSION_ADMIN:
				String prjGroup;
				switch(state) {
				case ADMINISTRATOR:
					prjGroup = request.getParameter("prjGroup");
					sb.append("<form method=post action = SignTimeReports>");
					sb.append("<input type=" + formElement("submit") + 
							" name=" + formElement("sign") + " value=" + formElement("Visa signerade tidrapporter") + ">");
					sb.append("<input type=" + formElement("submit") + 
							" name=" + formElement("unsign") + " value=" + formElement("Visa osignerade tidrapporter") + ">");
					sb.append("<input type=\"hidden\" name=\"prjGroup\" value=" + formElement(prjGroup) + ">");
					sb.append("</form>");
					session.setAttribute("signState", LIST);
					out.print(getPageIntro());
					out.print(generateMainMenu(permission, request));
					out.print(generateSubMenu(permission));
					out.print(sb.toString());
					out.print(getPageOutro());
					break;
				case LIST:
					prjGroup = request.getParameter("prjGroup");
					if(request.getParameter("sign") != null) {
						s = trg.showAllTimeReports(Integer.valueOf(prjGroup), TimeReportGenerator.SHOW_SIGN);
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						if(s == null)
							out.print("Det finns inga tidrapporter att visa.");
						else 
							out.print(s);
						out.print(getPageOutro());
					} else if(request.getParameter("unsign") != null){
						s = trg.showAllTimeReports(Integer.valueOf(prjGroup), TimeReportGenerator.SHOW_UNSIGNED);
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						if(s == null)
							out.print("Det finns inga tidrapporter att visa.");
						else 
							out.print(s);
						out.print(getPageOutro());
					}
					session.setAttribute("signState", SHOW);
					break;
				case SIGN:
					reportId = (String) request.getParameter("reportId");
					boolean success = trg.signOrUnsignReport(reportId);
					if(success) {
						out.print("<script> alert('Tidrapport signerad/avsignerad!')</script>");
						session.setAttribute("signState",FIRST);
						doGet(request, response);
					} else {
						out.print("<script> alert(" + formElement("Internt fel - tidrapporten kunde ej signeras") + ");</script>");
						session.setAttribute("signState",FIRST);
						doGet(request, response);
					}
					break;
				case SHOW:
					reportId = request.getParameter("reportId");
					if(reportId != null) {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_SIGN);
						out.print(s);
						out.print(getPageOutro());
						session.setAttribute("signState", SIGN);
					} else {
						out.print("<script> alert('Internt fel')</script>");
						session.setAttribute("signState", FIRST);
						doGet(request,response);
					}
					break;
				}
				break;
			case PERMISSION_PROJ_LEADER:
				switch(state) {
				case LIST:
					int projGroup = (Integer) session.getAttribute("project_group_id");
					if(request.getParameter("sign") != null) {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						s = trg.showAllTimeReports(projGroup, TimeReportGenerator.SHOW_SIGN);
						if(s == null)
							out.print("Det finns inga tidrapporter att visa");
						else 
							out.print(s);
						out.print(getPageOutro());
					} else if(request.getParameter("unsign") != null){
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						s = trg.showAllTimeReports(projGroup, TimeReportGenerator.SHOW_UNSIGNED);
						if(s == null)
							out.print("Det finns inga tidrapporter att visa");
						else 
							out.print(s);
						out.print(getPageOutro());
					}
					session.setAttribute("signState", SHOW);
					
					out.print(getPageOutro());
					break;
				case SHOW:
					reportId = request.getParameter("reportId");
					if(reportId != null) {
						out.print(getPageIntro());
						out.print(generateMainMenu(permission, request));
						out.print(generateSubMenu(permission));
						out.print(getPageIntro());
						s = trg.showTimeReport(Integer.valueOf(reportId), TimeReportGenerator.SHOW_SIGN);
						if(s == null)
							out.print("Det finns ingen tidrapporter att visa");
						else
							out.print(s);
						out.print(getPageOutro());
						session.setAttribute("signState", SIGN);
					} else {
						out.print("<script> alert('500 - Internt fel')</script>");
						doGet(request,response);
						session.setAttribute("signState", FIRST);
					}
					break;
				case SIGN:
					reportId = (String) request.getParameter("reportId");
					boolean success = trg.signOrUnsignReport(reportId);
					if(success) {
						out.print("<script> alert('Tidrapport signerad/avsignerad!')</script>");
						session.setAttribute("signState",FIRST);
						doGet(request, response);
					} else {
						out.print("<script> alert(" + formElement("Internt fel - tidrapporten kunde ej signeras") + ");</script>");
						session.setAttribute("signState",FIRST);
						doGet(request, response);
					}
					break;
				}
				break;
			}
		} else {
			response.sendRedirect("LogIn");
		}
	}
}
