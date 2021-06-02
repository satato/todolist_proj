package todo_package;

import java.util.ArrayList;

public interface List {

	/**
	 * adds the given task to the day's list
	 * @param task
	 * @return true if new task was successfully added, false if process fails for any reason
	 */
	public boolean add(ListItem task);
	
	/**
	 * removes the task with the given taskname from the day's list
	 * @param taskName
	 * @return true if task was successfully removed, false if process fails for any reason
	 */
	public boolean remove(String taskName);
	
	/**
	 * removes the task at the given index
	 * @param index
	 * @return true if task was successfully removed, false if process fails for any reason
	 */
	public boolean remove(int index);
	
	/**
	 * @return the object's itemlist - deep or shallow copy unnecessary, mutability is wanted
	 */
	public ArrayList<ListItem> getList();
	
	/**
	 * an Override of the .toString() method
	 * @return String
	 */
	@Override
	public String toString();
	
}
