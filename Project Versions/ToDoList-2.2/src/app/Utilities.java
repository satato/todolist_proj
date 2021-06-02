package app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import sorting.*;

public class Utilities {

	private static String saveFile = "/savedata.txt";
	private static String dir = System.getProperty("user.dir");
	private static File ref = new File(dir + saveFile);
	
	/**
	 * loads all data off of save file(s), if there is any, for use
	 */
	public static void load() {
		ArrayList<ItemList> lists = Driver.lists;
		
		if(!ref.exists()) {
			try {
				ref.createNewFile();
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "unable to create save file. data may not be stored");
			}
		}
		
		ItemList current;
		String listname;
		int index = 0;
		int tempindex = 0;
		int i = 0;
		int taskCount;
		
		try(BufferedReader br = new BufferedReader(new FileReader(ref))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	
		    	index = 0;
		    	tempindex = 0;
		    	taskCount = 0;
		    	
		    	//reads the list name and creates a new list with that name
		    	index = line.indexOf('-');
		    	listname = line.substring(6, index - 1);
		    	if(i != 0 && i != 1) {
		    		lists.add(new ItemList(listname));
		    	}
		    	current = lists.get(i);
		    	
		    	//next determines the number of tasks this list contains
		    	for(int n = 0; n < line.length(); n++) {
		    		if(line.charAt(n) == '|') {
		    			taskCount++;
		    		}
		    	}
		    	taskCount = taskCount / 6;
		    	
		    	//then parses out each task appropriately
		    	for(int j = 0; j < taskCount; j++) {
		    		
		    		index = line.indexOf('[', index) + 1;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//adds a task with this task's name to the list in question
		    		current.add(new ListItem(line.substring(index, tempindex)));
		    		
		    		index = line.indexOf("due:", index) + 5;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//sets the task's duedate based on save data
		    		current.get(j).changeDueDate(line.substring(index, tempindex));
		    		
		    		index = line.indexOf("do:", index) + 4;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//sets the task's "do" date based on save data
		    		current.get(j).changeDoDate(line.substring(index, tempindex));
		    		
		    		index = line.indexOf("importance:", index) + 12;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//sets the task's importance level based on save data
		    		current.get(j).setImportance(Integer.parseInt(line.substring(index, tempindex)));
		    		
		    		index = line.indexOf("workload:", index) + 10;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//sets the task's size/workload based on save data
		    		current.get(j).setSize(Integer.parseInt(line.substring(index, tempindex)));
		    		
		    		index = line.indexOf("crossed:", index) + 9;
		    		tempindex = line.indexOf('|', index) - 1;
		    		
		    		//sets the task's "crossed" field based on save data
		    		if(line.substring(index, tempindex).equals("true")) {
		    			current.get(j).crossed = true;
		    		}
		    	}
		    	i++;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * saves all the necessary data to resume function upon next load of the program
	 */
	public static void save() {
		
		ArrayList<ItemList> lists = Driver.lists;
		clear();
		
        try(var fr = new FileWriter(ref, StandardCharsets.UTF_8)) {
        	
        	for(int i = 0; i < lists.size(); i++) {
        		if(lists.get(i).getName().equals("Weekly")) {//if it's the weekly list, do not save its items!
        			fr.append("list: Weekly - \n");
        		}
        		else
        			fr.append(lists.get(i).toString()+"\n");
        	}
        	
            fr.close();
        } catch (IOException e) {
        	return;
		}
	}
	
	/**
	 * clears all the saved data - cannot undo, double check with user before calling this method
	 */
	public static void clear() {
		
		try(var fr = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
        	fr.flush();
            fr.close();
        } catch (IOException e) {
        	System.err.println("Clear Unsuccessful");
			e.printStackTrace();
		}
	}
	
	/**
	 * orders the provided tasks list by one of two methods:
	 * 0: order using the program's own sorting algorithm by predicted priority
	 * 1: order based on task due dates, with soonest being first and furthest away last.
	 * 2: order based on task "do" dates, with soonest being first and furthest away last.
	 * Directly modifies the list parameter
	 * @param list
	 * @param method
	 */
	public static void orderTasks(ItemList list, int method) {
		
		if(list == null)
			throw new IllegalArgumentException("Error: cannot sort null list");
		else if(method == 0) {//sorts the tasks in the given list by predicted priority level
			Collections.sort(list.getList(), new Sortbypriority());
		}
		else if(method == 1) {//sorts the tasks in the given list by due-date
			Collections.sort(list.getList(), new Sortbyduedate());
		}
		else if(method == 2) {//sorts the tasks in the given list by assigned "do"-date
			Collections.sort(list.getList(), new Sortbydodate());
		}
	}
	
	/**
	 * helper method to rebuild dates that are in array format as mm/dd/yyyy
	 * @param date
	 * @return
	 */
	public static String rebuildDate(int[] date) {
		String result = "";
		if(date[0] == 0)
			result = "none";
		else {
			if(date[0] < 10)
				result = "0" + date[0] + "/";
			else
				result = date[0] + "/";
			
			if(date[1] < 10)
				result += "0" + date[1] + "/";
			else
				result += date[1] + "/";
			result += date[2];
		}
		return result;
	}
	
}