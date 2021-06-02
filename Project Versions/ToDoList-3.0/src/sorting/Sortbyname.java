package sorting;

import java.time.LocalDate;
import java.util.Comparator;

import app.ListItem;

public class Sortbyname  implements Comparator<ListItem>{

	@Override
	public int compare(ListItem a, ListItem b) {
		return (a.getName()).compareTo(b.getName());
	}

}
