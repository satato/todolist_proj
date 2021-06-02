package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.Date;

import javax.swing.*;
import app.*;
import app.Driver;
import layout.SpringUtilities;
import java.sql.*;
import java.text.*;
import java.time.LocalDate;
import java.io.*;

public class DisplayUnit extends JFrame{
	
	private static JFrame frame;
	private static JMenuBar menu = new JMenuBar();
	private static JMenu option1 = new JMenu("Lists");
	private static JMenu option2 = new JMenu("Edit");
	public static JPanel mainPanel = new JPanel(new SpringLayout());
	private static JLabel listLabel = new JLabel("", JLabel.LEADING);
	
	//menu items
	private static JMenuItem edititem1 = new JMenuItem("New List");
	private static JMenuItem edititem2 = new JMenuItem("New Task");
	private static JMenuItem renameList = new JMenuItem("Rename List");
	private static JMenuItem deleteList = new JMenuItem("Delete List");
	private static JMenuItem editDue = new JMenuItem("Edit Due Date");
	private static JMenuItem editDo = new JMenuItem("Edit Do Date");
	private static JMenuItem editImportance = new JMenuItem("Edit Importance Level");
	private static JMenuItem editSize = new JMenuItem("Edit Size/Workload");
	private static JMenuItem deleteTask = new JMenuItem("Delete Task");
	
	//list management
	public static ItemList currentList = Driver.lists.get(0);
	private static ArrayList<String> listNames = new ArrayList<>();
	
	/**
	 * Constructor
	 * Develops the general display
	 * @return 
	 */
	public static void display() {
		//creates general 400x600px window
		frame = new JFrame("To-Do");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 600));
		
		//builds the general menu tabs
		buildMenu();
		
		//prepares layout before developing the list display
		JPanel titlePanel = new JPanel();
		JPanel outerListPanel = new JPanel();
		JPanel innerListPanel = new JPanel();
		outerListPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		innerListPanel.setLayout(new BoxLayout(innerListPanel, BoxLayout.Y_AXIS));
		titlePanel.add(listLabel);
		titlePanel.setPreferredSize(new Dimension(100, 20));
		outerListPanel.setPreferredSize(new Dimension(100, 400));
		outerListPanel.add(innerListPanel);
		mainPanel.add(titlePanel);
		mainPanel.add(outerListPanel);
		
		SpringUtilities.makeCompactGrid(mainPanel,
				2, 1,
				0, 0,
				0, 0);
		
		//updates list display accordingly
		updateListDisplay();
		
		
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Build's the general menu available on any instance of the window
	 * @param frame
	 */
	private static void buildMenu() {
		//handles menu items
		menu.add(option1); menu.add(option2);
		
		//add action listeners
		edititem1.addActionListener(new MenuActionListener());
		edititem2.addActionListener(new MenuActionListener());
		renameList.addActionListener(new MenuActionListener());
		deleteList.addActionListener(new MenuActionListener());
		editDue.addActionListener(new MenuActionListener());
		editDo.addActionListener(new MenuActionListener());
		editImportance.addActionListener(new MenuActionListener());
		editSize.addActionListener(new MenuActionListener());
		deleteTask.addActionListener(new MenuActionListener());
		
		updateMenu();
		
		JMenu listMenu = new JMenu("Edit List");
		listMenu.add(renameList);
		listMenu.add(deleteList);
		JMenu taskMenu = new JMenu("Edit Task");
		taskMenu.add(editDue);
		taskMenu.add(editDo);
		taskMenu.add(editImportance);
		taskMenu.add(editSize);
		taskMenu.add(deleteTask);
		
		option2.add(edititem1); option2.add(edititem2); option2.add(listMenu); option2.add(taskMenu);
		
		frame.getContentPane().add(BorderLayout.NORTH, menu);
		
		//adds a scroll bar
		JScrollBar scroller = new JScrollBar();  
	    scroller.setBounds(475, 0, 25,500);  
	    frame.getContentPane().add(BorderLayout.EAST, scroller);
	}
	
	/**
	 * Update's the menu options (typically called after a list edit or creation is performed)
	 */
	private static void updateMenu() {
		ArrayList<ItemList> lists = Driver.lists;
		//first clears the lists shown
		for(int i = option1.getMenuComponentCount() - 1; i >= 0; i--) {
			option1.remove(i);
			listNames.remove(i);
		}
		
		//then refills with lists
		for(int i = 0; i < lists.size(); i++) {
			JMenuItem temp = new JMenuItem(lists.get(i).getName());
			temp.addActionListener(new MenuActionListener());
			option1.add(temp);
			listNames.add(lists.get(i).getName());
		}
		Utilities.save();
	}
	
	/**
	 * Updates the list-display
	 */
	private static void updateListDisplay() {
		
		//updates label text to match the current list
		listLabel.setText(currentList.getName());
		
		//sets up task list & checkboxes
		int length = currentList.getList().size();
		JPanel outerPanel = (JPanel) mainPanel.getComponent(1);
		JPanel listPanel = (JPanel) outerPanel.getComponent(0);
		
		listPanel.removeAll();
		
		String data = "";
		ListItem task;
		if(currentList.getList().size() > 0) {
			if(currentList.getName().equals("Weekly")) {
				JTabbedPane days = new JTabbedPane(JTabbedPane.TOP);
				days.setPreferredSize(new Dimension(450, 560));
				
				JLabel tabs = new JLabel(currentList.get(6).getName());
				tabs.setPreferredSize(new Dimension(40, 30));
				JPanel temp;
				//creates a tab for each day of the week
				for(int i = 0; i < length; i++) {
					temp = new JPanel();
					temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
					//temp.setLayout(new FlowLayout(FlowLayout.LEADING));
					Day day = (Day) currentList.get(i);
					
					//populates the day's tab with its tasks
					for(int n = 0; n < day.getList().size(); n++) {
						data = "";
						//defines the current task item and fills its data for the tooltip before adding it to the panel
						task  = (ListItem) day.getList().get(n);
						data += "<html>Due Date: " + task.getDueDateAsString() + "<br>Do Date: " + 
								task.getDoDateAsString() + "<br>Importance: " + 
								task.getImportance() + "<br>Workload: " + task.getSize() +"</html";
						
						//creation of tasks within the currently selected day
						JCheckBox box = new JCheckBox(day.getList().get(n).getName());
						box.setToolTipText(data);
						box.addActionListener(new ListActionListener());
						if(task.crossed)
							box.setSelected(true);
						
						//adds the selected day's tasks to its panel
						temp.add(box);
					}
					//adds each day tab to the tabbed pane
					days.addTab(currentList.get(i).getName(), temp);
					days.setTabComponentAt(i, tabs);
				}
				//adds the tabbed pane to the panel for visibility
				listPanel.add(days);
			}
			else {
				Item item = currentList.get(0);
				task  = (ListItem) item;
				for(int i = 0; i < length; i++) {
					data = "";
					item = currentList.get(i);
					if(item instanceof ListItem) {
						//defines the current task item and fills its data for the tooltip before adding it to the panel
						task = (ListItem) item;
						data += "<html>Due Date: " + task.getDueDateAsString() + "<br>Do Date: " + 
								task.getDoDateAsString() + "<br>Importance: " + 
								task.getImportance() + "<br>Workload: " + task.getSize() +"</html";
					}
					JCheckBox temp = new JCheckBox(currentList.get(i).getName());
					temp.setToolTipText(data);
					temp.addActionListener(new ListActionListener());
					if(task.crossed)
						temp.setSelected(true);
					listPanel.add(temp);
				}
			}
		}
		Utilities.save();
		mainPanel.paintComponents(listPanel.getGraphics());
	}
	
	/**
	 * Builds the task-editor display, takes an integer as a parameter
	 * to determine what action is taking place.
	 * @param action
	 */
	public static void buildTaskEditor(int action) {
		
		//first checks that a task is selected, then acts accordingly
		if(ListActionListener.selectedItem == null) {
			JOptionPane.showMessageDialog(frame, "You have to select a task first.");
		}
		else {
			//double checks this is what the user wants
			int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to perform this action?");
			if(result == JOptionPane.YES_OPTION) {
				switch (action) {
				case 0://change due date
					changeDue();
					break;
				case 1://change do date
					changeDo();
					break;
				case 2://change importance level
					ListActionListener.selectedItem.setImportance(setRanks(0));
					break;
				case 3://change size/workload
					ListActionListener.selectedItem.setImportance(setRanks(1));
					break;
				case 4://delete task
					deleteTask();
					JOptionPane.showMessageDialog(frame, "Task Deleted");
					break;
				}
				Driver.prepareWeek();
				changeCurrentList("General"); // also updates list display
			}
			//otherwise nothing happens
		}
	}
	
	/**
	 * deletes the currently selected task
	 */
	private static void deleteTask() {
		for(int i = 0; i < Driver.lists.size(); i++) {
			for(int n = 0; n < Driver.lists.get(i).getList().size(); n++) {
				if(Driver.lists.get(i).getList().get(n).id == ListActionListener.selectedItem.id) {
					Driver.lists.get(i).getList().remove(n);
					break;
				}
			}
		}
	}
	
	/**
	 * prompts the user for input and sets either a task's importance level or workload/size appropriately
	 * @param action
	 * @return
	 */
	private static int setRanks(int action) {
		String response;
		boolean validResponse = false;
		do {
			if(action == 0)
				response = JOptionPane.showInputDialog(frame, "Please rank this task's importance on a scale from 1-5, with 5 being most important.");
			else
				response = JOptionPane.showInputDialog(frame, "Please rank this task's workload on a scale from 1-5, with 5 being 12+ hours of work, and 1 being less than 2 hours of work.");
			if(response.length() != 1)
				JOptionPane.showMessageDialog(frame, "Invalid Response");
			else {
				try {
					int i = Integer.parseInt(response);
					if(i <= 5 && i >= 1)
						validResponse = true;
					else
						JOptionPane.showMessageDialog(frame, "Invalid Response");
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(frame, "Invalid Response");
				}
			}
		} while(!validResponse);
		return Integer.parseInt(response);
	}
	
	/**
	 * private helper method for allowing user to input a date and checking its validity
	 * @return
	 */
	private static int[] dateSelector() {
		
		int[] result = new int[3];
		boolean validDate = false;
		
		do {
			String response = JOptionPane.showInputDialog(frame, "Please type a date in the form: mm/dd/yyyy, or type 'none'");
			if(response.length() != 10 && !response.equals("none")) {
				JOptionPane.showMessageDialog(frame, "Invalid Date Provided");
			}
			else if(response.equals("none")) {
				validDate = true;
			}
			else if(response.charAt(2) != '/' || response.charAt(5) != '/') {
				JOptionPane.showMessageDialog(frame, "Invalid Date Provided");
			}
			else {
				try {
					result[0] = Integer.parseInt(response.substring(0, 2));
					result[1] = Integer.parseInt(response.substring(3, 5));
					result[2] = Integer.parseInt(response.substring(6));
					LocalDate date = LocalDate.of(result[2],result[0],result[1]);
					validDate = true;
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(frame, "Invalid Date Provided");
				}
			}
		} while(!validDate);
		
		return result;
	}
	
	/**
	 * changes the selected task's due date
	 */
	private static void changeDue() {
		int[] due = new int[3];
		due = dateSelector();
		ListActionListener.selectedItem.changeDueDate(Utilities.rebuildDate(due));
	}
	
	/**
	 * changes the selected task's 'do' date
	 */
	private static void changeDo() {
		int[] doDate = new int[3];
		doDate = dateSelector();
		ListActionListener.selectedItem.changeDueDate(Utilities.rebuildDate(doDate));
	}
	
	/**
	 * private helper method for checking task name validity
	 * new task name cannot be null, contain the | character, or be that of a pre-existing task
	 * @param name
	 * @return
	 */
	private static boolean validateTaskName(String name) {
		if(name == null || name.contains("|") || Driver.invalidRenames.contains(name))
			return false;
		else {
			for(int i = 0; i < Driver.lists.get(0).getList().size(); i++) {
				if(Driver.lists.get(0).get(i).getName().equals(name))
					return false;
			}
			return true;
		}
	}
	
	/**
	 * Builds the task-creator display
	 * @param task
	 */
	public static void buildTaskCreator() {
		String response = JOptionPane.showInputDialog(frame, "Please provide a name for this task:");
		if(validateTaskName(response)) {
			ListItem item = new ListItem(response);
			JOptionPane.showMessageDialog(frame, "Next please label its due date");
			int[] due = dateSelector();
			item.changeDueDate(Utilities.rebuildDate(due));
			JOptionPane.showMessageDialog(frame, "Next please label its 'do' date");
			int[] doDate = dateSelector();
			item.changeDoDate(Utilities.rebuildDate(doDate));
			item.setImportance(setRanks(0));
			item.setSize(setRanks(1));
			
			JOptionPane.showMessageDialog(frame, "Task Created.");
			String[] choices = new String[Driver.lists.size()];
			for(int i = 0; i < Driver.lists.size(); i++) {
				choices[i] = Driver.lists.get(i).getName();
			}
			response = (String) JOptionPane.showInputDialog(null, "Please select a list to add this task to:", "Task Creation", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
			for(int i = 0; i < Driver.lists.size(); i++) {
				if(Driver.lists.get(i).getName().equals(response)) {
					Driver.lists.get(i).add(item);
					break;
				}
			}
			if(!response.equals("General")) {
				Driver.lists.get(0).add(item);
			}
			Driver.prepareWeek();
			updateListDisplay();
		}
		else {
			JOptionPane.showMessageDialog(frame, "Invalid Task Name. This task may already exist, or you may have included an illegal character. Task names may not include '|'");
		}
	}
	
	/**
	 * private helper method for checking if a new list name is valid
	 * @param name
	 * @return
	 */
	private static boolean validateListName(String name) {
		if(listNames.contains(name) || Driver.invalidRenames.contains(name))
			return false;
		else {
			if(name.contains("-"))
				return false;
			else
				return true;
		}
	}
	
	/**
	 * Produces the list-edit options for the selected list
	 * the "action" parameter takes an integer of either 0 or 1
	 * where 0 means to rename the current list, and 1 means to delete it.
	 * @param list
	 * @param action
	 */
	public static void listEdit(ItemList list, int action) {
		if(action == 0) {
			if(list.id == 0 || list.id == 1) {
				JOptionPane.showMessageDialog(frame, "This list cannot be renamed.");
			}
			else {
				//first confirms that the user wants to rename the current list
				int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to rename: " + list.getName() + "?", "Please Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(result == JOptionPane.YES_OPTION) {
					//prompts to rename the list
					String response = JOptionPane.showInputDialog("Please provide a new name for this list:");
					if(validateListName(response) && list.rename(response)) {
						JOptionPane.showMessageDialog(frame, "Successfully Renamed List");
						updateListDisplay();
						updateMenu();
					}
					else {
						JOptionPane.showMessageDialog(frame, "Rename failed. This list name may already exist or you may have included and illegal character. Please try again.");
					}
				}
			}
		}
		else {
			//attempts to delete the list in question and either confirms or notifies this is not possible
			if(list.id == 0 || list.id == 1)
				JOptionPane.showMessageDialog(frame, "This list cannot be deleted.");
			else {
				//confirms that the user wants to delete the list in question
				int result = JOptionPane.showConfirmDialog(frame, ("Are you sure you want to delete: " + list.getName() + "?"), "Please Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				
				if(result == JOptionPane.YES_OPTION) {
					Driver.deleteList(list);
					changeCurrentList("General");
					updateMenu();
				}
			}
		}
	}
	
	/**
	 * Produces the textbox and prompt for user to submit a new list name
	 */
	public static void createListPrompt() {
		//double check that the user does in fact wish to create a new list
		int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to make a new list?", "Please Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(result == JOptionPane.YES_OPTION) {
			//prompts for list name
			String response = JOptionPane.showInputDialog(frame, "Please provide a name for this list:");
			if(validateListName(response)) {
				Driver.createList(response);
				updateMenu();
			}
			else {
				JOptionPane.showMessageDialog(frame, "Invalid List Name. This list may already exist, or you may have included illegal characters. List names cannot contain '-'.");
			}
		}
		//otherwise does nothing
	}
	
	/**
	 * Changes the current list to that with the given name
	 * @param name
	 */
	public static void changeCurrentList(String name) {
		for(int i = 0; i < Driver.lists.size(); i++) {
			if(Driver.lists.get(i).getName().equals(name)) {
				currentList = Driver.lists.get(i);
				break;
			}
		}
		updateListDisplay();
	}
	
}

/**
 * ActionListener that listens for tasks being clicked in the list display and updates them and save data accordingly
 * @author amber
 */
class ListActionListener implements ActionListener {
	public static ListItem selectedItem;
	public void actionPerformed(ActionEvent e) {
		String happened = e.getActionCommand();
		ArrayList<ListItem> list = Driver.lists.get(0).getList();
		
		//goes through list items to determine which item was clicked
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).getName().equals(happened)) {
				selectedItem = DisplayUnit.currentList.getList().get(i);
				JPanel outerPanel = (JPanel) DisplayUnit.mainPanel.getComponent(1);
				JPanel listPanel = (JPanel) outerPanel.getComponent(0);
				JCheckBox check;
				
				for(int n = 0; n < listPanel.getComponentCount(); n++) {
					check = (JCheckBox) listPanel.getComponent(n);
					if(check.getText().equals(happened)) {
						if(selectedItem.crossed) {
							selectedItem.crossed = false;
							check.setSelected(false);
						}
						else {
							selectedItem.crossed = true;
							check.setSelected(true);
						}
						Utilities.save();
						break;
					}
				}
			}
		}
	}
}

/**
 * ActionListener that listens for a menu selection and performs the appropriate action
 * @author amber
 */
class MenuActionListener implements ActionListener {
	  public void actionPerformed(ActionEvent e) {
		  String happened = e.getActionCommand();
		  switch (happened) {
		  case "New List":
			  DisplayUnit.createListPrompt();
			  break;
		  case "New Task":
			  DisplayUnit.buildTaskCreator();
			  break;
		  case "Rename List":
			  DisplayUnit.listEdit(DisplayUnit.currentList, 0);
			  break;
		  case "Delete List":
			  DisplayUnit.listEdit(DisplayUnit.currentList, 1);
			  break;
		  case "Edit Due Date":
			  DisplayUnit.buildTaskEditor(0);
			  break;
		  case "Edit Do Date":
			  DisplayUnit.buildTaskEditor(1);
			  break;
		  case "Edit Importance Level":
			  DisplayUnit.buildTaskEditor(2);
			  break;
		  case "Edit Size/Workload":
			  DisplayUnit.buildTaskEditor(3);
			  break;
		  case "Delete Task":
			  DisplayUnit.buildTaskEditor(4);
			  break;
		  default:
			  DisplayUnit.changeCurrentList(happened);
			  break;
		  }
		  
	  }
	}

