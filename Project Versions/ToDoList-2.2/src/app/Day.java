package app;

import java.util.ArrayList;

public class Day extends ListItem{

	/**
	 * Constructor
	 * @param name
	 */
	public Day(String name) {
		super(name);
	}

	private int points = 0; //a day's points can be, at maximum, 14
	private ArrayList<ListItem> list = new ArrayList<>();
	
	//setters
	/**
	 * adds the given task to the day's list
	 * @param task
	 * @return true if new task was successfully added, false if process fails for any reason
	 */
	public boolean add(ListItem task) {
		int addition = checkPoints(task);
		
		if(this.points + addition > 14)
			return false; //cannot add the current task to this day due to time constraints
		else {
			this.list.add(task);
			this.points += addition;
			return true; //successfully adds the current task to this day's task list
		}
	}
	
	/**
	 * @return the accumulated points for the given day
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * clears the day's list of tasks
	 */
	public void clear() {
		this.list.clear();
		this.points = 0;
	}
	
	/**
	 * removes the task at the given index
	 * @param index
	 * @return true if task was successfully removed, false if process fails for any reason
	 */
	public boolean remove(int index) {
		if(index > this.list.size() || index < 0 || this.list.size() == 0)
			return false; // fails to remove the task at given index, index does not exist
		else {
			this.points -= checkPoints(this.list.get(index));
			this.list.remove(index);
			return true; // successfully removes the task at given index.
		}
	}
	
	/**
	 * private helper method for determining the no. of 'points' a specified ListItem is worth
	 * @param task
	 * @return int
	 */
	private static int checkPoints(ListItem task) {
		int taskPts = 0;
		
		switch (task.getSize()) {
		case 1:
			taskPts = 2;
			break;
		case 2:
			taskPts = 4;
			break;
		case 3:
			taskPts = 8;
			break;
		case 4:
			taskPts = 12;
			break;
		case 5:
			taskPts = 14;
			break;
		}
		return taskPts;
	}
	
	//getters
	/**
	 * gets the day's task list
	 * @return ArrayList<listItem>
	 */
	public ArrayList<ListItem> getList() {
		return this.list; //no need for deep copy since mutability is wanted
	}
}
