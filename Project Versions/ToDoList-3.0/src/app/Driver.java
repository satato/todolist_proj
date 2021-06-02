package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import sorting.*;
import gui.DisplayUnit;

public class Driver {

	public static ArrayList<ItemList> lists = new ArrayList<>();
	public static ArrayList<String> invalidNames = new ArrayList<String>(Arrays.asList(
			"New List", "New Task", "Edit Task", "Edit List", "Edit Due Date",
			"Edit Do Date", "Edit Importance Level", "Edit Size/Workload", "Delete Task",
			"Delete List", "Rename List", "Home", "New", "Edit", "Help"));
	public static ArrayList<String> invalidCharacters = new ArrayList<String>(Arrays.asList(
			"-", "|", "[", "]", ",", ";"));
	public static ArrayList<ItemList> hiddenlists = new ArrayList<>();
	public static ArrayList<String> usedTaskNames = new ArrayList<>();
	public static ArrayList<String> usedListNames = new ArrayList<>();
	
	private static ItemList gen;
	private static LocalDate today = LocalDate.now();
	public static DisplayUnit display;
	
	public static void main(String[] args) {

		//generates initial lists
		lists.add(new ItemList("General"));
		lists.add(new ItemList("Weekly"));
		
		Utilities.load();
		
		gen = lists.get(0);
		
		//generates "hidden" lists - aka lists that are not to be tabbed but rather
		//displayed on the home screen upon launch
		hiddenlists.add(new ItemList("Overdue"));
		populateOverdue(hiddenlists.get(0));
		hiddenlists.add(new ItemList("Today"));
		populateToday(hiddenlists.get(1));
		hiddenlists.add(new ItemList("Undated"));
		populateUndated(hiddenlists.get(2));
		hiddenlists.add(new ItemList("Upcoming"));
		populateUpcoming(hiddenlists.get(3));
		hiddenlists.add(new ItemList("Completed"));
		populateCompleted(hiddenlists.get(4));
		
		//calls and creates display
		display = new DisplayUnit();
	}
	
	private static void populateOverdue(ItemList list) {
		//cycles through gen and adds any overdue tasks to the overdue list
		list.clear();
		ArrayList<ListItem> temp = gen.getList();
		for(int i = 0; i < temp.size(); i++) {
			if(temp.get(i).getDue() != null && temp.get(i).getDue().compareTo(today) < 0) {
				list.add(temp.get(i));
			}
		}
		Collections.sort(list.getList(), new Sortbyduedate());
	}
	
	private static void populateToday(ItemList list) {
		//cycles through gen and adds any tasks "do" dated or due-dated for today to today's list
		list.clear();
		ArrayList<ListItem> temp = gen.getList();
		for(int i = 0; i < temp.size(); i++) {
			if((temp.get(i).getDue() != null && temp.get(i).getDue() == today) || (temp.get(i).getDo() != null && temp.get(i).getDo() == today)) {
				list.add(temp.get(i));
			}
		}
		Collections.sort(list.getList(), new Sortbydodate());
	}
	
	private static void populateUndated(ItemList list) {
		//cycles through gen and adds any undated tasks to the undated list
		list.clear();
		ArrayList<ListItem> temp = gen.getList();
		for(int i = 0; i < temp.size(); i++) {
			if(temp.get(i).getDue() == null && temp.get(i).getDo() == null) {
				list.add(temp.get(i));
			}
		}
		Collections.sort(list.getList(), new Sortbyname());
	}
		
	private static void populateUpcoming(ItemList list) {
		//cycles through gen and adds any upcoming tasks to the upcoming list
		list.clear();
		ArrayList<ListItem> temp = gen.getList();
		for(int i = 0; i < temp.size(); i++) {
			if(temp.get(i).getDue() != null && temp.get(i).getDue().compareTo(today) > 0) {
				list.add(temp.get(i));
			}
		}
		Collections.sort(list.getList(), new Sortbyduedate());
	}
	
	private static void populateCompleted(ItemList list) {
		//cycles through gen and adds any completed tasks to the completed list
		list.clear();
		ArrayList<ListItem> temp = gen.getList();
		for(int i = 0; i < temp.size(); i++) {
			if(temp.get(i).crossed) {
				list.add(temp.get(i));
			}
		}
		Collections.sort(list.getList(), new Sortbyduedate());
		Collections.reverse(list.getList());
	}

}
