package telran.numbers.controller;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import telran.numbers.net.CalculatorProxy;
import telran.numbers.service.*;
import telran.view.*;
public class CalculatorApp {

	private static final String HOST = "localhost";
	private static final int PORT = 2000;
static Calculator calculator;
	public static void main(String[] args) {
		InputOutput io = new ConsoleInputOutput();
		 
		try {
			calculator = new CalculatorProxy(HOST, PORT);
		} catch (Exception e) {
			io.writeObjectLine(e.getMessage());
		}
		ArrayList<Item> items = CalculatorActions.getCalculatorItems(calculator);
		
		Menu menu = new Menu("Calculator menu", items);
		menu.perform(io);

	}
	

}
