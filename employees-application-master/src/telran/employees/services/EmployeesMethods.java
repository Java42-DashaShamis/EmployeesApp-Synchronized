package telran.employees.services;
import java.io.Serializable;

import telran.employees.dto.*;
public interface EmployeesMethods extends Serializable{
ReturnCode addEmployee(Employee empl);
ReturnCode removeEmployee(long id);
Iterable<Employee> getAllEmployees();
Employee getEmployee(long id);
Iterable<Employee> getEmployeesByAge(int ageFrom, int ageTo);
Iterable<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
Iterable<Employee> getEmployeesByDepartment(String department);
Iterable <Employee> getEmployeesByDepartmentAndSalary(String department, int salaryFrom, int salaryTo);
ReturnCode updateSalary(long id, int newSalary);
ReturnCode updateDepartment(long id, String newDepartment);
void restore(); //restores EmployeesMethods object from ObjectInputStream
void save(); //saves EmployeesMethods object into ObjectOutputStream
}
