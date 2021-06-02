package app;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import sorting.*;

public class Utilities {

	private static String saveFile = "/savedata.txt";
	private static String dir = System.getProperty("user.dir") + "/todo_data";
	private static File dirRef = new File(dir);
	private static File ref = new File(dir + saveFile);
	
	/**
	 * loads all data off of save file if there is any
	 */
	public static void load() {
		ArrayList<ItemList> lists = Driver.lists;
		
		if(!dirRef.exists()) {
			try {
				dirRef.mkdirs();
				if(!ref.exists()) {
					ref.createNewFile();
				}
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "unable to create save file. data may not be stored");
			}
		}
		
		try(BufferedReader br = new BufferedReader(new FileReader(ref))) {
			ItemList current;
			String listname;
			int index = 0;
			int tempindex = 0;
			int i = 0;
			int taskCount;
			
		    for(String line; (line = br.readLine()) != null; ) {
		    	
		    	index = 0; tempindex = 0; taskCount = 0;
		    	//reads list name an creates a new list with that name, accordingly
		    	tempindex = line.indexOf(',');
		    	listname = line.substring(0, tempindex);
		    	if(i != 0 && i != 1) { //so long as it isnt the general or weekly lists which are already created
		    		lists.add(new ItemList(listname));
		    	}
		    	current = lists.get(i);
		    	
		    	//determines the number of tasks this list contains
		    	for(int n = 0; n < line.length(); n++) {
		    		if(line.charAt(n) == ',') {
		    			taskCount++;
		    		}
		    	}
		    	taskCount = taskCount / 5;
		    	
		    	//then parses out each task separately and adds them to the list
		    	for(int j = 0; j < taskCount; j++) {
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(',', index);
		    		current.add(new ListItem(line.substring(index, tempindex)));
		    		
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(',', index);
		    		if(line.substring(index, tempindex).equals("null"))
		    			current.get(j).changeDue("none");
		    		else
		    			current.get(j).changeDue(line.substring(index, tempindex));
		    		
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(',', index);
		    		if(line.substring(index, tempindex).equals("null"))
		    			current.get(j).changeDo("none");
		    		else
		    			current.get(j).changeDo(line.substring(index, tempindex));
		    		
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(',', index);
		    		int temp = Integer.parseInt(line.substring(index, tempindex));
		    		current.get(j).setImportance(temp);
		    		
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(',', index);
		    		temp = Integer.parseInt(line.substring(index, tempindex));
		    		current.get(j).setSize(temp);
		    		
		    		index = tempindex + 1;
		    		tempindex = line.indexOf(';', index);
		    		if(line.substring(index, tempindex).equals("true"))
		    			current.get(j).crossed = true;
		    	}
		    	i++; //tracks which line number it is on
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//loadSettings();
	}
	
	/**
	 * loads data off of settings file
	 */
	private static void loadSettings() {
		throw new UnsupportedOperationException("not yet implemented");
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
        			fr.append("Weekly,\n");
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
	 * clears all the save data - cannot undo, double check with user before calling this method
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
	
	public static boolean validateName(String name) {
		return true;
	}
	
}
