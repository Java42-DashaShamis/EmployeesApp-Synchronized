package telran.employees.controller;
import java.util.*;
import java.util.stream.Collectors;

import telran.employees.services.*;
import telran.net.Sender;
import telran.net.TcpSender;
import telran.view.*;
import java.io.*;
public class EmployeesAppl {
private static final String DEFAULT_FILE_NAME = "employees.data";
private static final Object DATA_FILE_PROPERTY = null;
static Map<String, String> properties;
static InputOutput io = new ConsoleInputOutput();
	public static void main(String[] args) {
		
		Sender sender = null;
		try {
			sender = new TcpSender("localhost", 2000);
		} catch (Exception e) {
			io.writeObjectLine(e.toString());
		}
		EmployeesMethods employeesMethods = new EmployeesMethodsProxy(sender);
		employeesMethods.restore();
		HashSet<String> departments = new HashSet<>(Arrays.asList("QA", "Development", "HR", "Management"));
		Menu menu = new Menu("Employees Application", EmployeeActions.getActionItems(employeesMethods, departments));
		menu.perform(io);

	}

	private static String getFileName(String configFile) {
		
		if(properties == null) {
			
			try(BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
				properties = reader.lines().map(str -> str.split("[ =:]+"))
						.collect(Collectors.toMap(tokens -> tokens[0], tokens -> tokens[1]));
				
			} catch (Exception e) {
				io.writeObjectLine(e.getMessage());
				return null;
			}
		}
		return properties.getOrDefault(DATA_FILE_PROPERTY, DEFAULT_FILE_NAME);
	}

}
