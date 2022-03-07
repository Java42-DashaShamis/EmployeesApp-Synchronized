package telran.view;

import java.util.Scanner;

public class ConsoleInputOutput implements InputOutput {
private Scanner scanner = new Scanner(System.in);
	@Override
	public String readString(String prompt) {
		writeObjectLine(prompt);
		
		return scanner.nextLine();
	}

	@Override
	public void writeObject(Object obj) {
		System.out.print(obj.toString());

	}

}
