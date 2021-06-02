package app;

import java.util.ArrayList;

public class Day extends ListItem {

	//fields
	private int points = 0; // can be at most 14
	private ArrayList<ListItem> list = new ArrayList<>();
	
	/**
	 * Constructor
	 * @param name
	 */
	public Day(String name) {
		super(name);
	}

	/**
	 * adds the given task to the day's list
	 * @param task
	 * @return true if new task was successfully added, false if process fails for any reason
	 */
	public boolean add(ListItem task) {
		throw new UnsupportedOperationException("not yet implemented");
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
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	/**
	 * gets the day's task list
	 * @return ArrayList<listItem>
	 */
	public ArrayList<ListItem> getList() {
		return this.list; //no need for deep copy since mutability is wanted
	}
	
}
