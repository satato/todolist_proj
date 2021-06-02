package todo_package;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Utilities {

	private static String saveFile = "src/todo_package/saves.txt";
	
	/**
	 * loads all data off of save file(s), if there is any, for use
	 */
	public static void load() {
		ArrayList<ItemList> lists = Driver.lists;
		
		ItemList current;
		String listname;
		int index = 0;
		int tempindex = 0;
		int i = 0;
		int taskCount;
		
		String strTemp;
		int numTemp;
		
		//reads save file
		try(BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	
		    	index = 0;
		    	tempindex = 0;
		    	taskCount = 0;
		    	
		    	//reads the list name and creates a new list with that name
		    	index = line.indexOf('-');
		    	listname = line.substring(6, index - 1);
		    	if(i != 0) {
		    		lists.add(new ItemList(listname));
		    	}
		    	current = lists.get(i);
		    	i++;
		        
		    	//next determines the number of tasks this list contains
		    	for(int n = 0; n < line.length(); n++) {
		    		if(line.charAt(n) == '|') {
		    			taskCount++;
		    		}
		    	}
		    	taskCount = taskCount / 4;
		    	
		    	//then parses out each task appropriately
		    	for(int j = 0; j < taskCount; j++) {
		    		
		    		index = line.indexOf('[', index) + 1;
		    		tempindex = line.indexOf('|', tempindex) - 1;
		    		
		    		//adds a task with this task's name to the list in question
		    		current.add(new ListItem(line.substring(index, tempindex)));
		    		
		    		index = line.indexOf("due:", index) + 5;
		    		tempindex = line.indexOf('|', tempindex + 2) - 1;
		    		
		    		//sets the task's duedate based on save data
		    		current.get(j).changeDueDate(line.substring(index, tempindex));
		    		
		    		index = line.indexOf("do:", index) + 4;
		    		tempindex = line.indexOf('|', tempindex + 2) - 1;
		    		
		    		//sets the task's "do" date based on save data
		    		current.get(j).changeDoDate(line.substring(index, tempindex));
		    		
		    		index = line.indexOf("importance:", index) + 12;
		    		tempindex = line.indexOf('|', tempindex + 2) - 1;
		    		
		    		//sets the task's importance level based on save data
		    		current.get(j).setImportance(Integer.parseInt(line.substring(index, tempindex)));
		    		
		    		index = line.indexOf("workload:", index) + 10;
		    		tempindex = line.indexOf(']', tempindex) - 1;
		    		
		    		//sets the task's size/workload based on save data
		    		current.get(j).setSize(Integer.parseInt(line.substring(index, index + 1)));
		    	}
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

        try(var fr = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
        	
        	for(int i = 0; i < lists.size(); i++) {
        		fr.append(lists.get(i).toString()+"\n");
        	}
        	
            fr.close();
        } catch (IOException e) {
        	System.err.println("Save Unsuccessful");
			e.printStackTrace();
		}
        
        System.err.println("Save Successful");
	}
	
	/**
	 * clears all the saved data - cannot undo, double check with user before calling this method
	 */
	public static void clear() {
		
		try(var fr = new FileWriter(saveFile, StandardCharsets.UTF_8)) {
        	fr.flush();
            fr.close();
        } catch (IOException e) {
        	System.err.println("Save Unsuccessful");
			e.printStackTrace();
		}
	}
	
	/**
	 * orders the provided tasks list by one of two methods:
	 * 0: order using the program's own sorting algorithm by predicted priority
	 * 1: order based on task due dates, with soonest being first and furthest away last.
	 * @param list
	 * @param method
	 */
	public static void orderTasks(ItemList list, int method) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
}
