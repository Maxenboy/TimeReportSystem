package subTimeReportMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.Activity;
import database.Database;
import database.TimeReport;

@WebServlet("/NewTimeReport")
public class NewTimeReport extends HttpServlet{
	private static final long serialVersionUID = 6091270182349510225L;
	private static final int NEW_REPORT = 1;
	private static final int FILLED_REPORT = 2;
	private static final int NOT_ENOUGH_DATA = 3;
	private static final int SUCCESSFUL_ADD = 4;
	private static final int UNSUCCESSFUL_ADD = 5;
	TimeReportGenerator trg = new TimeReportGenerator(new Database());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.print(getPageIntro());
		/*
		 *Check whether user has a role and member of a project.
		 *if not, print "You are currently not assigned to a project" or "You are currently not assigned to a project role". 
		 */
		int state = 0;
//		session.invalidate();
		if(session.isNew()) {
			state = NEW_REPORT;
		} else {
			state = (Integer) session.getAttribute("state");
		}
		String s;
		switch(state) {
		case NEW_REPORT:
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internal error");
			} else { 
				out.print(s);
			}
			break;
		case NOT_ENOUGH_DATA:
			s = trg.showNewTimeForm();
			if(s == null) {
				out.print("500 internal error");
			} else { 
				out.print(s);
				session.setAttribute("state", NOT_ENOUGH_DATA);
			}
			out.println("<script> alert('Mandatory data has not been filled in. \n Please fill in week nr. and at least one activity') </script>");
			break;
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/*
		 * Do shizzle 
		 */
		HttpSession session = request.getSession(true);
		
		String week = request.getParameter("week");
		if(week.equals("")) {
			session.setAttribute("state", NOT_ENOUGH_DATA);
			doGet(request,response);
		} else {
			//Change the values to valid ones
			TimeReport timeReport = new TimeReport(Integer.valueOf(week), 1, 1);
			String[] fields = request.getParameter("FormFields").split(",");
			ArrayList<Activity> activities = new ArrayList<Activity>();
			boolean filledIn = false;
			int activityNbr = 10;
			for(int i = 0; i < fields.length; i++) {
				String field = fields[i];
				String time = request.getParameter(field);
				if(i % 4 == 0)
					activityNbr++;
				if(!time.equals("")) {
					filledIn = true;
					if(i < 36) {
						activities.add(new Activity(activityNbr, Activity.mapIntToActivityType(i % 4), Integer.valueOf(time), timeReport.getId()));
					} else {
						System.out.println("outside of " + i);
						activities.add(new Activity(Integer.valueOf(field.trim()), Activity.ACTIVITY_TYPE_DEVELOPMENT, Integer.valueOf(time), timeReport.getId()));
					}
				}
			}
			if(!filledIn) {
				session.setAttribute("state", NOT_ENOUGH_DATA);
				doGet(request, response);
			} else {
				//save in database
				PrintWriter out = response.getWriter();
				if(trg.addNewTimeReport(timeReport, activities)) {
					out.print("<script>alert('Successfully saved time report') </script>");
					session.invalidate();
				} else {
					out.print("<script>alert('Internal error - could not save time report') </script>");
					session.invalidate();
				}
			}
			
			
		}
	}
	
	private String formElement(String par) {
		return '"' + par + '"';
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}

}
