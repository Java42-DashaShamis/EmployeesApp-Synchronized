package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.employees.dto.Employee;

class InputOutputTest {
InputOutput io = new ConsoleInputOutput();
	@BeforeEach
	void setUp() throws Exception {
	}

//	@Test
//	void employeeInputAsOneString() {
//		Employee empl = io.readObject("Enter employee data as string <id>#<name>#<birthdate ISO>#<salary>#<department>",
//				"Wrong format of employee data", InputOutputTest::toEmployeeFromStr);
//		io.writeObjectLine(empl);
//	}
//	static Employee toEmployeeFromStr(String str) {
//		String emplTokens[] = str.split("#");
//		long id = Long.parseLong ( emplTokens[0] );
//		String name = emplTokens[1];
//		LocalDate birthDate = LocalDate.parse(emplTokens[2]);
//		int salary = Integer.parseInt(emplTokens[3]);
//		String department = emplTokens[4];
//		return new Employee(id, name, birthDate, salary, department);
//	}
//	
//	@Test 
//	void readPredicateTest() {
//		String str = io.readStringPredicate("enter any string containing exactly 3 symbols",
//				"this is no string containing exactly 3 symbols", s -> s.length() == 3);
//		assertEquals(3, str.length());
//	}
	@Test
	void employeeBySeparateFields() {
		
		//enter ID by readLong method
		long id = io.readLong("Enter ID");
		//enter Name by readStringPredicate (only letters with capital first letter)
		String name = io.readStringPredicate("Enter name", "Name may contain only letters with first capital",
				str -> str.matches("[A-Z][a-z]+"));
		//enter birthdate by readDate 
		LocalDate birthDate = io.readDate("Enter birthdate in the yyyy-MM-dd format");
		//enter salary by readInt(prompt, min, max)
		
		int salary = io.readInt("Enter salary ", 5000, 35000);
		//enter department by readStringOption specifying possible departments
		
		HashSet<String> departments = new HashSet<>(Arrays.asList("QA", "Development", "Management"));
		String department = io.readStringOption("Enter department " + departments, departments);
		Employee empl = new Employee(id, name, birthDate, salary, department);
		System.out.println(empl);
		
	}

}
