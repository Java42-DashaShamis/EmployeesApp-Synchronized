package telran.employees.controller;

import java.time.LocalDate;
import java.util.*;
import java.io.*;
import telran.employees.dto.Employee;
import telran.employees.services.EmployeesMethods;
import telran.view.*;
public class EmployeeActions {
private static final int MIN_SALARY = 5000;
private static final int MAX_SALARY = 40000;
private static final int MIN_AGE = 18;
private static final int MAX_AGE = 70;
private static EmployeesMethods employees;
private static ArrayList<Item> items;
private static Set<String> departments;
private EmployeeActions() {
	
}
public static ArrayList<Item> getActionItems(EmployeesMethods employees, Set<String> departments) {
	EmployeeActions.employees = employees;
	EmployeeActions.departments = departments;
	if (items == null) {
		items = new ArrayList<>(Arrays.asList(new Item[] {
			Item.of("Hiring new Employee", EmployeeActions::hireEmployee)	,
			Item.of("Firing Employee ", EmployeeActions::fireEmployee )	,
			Item.of("Updating salary", EmployeeActions::updateSalary)	,
			Item.of("Updating department", EmployeeActions::updateDepartment)	,
			Item.of("Display Employee Info", EmployeeActions::displayEmployee)	,
			Item.of("Display Employees Info filtered by Age", EmployeeActions::displayEmployeesAge)	,
			Item.of("Display Employees Info filtered by Salary", EmployeeActions::displayEmployeesSalary)	,
			Item.of("Display Employees Info of Department", EmployeeActions::displayEmployeesDepartment)	,
			Item.of("Display Employees Info filtered by Salary and Department", EmployeeActions::displayEmployeesSalaryDepartment)	,
			Item.of("Display Employees Info", EmployeeActions::displayEmployees),
			Item.of("Exit", EmployeeActions::exit, true) 
			
		})) ;
		
	}
	return items;
}
private static void exit(InputOutput io) {
	try {
		((Closeable)employees).close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
private static void hireEmployee(InputOutput io) {
	long id = getId(io, true);
	String name = getName(io);
	
	LocalDate birthdate = getBirthDate(io);
	int salary = getSalary(io);
	String department = getDepartment(io);
	Employee empl = new Employee(id, name, birthdate , salary , department );
	io.writeObjectLine(employees.addEmployee(empl));
}
private static String getDepartment(InputOutput io) {
	
	return io.readStringOption("Enter department " + departments, departments);
}
private static int getSalary(InputOutput io) {
	
	return io.readInt("Enter salary", MIN_SALARY, MAX_SALARY);
}
private static LocalDate getBirthDate(InputOutput io) {
	
	return io.readDate("Enter birthdate in the yyyy-MM-dd format");
}
private static String getName(InputOutput io) {
	
	return  io.readStringPredicate("Enter name", "Name may contain only letters with first capital",
			str -> str.matches("[A-Z][a-z]+"));
}
private static long getId(InputOutput io, boolean flHire) {
	long idRes = Long.parseLong(io.readStringPredicate("Enter Employee's ID", "Wrong ID input", str -> str.matches("\\d{9}")));
	Employee empl = employees.getEmployee(idRes);
	if(empl != null && flHire) {
		throw new RuntimeException(String.format("Employee with id %d already exists", idRes));
	}
	if(empl == null && !flHire) {
		throw new RuntimeException(String.format("Employee with id %d not found", idRes));
	}
	return idRes;
}
private static void fireEmployee(InputOutput io) {
	long id = getId(io, false);
	io.writeObjectLine(employees.removeEmployee(id));
}
private static void updateSalary(InputOutput io) {
	long id = getId(io, false);
	int salary = getSalary(io);
	io.writeObjectLine(employees.updateSalary(id, salary));
}
private static void updateDepartment(InputOutput io) {
	long id = getId(io, false);
	String department = getDepartment(io);
	io.writeObjectLine(employees.updateDepartment(id, department));
}
private static void displayEmployee(InputOutput io) {
	long id = getId(io, false);
	Employee empl = employees.getEmployee(id);
	io.writeObjectLine(empl);
}
private static void displayEmployeesAge(InputOutput io) {
	int fromTo[] = getFromTo(io, "Age", MIN_AGE, MAX_AGE);
	employeesOutput(io, employees.getEmployeesByAge(fromTo[0], fromTo[1]));
}
private static void employeesOutput(InputOutput io, Iterable<Employee> employeesIterable) {
	boolean isNotEmpty = employeesIterable.iterator().hasNext();
	if (!isNotEmpty) {
		io.writeObjectLine("No Employees found");
	} else {
		employeesIterable.forEach(io::writeObjectLine);
	}
	
	
}
private static int[] getFromTo(InputOutput io, String type, int min, int max) {
	int from = io.readInt(String.format("Enter %s value from", type), min, max);
	int to = io.readInt(String.format("Enter %s value to", type), from + 1, max);
	return new int[] {from, to};
}
private static void displayEmployeesSalary(InputOutput io) {
	int fromTo[] = getFromTo(io, "Salary", MIN_SALARY, MAX_SALARY);
	employeesOutput(io, employees.getEmployeesBySalary(fromTo[0], fromTo[1]));
}
private static void displayEmployeesDepartment(InputOutput io) {
	String department = getDepartment(io);
	employeesOutput(io, employees.getEmployeesByDepartment(department));
}
private static void displayEmployeesSalaryDepartment(InputOutput io) {
	String department = getDepartment(io);
	int fromTo[] = getFromTo(io, "salary", MIN_SALARY, MAX_SALARY);
	employeesOutput(io, employees.getEmployeesByDepartmentAndSalary(department, fromTo[0], fromTo[1]));
}
private static void displayEmployees(InputOutput io) {
	employeesOutput(io, employees.getAllEmployees());
}
}
