package todo_package;

public class ListItem {
	//fields
	private String itemName;
	private int[] duedate = new int[3];
	private int[] dodate = new int[3];
	private int importance;
	private int size;
	private int closeness;
	private double priorityLevel;
	private boolean crossed = false;
	
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
		this.importance = -1;
		this.size = -1;
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
		if(!(duedate.equals("none") || duedate == null || duedate.length() == 10) || !(dodate.equals("none") || dodate == null || dodate.length() == 10)) {
			throw new IllegalArgumentException("invalid input");
		}
		
		this.itemName = name;
		this.importance = importance;
		this.size = size;
		
		//checks dates
		String date = duedate;
		int mm;
		int dd;
		int yyyy;
		
		for(int i = 0; i < 2; i++) {
			if(date.length() == 10) {
				if(date.charAt(2) != '/' || date.charAt(5) != '/') {
					throw new IllegalArgumentException("invalid input");
				}
				else {
					try {
						mm = Integer.parseInt(date.substring(0, 2));
						dd = Integer.parseInt(date.substring(3,5));
						yyyy = Integer.parseInt(date.substring(6));
					}
					catch (Exception e) {
						throw new IllegalArgumentException("invalid input");
					}
					
					//doesn't quite check for valid dates (ex. this would consider february 31st a valid date) but covers most of it
					if((mm < 1 || mm > 12) || (dd < 1 || dd > 31) || (yyyy < 1000 || yyyy > 9999)) {
						throw new IllegalArgumentException("invalid input");
					}
					else {
						if(i == 0) {
							this.duedate[0] = mm;
							this.duedate[1] = dd;
							this.duedate[2] = yyyy;
						}
						else {
							this.dodate[0] = mm;
							this.dodate[1] = dd;
							this.dodate[2] = yyyy;
						}
					}
				}
			}
			date = dodate;
		}
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
		
	}

	//methods
	/**
	 * gets the task/item's name
	 * @return String (item name)
	 */
	public String getName() {
		return new String(this.itemName);
	}
	
	/**
	 * gets the duedate as an array of integers
	 * @return int[] duedate
	 */
	public int[] getDueDate() {
		int[] result = new int[3];
		for(int i = 0; i < 3; i++) {
			result[i] = this.duedate[i];
		}
		return result;
	}
	
	/**
	 * gets the dodate as an array of integers
	 * @return int[] dodate
	 */
	public int[] getDoDate() {
		int[] result = new int[3];
		for(int i = 0; i < 3; i++) {
			result[i] = this.dodate[i];
		}
		return result;
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
		//score += closeness;
		score = (int)(score * 100);
		this.priorityLevel = score / 100.00;
	}
	
	//checks if a date is valid
	private boolean checkDate(String date) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	/**
	 * changes the due date so long as the date is valid
	 * @param date
	 */
	public void changeDueDate(String date) {
		
		//NOT YET FULLY IMPLEMENTED - CHECK THE DATES FIRST
		if(!date.equals("none")) {
			this.duedate[0] = Integer.parseInt(date.substring(0,2));
			this.duedate[1] = Integer.parseInt(date.substring(3,5));
			this.duedate[2] = Integer.parseInt(date.substring(6));
		}
		calculatePriority();
	}
	
	/**
	 * changes the do-date so long as the date is valid
	 * @param date
	 */
	public void changeDoDate(String date) {
		//NOT YET FULLY IMPLEMENTED - CHECK THE DATES FIRST
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
		
		if(this.crossed) {
			return "";
		}
		else {
			String due;
			String d;
			
			String mm;
			String dd;
			
			if(duedate[0] == 0)
				due = "none";
			else {
				if(duedate[0] < 10)
					due = "0" + duedate[0] + "/";
				else
					due = duedate[0] + "/";
				
				if(duedate[1] < 10)
					due += "0" + duedate[1] + "/";
				else
					due += duedate[1] + "/";
				due += duedate[2];
			}
			if(dodate[0] == 0)
				d = "none";
			else {
				if(dodate[0] < 10)
					d = "0" + dodate[0] + "/";
				else
					d = dodate[0] + "/";
				
				if(dodate[1] < 10)
					d += "0" + dodate[1] + "/";
				else
					d += dodate[1] + "/";
				d += dodate[2];
			}
			
			String result = "[" + this.itemName + " | due: " + due + " | do: " + d + " | importance: " + this.importance + " | workload: " + this.size + "]";
			
			return result;
		}
	}
	
	public void cross() {
		this.crossed = true;
	}
	
}
