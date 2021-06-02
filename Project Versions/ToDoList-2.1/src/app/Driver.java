package app;

import java.time.LocalDate;
import java.util.*;
import gui.*;

public class Driver {

	public static ArrayList<ItemList> lists = new ArrayList<>();
	public static ItemList orderedTasks = new ItemList("Task Order");
	private static String[] days = {"Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat."};
	private static String[] invalidNames = {"New List", "New Task", "Edit Task", "Edit List", "Edit Due Date",
									"Edit Do Date", "Edit Importance Level", "Edit Size/Workload", "Delete Task",
									"Delete List", "Rename List"};
	public static ArrayList<String> invalidRenames = new ArrayList<String>(Arrays.asList(invalidNames));
	
	public static void main(String[] args) {
		
		//generates initial, general list object, daily list, and weekly list
		ItemList general = new ItemList("General");
		//ItemList daily = new ItemList("Daily");
		ItemList weekly = new ItemList("Weekly");
				
		lists.add(general);
		lists.add(weekly);
		//lists.add(daily);
		
		Utilities.load();
		populateWeek();
		prepareWeek();
		
		//launches gui display
		DisplayUnit.display();
		
	}
	/**
	 * Orders all tasks appropriately, then designates tasks to each day of the week
	 */
	public static void prepareWeek() {
		ItemList general = lists.get(0);
		
		//assigns designated ItemList with all existing tasks in the "general" list, to be used for task order sorting
		orderedTasks.clear();
		for(int i = 0; i < general.getList().size(); i++) {
			orderedTasks.add(general.getList().get(i));
		}
		//initially orders tasks by predicted priority level
		if(orderedTasks.getList().size() > 1) {//only operates if there is more than one item in the list to sort
			Utilities.orderTasks(orderedTasks, 0);
			
			//once tasks are ordered, designates tasks to each day of the week
			dayPopulator();
		}
		
		
		Day day;
		for(int i = 0; i < 7; i++) {
			day = (Day) lists.get(1).get(i);
			System.out.println(day.getPoints());
		}
		
		
	}
	
	/**
	 * private helper method which clears the task list of each day of the week for a clean slate
	 */
	private static void clearWeek() {
		Day currentDay;
		for(int i = 0; i < 7; i++) {
			currentDay = (Day) lists.get(1).get(i);
			currentDay.clear();
		}
	}
	
	/**
	 * private helper method for populating days of the week with tasks based
	 * on sort order and time consumption. 
	 */
	private static void dayPopulator() {
		//before populating the days of the week with tasks, ensures that each day is empty first
		clearWeek();
		
		int i = 0;
		int o = 0;
		Day currentDay;
		
		while(i < 7) {
			currentDay = (Day) lists.get(1).get(i);
			
			if(o < orderedTasks.getList().size()) {
				if(!orderedTasks.get(o).crossed && currentDay.add(orderedTasks.get(o)))
					o++; //only adds incomplete tasks to the weekly to-do list
				else if(orderedTasks.get(o).crossed)
					o++; 
				else
					i++; //if the current day is full of tasks, move onto the next day
			}
			else
				i = 7;
		}
	}
	
	/**
	 * private helper method to prepare the Weekly list; 
	 * populates the Weekly list with days to be filled with tasks
	 */
	private static void populateWeek() {
		int index = 0;
		
		LocalDate today = LocalDate.now();
		today = LocalDate.of(2021, 4, 05); //for testing purposes
		today.getDayOfWeek();
		
		switch (today.getDayOfWeek()) {
		case SUNDAY:
			index = 0;
			break;
		case MONDAY:
			index = 1;
			break;
		case TUESDAY:
			index = 2;
			break;
		case WEDNESDAY:
			index = 3;
			break;
		case THURSDAY:
			index = 4;
			break;
		case FRIDAY:
			index = 5;
			break;
		case SATURDAY:
			index = 6;
			break;
		}
		
		int i = 0;
		int temp;
		while(i <= 6) {
			
			if((index + i) > 6)
				temp = (index + i) - 7;
			else
				temp = index + i;
			
			lists.get(1).add(new Day(days[temp]));
			i++;
		}
	}
	
	/**
	 * deletes the list with the given name
	 * @param list
	 */
	public static void deleteList(ItemList list) {
		for(int i = 0; i < lists.size(); i++) {
			if(list.id == lists.get(i).id) {
				lists.remove(i);
				return;
			}
		}
	}
	
	/**
	 * creates a list with the given name
	 * @param name
	 */
	public static void createList(String name) {
		lists.add(new ItemList(name));
	}
	
}
