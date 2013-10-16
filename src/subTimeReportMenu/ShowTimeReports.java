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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		PrintWriter out = response.getWriter();
		Database db = new Database();
		TimeReportGenerator trg = new TimeReportGenerator(db);
		out.print(getPageIntro());
		String s = trg.showTimeReports(1);
		if(s == null)
			out.print("<p> Nothing to show </p>");
		else 
			out.print(s);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("in here?");
	}
	
	private String getPageIntro() {
		String intro = "<html>"
				+ "<head><title> The Base Block System </title></head>"
				+ "<body>";
		return intro;
	}

}
