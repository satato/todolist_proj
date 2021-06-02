package app;

import java.util.Comparator;
import java.time.*;
import java.time.temporal.*;

public class ListItem implements Item, java.lang.Comparable<ListItem> {
	//fields
	private String itemName;
	private int[] duedate = new int[3];
	private int[] dodate = new int[3];
	private int importance;
	private int size;
	private double priorityLevel;
	public boolean crossed = false;
	public int id;
	private static int ID = 0;
	
	//constructors
	/**
	 * Constructor
	 * @param name
	 */
	public ListItem(String name) {
		if(name == null) {
			throw new IllegalArgumentException("invalid input");
		}
		this.itemName = name;
		this.importance = 1;
		this.size = 1;
		this.id = ID;
		ID++;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param duedate
	 * @param dodate
	 * @param importance
	 * @param size
	 */
	public ListItem(String name, String duedate, String dodate, int importance, int size) {
		//checks for valid input
		if(name == null || (importance < 1 || importance > 5) || (size < 1 || size > 5)) {
			throw new IllegalArgumentException("invalid input");
		}
		this.itemName = name;
		this.importance = importance;
		this.size = size;
		
		//date validity is checked before any constructors are called
		
		this.id = ID;
		ID++;
		calculatePriority();
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param importance
	 * @param size
	 */
	public ListItem(String name, int importance, int size) {
		//checks for valid input
		if(name == null || (importance < 1 || importance > 5) || (size < 1 || size > 5)) {
			throw new IllegalArgumentException("invalid input");
		}
		
		this.itemName = name;
		this.importance = importance;
		this.size = size;
		this.id = ID;
		ID++;
		calculatePriority();
	}
	
	/**
	 * Copy Constructor
	 * @param item
	 */
	public ListItem(ListItem item) {
		int[] temp = new int[3];
		temp = item.getDueDate();
		for(int i = 0; i < 3; i++) {
			this.duedate[i] = temp[i];
		}
		temp = item.getDoDate();
		for(int i = 0; i < 3; i++) {
			this.dodate[i] = temp[i];
		}
		
		this.itemName = item.getName();
		this.importance = item.getImportance();
		this.size = item.getSize();
		this.priorityLevel = item.getPriorityLevel();
		
		this.id = item.id;
	}

	//methods
	/**
	 * gets the task/item's name
	 * @return String (item name)
	 */
	public String getName() {
		return new String(this.itemName);
	}
	
	//date validity is checked before any date setter methods are called
	/**
	 * gets the duedate as an array of integers
	 * @return int[]
	 */
	public int[] getDueDate() {
		int[] temp = new int[3];
		
		for(int i = 0; i < 3; i++) {
			temp[i] = this.duedate[i];
		}
		
		return temp;
	}
	
	/**
	 * gets the dodate as an array of integers
	 * @return int[]
	 */
	public int[] getDoDate() {
		int[] temp = new int[3];
		
		for(int i = 0; i < 3; i++) {
			temp[i] = this.dodate[i];
		}
		
		return temp;
	}
	
	/**
	 * gets the duedate as a string in the format mm/dd/yyyy
	 * @return String duedate
	 */
	public String getDueDateAsString() {
		return Utilities.rebuildDate(this.duedate);
	}
	
	/**
	 * gets the dodate as a string in the format mm/dd/yyyy
	 * @return String dodate
	 */
	public String getDoDateAsString() {
		return Utilities.rebuildDate(this.dodate);
	}
	
	/**
	 * gets the assigned level of importance
	 * @return int (importance level)
	 */
	public int getImportance() {
		return this.importance;
	}
	
	/**
	 * gets the task's workload/size rank
	 * @return int (size/workload)
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * gets the task's calculated priority level
	 * @return double (priority level)
	 */
	public double getPriorityLevel() {
		return this.priorityLevel;
	}
	
	/**
	 * gets the item's key information
	 * @return String of data
	 */
	public String getData() {
		String result;
		String due = duedate[0] + "/" + duedate[1] + "/" + duedate[2];
		String doDate = dodate[0] + "/" + dodate[1] + "/" + dodate[2];
		
		result = due + doDate + importance + size + priorityLevel;
		
		return result;
	}
	
	//setters
	/**
	 * calculates the task's predicted priority level
	 */
	private void calculatePriority() {
		double score = 0;
		if(this.importance == this.size) {
			score = (size * 0.64) + 1.48;
		}
		else if(importance >= 3) {
			score = (importance * 0.338) + 3.35;
		}
		else if(size >= 3) {
			score = (size * 1.512) + 2.1;
		}
		score += determineCloseness();
		score = (int)((score / 2.0) * 1000);
		this.priorityLevel = score / 1000.00;
	}
	
	private double determineCloseness() {
		
		LocalDate today = LocalDate.now();
		double result;
		if(duedate[0] == 0) {
			result = 0;
		}
		else {
			LocalDate due = LocalDate.of(duedate[2], duedate[0], duedate[1]);
			long days;
			if(today.compareTo(due) < 0)
				days = ChronoUnit.DAYS.between(today, due);
			else if(today.compareTo(due) > 0)
				days = ChronoUnit.DAYS.between(due, today) * -1;
			else
				days = 0;
			
			if(days < 0)
				result = 0;
			else
				result = days * (-0.487) + 5.5;
		}
		
		return result;
	}
	
	/**
	 * changes the due date
	 * @param date
	 */
	public void changeDueDate(String date) {
		if(!date.equals("none")) {
			this.duedate[0] = Integer.parseInt(date.substring(0,2));
			this.duedate[1] = Integer.parseInt(date.substring(3,5));
			this.duedate[2] = Integer.parseInt(date.substring(6));
		}
		calculatePriority();
	}
	
	/**
	 * changes the do-date
	 * @param date
	 */
	public void changeDoDate(String date) {
		if(!date.equals("none")) {
			this.dodate[0] = Integer.parseInt(date.substring(0,2));
			this.dodate[1] = Integer.parseInt(date.substring(3,5));
			this.dodate[2] = Integer.parseInt(date.substring(6));
		}
		calculatePriority();
	}
	
	/**
	 * sets the importance level
	 * @param level
	 */
	public void setImportance(int level) {
		if(level < 1 || level > 5) {
			throw new IllegalArgumentException("invalid input");
		}
		else {
			this.importance = level;
		}
		calculatePriority();
	}
	
	/**
	 * sets the new size/workload
	 * @param size
	 */
	public void setSize(int size) {
		if(size < 1 || size > 5) {
			throw new IllegalArgumentException("invalid input");
		}
		else {
			this.size = size;
		}
		calculatePriority();
	}
	
	@Override
	public String toString() {
		String due;
		String d;
			
		if(duedate[0] == 0)
			due = "none";
		else {
			due = Utilities.rebuildDate(duedate);
		}
		if(dodate[0] == 0)
			d = "none";
		else {
			d = Utilities.rebuildDate(dodate);
		}
			
		return "[" + this.itemName + " | due: " + due + " | do: " + d + " | importance: " + this.importance + " | workload: " + this.size + " | crossed: " + this.crossed + " | ID: " + this.id + "]";
	}

	@Override
	public int compareTo(ListItem i) {
		if(this.priorityLevel < i.getPriorityLevel())
			return -1;
		else if(this.priorityLevel > i.getPriorityLevel())
			return 1;
		else
			return 0;
	}
	
	
}