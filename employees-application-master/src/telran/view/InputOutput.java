package telran.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String NO_NUMBER = "No number";
	String NO_OPTION = "Wrong input (see possible options)";
	String readString(String prompt);
	void writeObject(Object obj);
	default void writeObjectLine(Object obj) {
		writeObject(obj.toString() + "\n");
	}

	default <R> R readObject(String prompt, String errorPrompt, Function<String, R> mapper) {
		
		while(true) {
			String string = readString(prompt);
			try {
				R result = mapper.apply(string);
				return result;
			} catch (Exception e) {
				writeObjectLine(errorPrompt);
			}
		}
		
	}

	default String readStringPredicate(String prompt, String errorMessage, Predicate<String> predicate) {
		
		return readObject(prompt, errorMessage, str -> {
			if (predicate.test(str)) {
				return str;
			}
			throw new IllegalArgumentException();
		}); 
	}
	default Integer readInt( String prompt) {
		return readObject(prompt, NO_NUMBER, Integer::parseInt);
	}
	default Double readDouble(String prompt) {
		return readObject(prompt, NO_NUMBER, Double::parseDouble);
	}
	default Integer readInt(String prompt, int min, int max) {
		return readObject(prompt, String.format("No number in the range [%d-%d]",min,max), str -> {
			int res = Integer.parseInt(str);
			if (res < min || res > max) {
				throw new IllegalArgumentException();
			}
			return res;
		});
	}
	default Long readLong( String prompt) {
		return readObject(prompt, NO_NUMBER, Long::parseLong);
		
	}
	default String readStringOption(String prompt, Set<String> options) {
		return readStringPredicate(prompt, NO_OPTION, options::contains);
		
	}
	default LocalDate readDate(String prompt)  {
		return readObject(prompt, "Wrong date in yyyy-MM-dd format", LocalDate::parse);
	}
	default LocalDate readDate(String prompt,  DateTimeFormatter formatter) {
		return readObject(prompt, "Wrong date in the specified format", str -> LocalDate.parse(str, formatter));
	}
}
