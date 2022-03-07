package telran.employees.services;

import telran.employees.dto.Employee;

import telran.employees.dto.ReturnCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.stream.*;
import java.io.*;
public class EmployeesMethodsMapsImpl implements EmployeesMethods {
	
	private static final long serialVersionUID = 1L;
	public EmployeesMethodsMapsImpl(String fileName) {
		this.fileName = fileName;
	}

	private transient String fileName; //field won't be serialized
 private HashMap<Long, Employee> mapEmployees = new HashMap<>(); //key employee's id, value - employee
 private static ReadWriteLock lock1 = new ReentrantReadWriteLock();
 private static Lock readLockGeneral = lock1.readLock();
 private static Lock writeLockGeneral = lock1.writeLock();
 private TreeMap<Integer, List<Employee>> employeesAge= new TreeMap<>(); //key - age, value - list of employees with the same age
 private static ReadWriteLock lock2 = new ReentrantReadWriteLock();
 private static Lock readLockAge = lock2.readLock();
 private static Lock writeLockAge = lock2.writeLock();
 private TreeMap<Integer, List<Employee>> employeesSalary = new TreeMap<>(); //key - salary,
 //value - list of employees with the same salary
 private static ReadWriteLock lock3 = new ReentrantReadWriteLock();
 private static Lock readLockSalary = lock3.readLock();
 private static Lock writeLockSalary = lock3.writeLock();
 private HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
 private static ReadWriteLock lock4 = new ReentrantReadWriteLock();
 private static Lock readLockDepartment = lock4.readLock();
 private static Lock writeLockDepartment = lock4.writeLock();
	@Override
	public ReturnCode addEmployee(Employee empl) {
		boolean fl;
		try {
			readLockGeneral.lock();
			fl = mapEmployees.containsKey(empl.id);
		} finally {
			readLockGeneral.unlock();
		}
		if (fl) {
			return ReturnCode.EMPLOYEE_ALREADY_EXISTS;
		}
		Employee emplS = copyOneEmployee(empl);
		
		try {
			writeLockGeneral.lock();
			mapEmployees.put(emplS.id, emplS);
		} finally {
			writeLockGeneral.unlock();
		}
		
		try {
			writeLockAge.lock();
			employeesAge.computeIfAbsent(getAge(emplS), k -> new LinkedList<Employee>()).add(emplS);
		} finally {
			writeLockAge.unlock();
		}
		
		try {
			writeLockSalary.lock();
			employeesSalary.computeIfAbsent(emplS.salary, k -> new LinkedList<Employee>()).add(emplS);
		} finally {
			writeLockSalary.unlock();
		}
		
		try {
			writeLockDepartment.lock();
			employeesDepartment.computeIfAbsent(emplS.department, k -> new LinkedList<Employee>()).add(emplS);
		} finally {
			writeLockDepartment.unlock();
		}
		return ReturnCode.OK;
	}

	private Integer getAge(Employee emplS) {
		
		return (int)ChronoUnit.YEARS.between(emplS.birthDate, LocalDate.now());
	}

	@Override
	public ReturnCode removeEmployee(long id) {
		Employee empl;
		
		try {
			writeLockGeneral.lock();
			empl = mapEmployees.remove(id);
		} finally {
			writeLockGeneral.unlock();
		}
		if (empl == null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		// V.R. It is not necessary. BEGIN
		List<Employee>listEmployeesAge;
		try {
			readLockAge.lock();
			listEmployeesAge = employeesAge.get(getAge(empl));
		} finally {
			readLockAge.unlock();
		}
		// V.R. It is not necessary. END
		try {
			writeLockAge.lock();
			listEmployeesAge.remove(empl);
		} finally {
			writeLockAge.unlock();
		}
		// V.R. It is not necessary. BEGIN
		List<Employee>listEmployeesDepartment;
		try {
			readLockDepartment.lock();
			listEmployeesDepartment = employeesDepartment.get(empl.department);
		} finally {
			readLockDepartment.unlock();
		}
		// V.R. It is not necessary. END
		try {
			writeLockDepartment.lock();
			listEmployeesDepartment.remove(empl);
		} finally {
			writeLockDepartment.unlock();
		}
		// V.R. It is not necessary. BEGIN
		List<Employee>listEmployeesSalary;
		try {
			readLockSalary.lock();
			listEmployeesSalary = employeesSalary.get(empl.salary);
		} finally {
			readLockSalary.unlock();
		}
		// V.R. It is not necessary. END
		try {
			writeLockSalary.lock();
			listEmployeesSalary.remove(empl);
		} finally {
			writeLockSalary.unlock();
		}
		return ReturnCode.OK;
	}

	@Override
	public Iterable<Employee> getAllEmployees() {
		Collection<Employee> employees;
		
		try {
			readLockGeneral.lock();
			employees = mapEmployees.values();
			// V.R. It is much better to to write return here:
			// return copyEmployees(employees);
		} finally {
			readLockGeneral.unlock();
		}
		return copyEmployees(employees);
	}

	private Iterable<Employee> copyEmployees(Collection<Employee> employees) {
		
		return employees.stream()
				.map(empl -> copyOneEmployee(empl))
				.collect(Collectors.toList());
	}

	private Employee copyOneEmployee(Employee empl) {
		return new Employee(empl.id, empl.name, empl.birthDate, empl.salary, empl.department);
	}

	@Override
	public Employee getEmployee(long id) {
		Employee empl;
		
		try {
			readLockGeneral.lock();
			empl = mapEmployees.get(id);
			// V.R. It is much better to to write return here:
			// return empl == null ? null : copyOneEmployee(empl);
		} finally {
			readLockGeneral.unlock();
		}
		return empl == null ? null : copyOneEmployee(empl);
	}

	@Override
	public Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		Collection<List<Employee>> lists;
		
		try {
			readLockAge.lock();
			lists = employeesAge.subMap(ageFrom, true, ageTo, true).values();
			// V.R. It is better
			// List<Employee> employeesList = getCombinedList(lists);
			// return copyEmployees(employeesList);
		} finally {
			readLockAge.unlock();
		}
		List<Employee> employeesList = getCombinedList(lists);
		return copyEmployees(employeesList);
	}

	private List<Employee> getCombinedList(Collection<List<Employee>> lists) {
		
		return lists.stream().flatMap(List::stream).collect(Collectors.toList());
	}

	@Override
	public Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		Collection<List<Employee>> lists;
		
		try {
			readLockSalary.lock();
			lists = employeesSalary.subMap(salaryFrom, true, salaryTo, true).values();
			// V.R. It is better
			// List<Employee> employeesList = getCombinedList(lists);
			// return copyEmployees(employeesList);
		} finally {
			readLockSalary.unlock();
		}
		List<Employee> employeesList = getCombinedList(lists);
		return copyEmployees(employeesList);
	}

	@Override
	public Iterable<Employee> getEmployeesByDepartment(String department) {
		List<Employee> employees;
		
		try {
			readLockDepartment.lock();
			employees = employeesDepartment.getOrDefault(department, Collections.emptyList());
			// V.R. It the place for return
		} finally {
			readLockDepartment.unlock();
		}
		
		return employees.isEmpty() ? employees : copyEmployees(employees);
	}

	

	@Override
	public Iterable<Employee> getEmployeesByDepartmentAndSalary(String department, int salaryFrom,
			int salaryTo) {
		Iterable<Employee> employeesByDepartment;
		try {
			readLockDepartment.lock();
			employeesByDepartment = getEmployeesByDepartment(department);
		} finally {
			readLockDepartment.unlock();
		}
		HashSet<Employee> employeesBySalary;
		try {
			readLockSalary.lock();
			employeesBySalary = new HashSet<>((List<Employee>) getEmployeesBySalary(salaryFrom, salaryTo));
		} finally {
			readLockSalary.unlock();
		}
		
		return StreamSupport.stream(employeesByDepartment.spliterator(), false)
				.filter(employeesBySalary::contains).collect(Collectors.toList());
	}

	@Override
	public ReturnCode updateSalary(long id, int newSalary) {
		Employee empl;
		try {
			readLockGeneral.lock();
			empl = mapEmployees.get(id);
		} finally {
			readLockGeneral.unlock();
		}
		// V.R. Both of IFs can be inside the try
		if (empl == null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		if (empl.salary == newSalary) {
			return ReturnCode.SALARY_NOT_UPDATED;
		}
		List<Employee> empBySalary;
		try {
			readLockSalary.lock();
			empBySalary = employeesSalary.get(empl.salary);
		} finally {
			readLockSalary.unlock();
		}
		try {
			writeLockSalary.lock();
			empBySalary.remove(empl);
			empl.salary = newSalary;
			employeesSalary.computeIfAbsent(empl.salary, k -> new LinkedList<Employee>()).add(empl);
		} finally {
			writeLockSalary.unlock();
		}
		// V.R. return can be inside the try's brackets.
		return ReturnCode.OK;
	}

	@Override
	public ReturnCode updateDepartment(long id, String newDepartment) {
		Employee empl;
		try {
			readLockGeneral.lock();
			empl = mapEmployees.get(id);
		} finally {
			readLockGeneral.unlock();
		}
		if (empl == null) {
			return ReturnCode.EMPLOYEE_NOT_FOUND;
		}
		if (empl.department.equals(newDepartment)) {
			return ReturnCode.DEPARTMENT_NOT_UPDATED;
		}
		List<Employee> empByDepartment;
		try {
			readLockDepartment.lock();
			empByDepartment = employeesDepartment.get(empl.department);
		} finally {
			readLockDepartment.unlock();
		}
		try {
			writeLockDepartment.lock();
			empByDepartment.remove(empl);
			empl.department = newDepartment;
			employeesDepartment.computeIfAbsent(empl.department, k -> new LinkedList<Employee>()).add(empl);
		} finally {
			writeLockDepartment.unlock();
		}
		return ReturnCode.OK;
	}

	@Override
	public void restore() {
		File inputFile = new File(fileName);
		if (inputFile.exists()) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(inputFile))) {
				EmployeesMethodsMapsImpl employeesFromFile = (EmployeesMethodsMapsImpl) input.readObject();
				/* V.R.
				 * Is it possible  to read/write maps and to change them concurrently?
				 */
				this.employeesAge = employeesFromFile.employeesAge;
				this.employeesDepartment =  employeesFromFile.employeesDepartment;
				this.employeesSalary = employeesFromFile.employeesSalary;
				this.mapEmployees = employeesFromFile.mapEmployees;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} 
		}
		
	}

	@Override
	public void save() {
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
			/* V.R.
			 * Is it possible  to read/write maps and to save them concurrently?
			 */
			output.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		
	}

}
