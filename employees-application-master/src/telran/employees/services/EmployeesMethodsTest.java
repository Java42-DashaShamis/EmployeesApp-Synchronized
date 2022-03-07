package telran.employees.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.employees.dto.*;
import java.util.*;
class EmployeesMethodsTest {
private static final long ID1 = 123;
private static final String NAME = "name";
private static final LocalDate BIRTHDATE1 = LocalDate.of(2000, 1, 1);
private static final int SALARY1 = 5000;
private static final String DEPARTMENT1 = "department1";
private static final long ID2 = 124;
private static final long ID3 = 125;
private static final long ID4 = 126;
private static final long ID5 = 127;
private static final long ID6 = 128;
private static final LocalDate BIRTHDATE2 = LocalDate.of(1997, 1, 1);
private static final LocalDate BIRTHDATE3 = LocalDate.of(1995, 1, 1);
private static final LocalDate BIRTHDATE4 = LocalDate.of(1970, 1, 1);
private static final LocalDate BIRTHDATE5 = LocalDate.of(1971, 1, 1);
private static final LocalDate BIRTHDATE6 = LocalDate.of(1980, 1, 1);
private static final String DEPARTMENT2 = "department2";
private static final int SALARY2 = 6000;
private static final int SALARY3 = 10000;
EmployeesMethods employees; 
Employee empl1 = new Employee(ID1, NAME, BIRTHDATE1, SALARY1, DEPARTMENT1);
Employee empl2 = new Employee(ID2, NAME, BIRTHDATE2, SALARY1, DEPARTMENT1);
Employee empl3 = new Employee(ID3, NAME, BIRTHDATE3, SALARY2, DEPARTMENT1);
Employee empl4 = new Employee(ID4, NAME, BIRTHDATE4, SALARY2, DEPARTMENT2);
Employee empl5 = new Employee(ID5, NAME, BIRTHDATE5, SALARY3, DEPARTMENT2);
Employee empl6 = new Employee(ID6, NAME, BIRTHDATE6, SALARY3, DEPARTMENT2);
List<Employee> employeesList = Arrays.asList(empl1,empl2,empl3,empl4,empl5,empl6);
	@BeforeEach
	void setUp() throws Exception {
		employees = new EmployeesMethodsMapsImpl(null);
		employeesList.forEach(employees::addEmployee);
		
	}

	@Test
	void testAddEmployee() {
		assertEquals(ReturnCode.EMPLOYEE_ALREADY_EXISTS, employees.addEmployee(empl1));
		assertEquals(ReturnCode.OK, 
				employees.addEmployee(new Employee(ID1 + 10000, NAME, BIRTHDATE1, SALARY1, DEPARTMENT1)));
	}

	@Test
	void testRemoveEmployee() {
		assertEquals(ReturnCode.OK, employees.removeEmployee(ID1));
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.removeEmployee(ID1));
		
		testBySalaryRun(new Employee[] {empl2, empl3, empl4}, SALARY1, SALARY2);
		testByAgeRun(new Employee[] {empl2, empl3}, BIRTHDATE3, BIRTHDATE1 );
	}

	private void testByAgeRun(Employee[] employeesAr, LocalDate birthdateFrom, LocalDate birthdateTo) {
		int ageFrom = getAge(birthdateTo);
		int ageTo = getAge(birthdateFrom);
		assertArrayEquals(employeesAr,
				fromIterableToArray(employees.getEmployeesByAge(ageFrom, ageTo)));
		
		
	}
	private void testByDepartmentRun(Employee[] employeesAr, String department) {
		assertArrayEquals(employeesAr, fromIterableToArray(employees.getEmployeesByDepartment(department)));
	}

	private int getAge(LocalDate birthdateTo) {
		
		return (int)ChronoUnit.YEARS.between(birthdateTo, LocalDate.now());
	}

	private void testBySalaryRun(Employee[] employeesAr, int salaryFrom, int salaryTo) {
		assertArrayEquals(employeesAr,
				fromIterableToArray(employees.getEmployeesBySalary(salaryFrom, salaryTo)));
		
	}

	

	@Test
	void testGetAllEmployees() {
		assertArrayEquals(new Employee[] {empl1, empl2, empl3, empl4, empl5, empl6},
				fromIterableToArray(employees.getAllEmployees()));
	}

	@Test
	void testGetEmployee() {
		assertEquals(empl1, employees.getEmployee(ID1));
		assertNull(employees.getEmployee(ID1 + 10000));
	}

	@Test
	void testGetEmployeesByAge() {
		testByAgeRun(new Employee[] {empl1, empl2, empl3}, BIRTHDATE3, BIRTHDATE1);
		testByAgeRun(new Employee[] {empl4, empl5, empl6}, BIRTHDATE4, BIRTHDATE6);
	}

	@Test
	void testGetEmployeesBySalary() {
		testBySalaryRun(new Employee[] {empl1, empl2, empl3,empl4}, SALARY1, SALARY2);
		testBySalaryRun(new Employee[] {empl5, empl6}, SALARY3, SALARY3);
	}

	@Test
	void testGetEmployeesByDepartment() {
		testByDepartmentRun(new Employee[] {empl1, empl2, empl3}, DEPARTMENT1);
	}

	@Test
	void testGetEmployeesByDepartmentAndSalary() {
		
		assertArrayEquals(new Employee[] {empl1, empl2, empl3},
				fromIterableToArray(employees.getEmployeesByDepartmentAndSalary(DEPARTMENT1, SALARY1, SALARY2)));
	}

	@Test
	void testUpdateSalary() {
		assertEquals(ReturnCode.OK, employees.updateSalary(ID4, SALARY3));
		testBySalaryRun(new Employee[] {empl1, empl2, empl3}, SALARY1, SALARY2);
		testBySalaryRun(new Employee[] {empl4, empl5, empl6}, SALARY3, SALARY3);
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.updateSalary(ID4 + 10000, SALARY3));
		assertEquals(ReturnCode.SALARY_NOT_UPDATED, employees.updateSalary(ID4, SALARY3));
	}

	@Test
	void testUpdateDepartment() {
		assertEquals(ReturnCode.OK, employees.updateDepartment(ID1, DEPARTMENT2));
		testByDepartmentRun(new Employee[] {empl2, empl3}, DEPARTMENT1);
		testByDepartmentRun(new Employee[] {empl1, empl4, empl5, empl6}, DEPARTMENT2);
		assertEquals(ReturnCode.DEPARTMENT_NOT_UPDATED, employees.updateDepartment(ID1, DEPARTMENT2));
		assertEquals(ReturnCode.EMPLOYEE_NOT_FOUND, employees.updateDepartment(ID1 + 10000, DEPARTMENT2));
	}
	private Employee[] fromIterableToArray(Iterable<Employee> employeesIt) {
		List<Employee> listRes = new ArrayList<>();
		employeesIt.forEach(listRes::add);
		listRes.sort((e1, e2) -> Long.compare(e1.id, e2.id));
		return listRes.toArray(Employee[]::new);
		
		
	}

}
