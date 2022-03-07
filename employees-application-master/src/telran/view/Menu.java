package telran.view;

import java.util.*;
import java.util.stream.IntStream;

public class Menu implements Item {
private String name;
private ArrayList<Item> items;
public Menu(String name, ArrayList<Item> items) {
	this.name = name;
	this.items = items;
}
public Menu(String name, Item ...items ) {
	this(name, new ArrayList<>(Arrays.asList(items)));
}
	@Override
	public String displayName() {
		
		return name;
	}

	@Override
	public void perform(InputOutput io) {
		displayTitle(io);
		while(true) {
			displayItems(io);
			int nItem = io.readInt("Enter item number", 1, items.size());
			Item item = items.get(nItem - 1);
			try {
				item.perform(io);
				if (item.isExit()) {
					break;
				}
			} catch (Throwable e) {
				io.writeObjectLine(e.getMessage());
			}
		}
		io.writeObjectLine("bye & thanks for using application");

	}

	private void displayItems(InputOutput io) {
		IntStream.range(0, items.size()).forEach(i -> io.writeObjectLine(String.format("%d. %s", i + 1, items.get(i).displayName())));
		
	}
	private void displayTitle(InputOutput io) {
		io.writeObjectLine("_".repeat(20));
		io.writeObjectLine(name);
		io.writeObjectLine("_".repeat(20));
		
	}
	@Override
	public boolean isExit() {
		
		return false;
	}

}
