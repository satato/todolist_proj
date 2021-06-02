package sorting;

import java.time.LocalDate;
import java.util.Comparator;

import app.ListItem;

public class Sortbydodate  implements Comparator<ListItem>{

	@Override
	public int compare(ListItem a, ListItem b) {

		LocalDate aDate = a.getDo();
		LocalDate bDate = a.getDo();
		
		return aDate.compareTo(bDate);
	}

}
