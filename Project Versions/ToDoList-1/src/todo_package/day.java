package todo_package;

import java.util.ArrayList;

public class Day implements List {

	public int place;
	private int points = 0;
	private ArrayList<ListItem> list = new ArrayList<>();
	
	/**
	 * Constructor
	 * @param place
	 */
	public Day(int place) {
		if(place < 1 || place > 7) {
			throw new IllegalArgumentException("invalid day of the week");
		}
		this.place = place;
	}
	
	//setters
	/**
	 * adds the given task to the day's list
	 * @param task
	 * @return true if new task was successfully added, false if process fails for any reason
	 */
	public boolean add(ListItem task) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	//two types of "remove" methods
	/**
	 * removes the task with the given taskname from the day's list
	 * @param taskName
	 * @return true if task was successfully removed, false if process fails for any reason
	 */
	public boolean remove(String taskName) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	/**
	 * removes the task at the given index
	 * @param index
	 * @return true if task was successfully removed, false if process fails for any reason
	 */
	public boolean remove(int index) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	//getters
	/**
	 * gets the day's points
	 * @return int (points)
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * gets the day's task list
	 * @return ArrayList<listItem>
	 */
	public ArrayList<ListItem> getList() {
		return this.list; //no need for deep copy since mutability is wanted
	}
	
}
