package statistics;

import java.util.ArrayList;
import java.util.HashMap;

import database.Database;
import database.ProjectGroup;
import database.TimeReport;
import database.User;
import database.Activity;
public class testStats {

	public static void main(String[] args) {
		Database db = new Database();/*
		ProjectGroup pg1 = new ProjectGroup(1,"first", true, 1, 10, 1000);
		ProjectGroup pg2 = new ProjectGroup(2,"second", true, 1, 10, 2000);
		ProjectGroup pg3 = new ProjectGroup(3,"third", true, 1, 10, 1245);
		ProjectGroup pg4 = new ProjectGroup(4,"fourth", true, 1, 10, 952);
		ProjectGroup pg5 = new ProjectGroup(5,"fifth", true, 1, 10, 291);/*
		db.addProjectGroup(pg1);
		db.addProjectGroup(pg2);
		db.addProjectGroup(pg3);
		db.addProjectGroup(pg4);
		db.addProjectGroup(pg5);/*
		User u1 = new User(1, "anva1", "asdfqw", true, 1,1);
		User u2 = new User(2, "anva", "asdfqw", true, 2,2);
		User u3 = new User(3, "lennart", "asdfqw", true, 3,3);
		User u4 = new User(4, "leffe", "asdfqw", true, 4,4);
		User u5 = new User(5, "kalle", "asdfqw", true, 5,5);
		User u6 = new User(6, "nalle", "asdfqw", true, 6,1);
		User u7 = new User(7, "tjalle", "asdfqw", true, 7,2);/* 
		User u1 = new User("anv1");
		User u2 = new User("anv2");
		User u3 = new User("anv3");
		User u4 = new User("anv4");
		User u5 = new User("anv5");
		User u6 = new User("anv6");
		User u7 = new User("anv7");*//*
		db.addUserToProjectGroup(2,1);
		db.addUserToProjectGroup(3,2);
		db.addUserToProjectGroup(4,3);
		db.addUserToProjectGroup(5,4);
		db.addUserToProjectGroup(6,5);
		db.addUserToProjectGroup(7,4);
		db.addUserToProjectGroup(8,3);
		db.addUserToProjectGroup(9,2);
		db.addUserToProjectGroup(10,1);
*/
/*
		HashMap<Integer,Integer> roles = new HashMap<Integer,Integer>();
		roles.put(1,1);
		roles.put(2,2);
		roles.put(3,3);
		roles.put(4,4);
		roles.put(5,5);
		roles.put(6,6);
		roles.put(7,7);
		roles.put(8,6);
		roles.put(9,5);
		db.setUserRoles(roles);


		*/
		/*
		System.out.print(db.addUser(u1));
		db.addUser(u2);
		db.addUser(u3);
		db.addUser(u4);
		db.addUser(u5);
		db.addUser(u6);
		db.addUser(u7);*/
		
		/*
		TimeReport t1 = new TimeReport(1, 1, true, 1, 1);
		TimeReport t2 = new TimeReport(2, 2, true, 2, 2);
		TimeReport t3 = new TimeReport(3, 3, true, 3, 3);
		TimeReport t4 = new TimeReport(4, 4, true, 4, 4);
		TimeReport t5 = new TimeReport(5, 5, true, 5, 5);
		TimeReport t6 = new TimeReport(6, 6, true, 6, 1);
		
		Activity a1 = new Activity(1, "A", 241, 1);
		Activity a2 = new Activity(2, "s", 242, 2);
		Activity a3 = new Activity(3, "d", 243, 3);
		Activity a4 = new Activity(4, "r", 244, 4);
		Activity a5 = new Activity(5, "e", 245, 5);
		Activity a6 = new Activity(6, "e", 246, 6);
		Activity a7 = new Activity(5, "f", 247, 3);
		Activity a8 = new Activity(4, "r", 248, 1);
		Activity a9 = new Activity(3, "t", 249, 2);
		Activity a10 = new Activity(2, "d", 250, 5);
		Activity a11 = new Activity(1, "b", 251, 4);
		
		ArrayList<Activity> l1 = new ArrayList<Activity>();
		l1.add(a1);
		l1.add(a8);
		ArrayList<Activity> l2 = new ArrayList<Activity>();
		l2.add(a2);
		l2.add(a9);
		ArrayList<Activity> l3 = new ArrayList<Activity>();
		l3.add(a3);
		l3.add(a7);
		ArrayList<Activity> l4 = new ArrayList<Activity>();
		l4.add(a4);
		l4.add(a11);
		ArrayList<Activity> l5 = new ArrayList<Activity>();
		l5.add(a5);
		l5.add(a10);

		
		db.addTimeReport(t1,l1);
		db.addTimeReport(t2,l2);
		db.addTimeReport(t3,l3);
		db.addTimeReport(t4,l4);
		db.addTimeReport(t5,l5);

*/
		ProjectGroup pg = new ProjectGroup("TheProject", 1, 7, 180);
		db.addProjectGroup(pg);
		User user1 = new User("ada10xyz");
		User user2 = new User("ain10xyz");
		User user3 = new User("atf10xyz");
		db.addUser(user1);
		db.addUser(user2);
		db.addUser(user3);
		db.addUserToProjectGroup(user1.getId(), pg.getId());
		db.addUserToProjectGroup(user2.getId(), pg.getId());
		db.addUserToProjectGroup(user3.getId(), pg.getId());
		HashMap<Integer, Integer> roles = new HashMap<Integer, Integer>();
		roles.put(user1.getId(), User.ROLE_DEVELOPMENT_GROUP);
		roles.put(user2.getId(), User.ROLE_PROJECT_LEADER);
		roles.put(user3.getId(), User.ROLE_SYSTEM_GROUP);
		db.setUserRoles(roles);
		TimeReport timeReport1 = new TimeReport(1, user1.getId(), pg.getId());
		TimeReport timeReport2 = new TimeReport(2, user1.getId(), pg.getId());
		TimeReport timeReport3 = new TimeReport(3, user1.getId(), pg.getId());
		Activity activity1 = new Activity(Activity.ACTIVITY_NR_LECTURE, Activity.ACTIVITY_TYPE_OTHER, 90, timeReport1.getId());
		Activity activity2 = new Activity(Activity.ACTIVITY_NR_EXERCISE, Activity.ACTIVITY_TYPE_OTHER, 100, timeReport2.getId());
		Activity activity3 = new Activity(Activity.ACTIVITY_NR_SDP, Activity.ACTIVITY_TYPE_DEVELOPMENT, 200, timeReport3.getId());
		ArrayList<Activity> activities1 = new ArrayList<Activity>();
		ArrayList<Activity> activities2 = new ArrayList<Activity>();
		ArrayList<Activity> activities3 = new ArrayList<Activity>();
		activities1.add(activity1);
		activities2.add(activity2);
		activities3.add(activity3);
		db.addTimeReport(timeReport1, activities1);
		db.addTimeReport(timeReport2, activities2);
		db.addTimeReport(timeReport3, activities3);
		HashMap<String, ArrayList<String>> map = db.getStatisticsFilter(pg.getId());		

		

		/*private void testSetSessionData(HttpSession session) {
			session.setAttribute("user_Permissions", 4);
			session.setAttribute("project_group_id", 1);
			session.setAttribute("username","ada10xyz");
		}*/
		
	}
}
