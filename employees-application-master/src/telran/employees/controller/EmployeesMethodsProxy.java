package telran.employees.controller;

import telran.employees.dto.Employee;
import telran.employees.dto.ReturnCode;
import telran.employees.services.EmployeesMethods;
import telran.net.Sender;
import static telran.employees.net.dto.ApiConstants.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
public class EmployeesMethodsProxy implements EmployeesMethods, Closeable {
private Sender sender;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeesMethodsProxy(Sender sender) {
		this.sender = sender;
	}

	@Override
	public ReturnCode addEmployee(Employee empl) {
		
		return sender.send(ADD_EMPLOYEE, empl);
	}

	@Override
	public ReturnCode removeEmployee(long id) {
		
		return sender.send(REMOVE_EMPLOYEE, id);
	}

	@Override
	public Iterable<Employee> getAllEmployees() {
		
		return sender.send(GET_EMPLOYEES, "");
	}

	@Override
	public Employee getEmployee(long id) {
		
		return sender.send(GET_EMPLOYEE, id);
	}

	@Override
	public Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		Integer[] fromTo = {ageFrom, ageTo};
		return sender.send(GET_EMPLOYEES_AGE, fromTo);
	}

	@Override
	public Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		Integer[] fromTo = {salaryFrom, salaryTo};
		return sender.send(GET_EMPLOYEES_SALARY, fromTo);
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartment(String department) {
		
		return sender.send(GET_EMPLOYEES_DEPARTMENT, department);
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartmentAndSalary(String department, int salaryFrom, int salaryTo) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(DEPARTMENT, department);
		Integer[] fromTo = {salaryFrom, salaryTo};
		map.put(FROM_TO, fromTo);
		return sender.send(GET_EMPLOYEES_DEPARTMENT_SALARY, map);
	}

	@Override
	public ReturnCode updateSalary(long id, int newSalary) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(ID, id);
		
		map.put(SALARY, newSalary);
		return sender.send(UPDATE_SALARY, map);
	}

	@Override
	public ReturnCode updateDepartment(long id, String newDepartment) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(ID, id);
		
		map.put(DEPARTMENT, newDepartment);
		return sender.send(UPDATE_DEPARTMENT, map);
	}

	@Override
	public void restore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		sender.close();
		
	}

}
