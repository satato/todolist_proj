package sorting;

import java.util.Comparator;

import app.ListItem;

public class Sortbypriority implements Comparator<ListItem>{

	/**
	 * Compares two ListItem objects by priority level
	 * @param a
	 * @param b
	 * @return zero if they're equal, negative if a is less than b, positive if b is less than a
	 */
	@Override
	public int compare(ListItem a, ListItem b) {
		double aPri = a.getPriority();
		double bPri = b.getPriority();
		
		if(aPri < bPri)
			return -1;
		else if(aPri > bPri)
			return 1;
		else
			return 0;
	}

}
