package telran.numbers.controller;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

import telran.numbers.service.Calculator;
import telran.numbers.service.CalculatorImpl;
import telran.view.InputOutput;
import telran.view.Item;

public class CalculatorActions {
private static Calculator calculator;
private CalculatorActions() {
	
}
static public ArrayList<Item> getCalculatorItems(Calculator calculator) {
	CalculatorActions.calculator = calculator;
	ArrayList<Item> items = new ArrayList<>();
	items.add(Item.of("Add two numbers", CalculatorActions::add));
	items.add(Item.of("Subtract two numbers", CalculatorActions::subtract));
	items.add(Item.of("Multiply two numbers", CalculatorActions::multiply));
	items.add(Item.of("Divide two numbers", CalculatorActions::divide));
	items.add(Item.of("Exit", CalculatorActions::exitItem, false));
	
	return items;
}
static private double[] enterTwoNumbers(InputOutput io) {
	return new double[] {
			io.readDouble("Enter first number"),
			io.readDouble("Enter second number")
	};
}
static private void add(InputOutput io) {
	double[] numbers = enterTwoNumbers(io);
	io.writeObjectLine(calculator.compute("+", numbers[0], numbers[1]));
}
static private void subtract(InputOutput io) {
	double[] numbers = enterTwoNumbers(io);
	io.writeObjectLine(calculator.compute("-", numbers[0], numbers[1]));
}
static private void multiply(InputOutput io) {
	double[] numbers = enterTwoNumbers(io);
	io.writeObjectLine(calculator.compute("*", numbers[0], numbers[1]));
}
static private void divide(InputOutput io) {
	double[] numbers = enterTwoNumbers(io);
	io.writeObjectLine(calculator.compute("/", numbers[0], numbers[1]));
}
static void exitItem (InputOutput io) {
	if (calculator instanceof Closeable) {
		try {
			((Closeable)calculator).close();
		} catch (IOException e) {
			io.writeObjectLine(e.getMessage());
		}
	}
}
}
