package app;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ListItem {

	//fields
	private String itemName;
	private LocalDate duedate = null;
	private LocalDate dodate = null;
	private int importance = 0;
	private int size = 0;
	private double priority = 0;
	private int id;
	
	private static int ID = 0;
	public boolean crossed = false;
	
	/**
	 * Constructor
	 * @param name
	 */
	public ListItem(String name) {
		if(name == null)
			throw new IllegalArgumentException("invalid task name");
		this.itemName = name;
		this.id = ID;
		ID++;
		Driver.usedTaskNames.add(name);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param duedate
	 * @param dodate
	 * @param importance
	 * @param size
	 */
	public ListItem(String name, LocalDate duedate, LocalDate dodate, int importance, int size) {
		if(name == null || (importance < 0 || importance > 5) || (size < 0 || size > 5))
			throw new IllegalArgumentException("invalid parameter(s)");
		this.itemName = name;
		this.importance = importance;
		this.size = size;
		
		this.id = ID;
		ID++;
		calculatePriority();
		Driver.usedTaskNames.add(name);
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param importance
	 * @param size
	 */
	public ListItem(String name, int importance, int size) {
		if(name == null || (importance < 0 || importance > 5) || (size < 0 || size > 5))
			throw new IllegalArgumentException("invalid parameter(s)");
		this.itemName = name;
		this.importance = importance;
		this.size = size;
		
		this.id = ID;
		ID++;
		calculatePriority();
		Driver.usedTaskNames.add(name);
	}
	
	/**
	 * Copy Constructor
	 * @param item
	 */
	public ListItem(ListItem item) {
		
		this.duedate = item.getDue();
		this.dodate = item.getDo();
		
		this.itemName = item.getName();
		this.importance = item.getImportance();
		this.size = item.getSize();
		
		this.id = ID;
		ID++;
		calculatePriority();
	}
	
	/**
	 * returns this object's due date
	 * @return LocalDate
	 */
	public LocalDate getDue() {
		return this.duedate;
	}
	
	/**
	 * returns this object's "do" date
	 * @return LocalDate
	 */
	public LocalDate getDo() {
		return this.dodate;
	}
	
	/**
	 * gets the task/item's name
	 * @return String (item name)
	 */
	public String getName() {
		return new String(this.itemName);
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
	public double getPriority() {
		return this.priority;
	}
	
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
		this.priority = score / 1000.00;
	}
	
	/**
	 * private helper method
	 * @return
	 */
	private double determineCloseness() {
		LocalDate today = LocalDate.now();
		double result;
		if(duedate == null) {
			result = 0;
		}
		else {
			long days;
			if(today.compareTo(duedate) < 0)
				days = ChronoUnit.DAYS.between(today, duedate);
			else if(today.compareTo(duedate) > 0)
				days = ChronoUnit.DAYS.between(duedate, today) * -1;
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
	 * changes the due date to a given LocalDate object
	 * @param date
	 */
	public void changeDue(LocalDate date) {
		this.duedate = date;
	}
	
	/**
	 * changes the due date into the LocalDate formed from parsing out the string input
	 * @param date
	 */
	public void changeDue(String date) {
		if(date.equals("none"))
			this.duedate = null;
		else {
			try {
				int month = Integer.parseInt(date.substring(0,2));
				int day = Integer.parseInt(date.substring(3,5));
				int year = Integer.parseInt(date.substring(6));
				
				this.duedate = LocalDate.of(year, month, day);
			}
			catch(Exception e) {
				System.err.println("invalid due date provided: " + date);
			}
		}
	}
	
	/**
	 * changes the "do" date to a given LocalDate object
	 * @param date
	 */
	public void changeDo(LocalDate date) {
		this.dodate = date;
	}
	
	/**
	 * changes the "do" date into the LocalDate formed from parsing out the string input
	 * @param date
	 */
	public void changeDo(String date) {
		if(date.equals("none"))
			this.dodate = null;
		else {
			try {
				int month = Integer.parseInt(date.substring(0,2));
				int day = Integer.parseInt(date.substring(3,5));
				int year = Integer.parseInt(date.substring(6));
				
				this.dodate = LocalDate.of(year, month, day);
			}
			catch(Exception e) {
				System.err.println("invalid due date provided: " + date);
			}
		}
	}
	
	/**
	 * sets the importance level
	 * @param level
	 */
	public void setImportance(int level) {
		if(level < 0 || level > 5) {
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
		if(size < 0 || size > 5) {
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
			
		due = this.duedate.getMonthValue() + "/" + this.duedate.getDayOfMonth() + "/" + this.duedate.getYear();
		d = this.dodate.getMonthValue() + "/" + this.dodate.getDayOfMonth() + "/" + this.dodate.getYear();
			
		return "[" + this.itemName + " | due: " + due + " | do: " + d + " | importance: " + this.importance + " | workload: " + this.size + " | crossed: " + this.crossed + " | ID: " + this.id + "]";
	}
	
	public String toString(int i) {
		String due;
		String d;
		
		if(duedate == null)
			due = "null";
		else {
			String temp = String.valueOf(this.duedate.getMonthValue());
			if(this.duedate.getMonthValue() < 10)
				temp = "0" + this.duedate.getMonthValue();
			
			due = temp + "/";
			
			temp = String.valueOf(this.duedate.getDayOfMonth());
			if(this.duedate.getDayOfMonth() < 10)
				temp = "0" + this.duedate.getDayOfMonth();
			
			due += temp + "/" + this.duedate.getYear();
		}
		
		if(dodate == null)
			d = "null";
		else {
			String temp = String.valueOf(this.dodate.getMonthValue());
			if(this.dodate.getMonthValue() < 10)
				temp = "0" + this.dodate.getMonthValue();
			
			d = temp + "/";
			
			temp = String.valueOf(this.dodate.getDayOfMonth());
			if(this.dodate.getDayOfMonth() < 10)
				temp = "0" + this.dodate.getDayOfMonth();
			
			d += temp + "/" + this.dodate.getYear();
		}
			
		return this.itemName + "," + due + "," + d + "," + this.importance + "," + this.size + "," + this.crossed;
	}
	
	
	
}
