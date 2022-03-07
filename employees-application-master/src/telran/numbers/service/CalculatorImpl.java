package telran.numbers.service;
import java.util.*;
import java.util.function.BinaryOperator;
public class CalculatorImpl implements Calculator{
private static HashMap<String, BinaryOperator<Double>> mapOperators;
static {
	//constructing static fields
	mapOperators = new HashMap<>();
	mapOperators.put("+", (op1, op2) -> op1 + op2);
	mapOperators.put("-", (op1, op2) -> op1 - op2);
	mapOperators.put("*", (op1, op2) -> op1 * op2);
	mapOperators.put("/", (op1, op2) -> op1 / op2);
	
}
@Override
public double compute(String operator, double op1, double op2) {
	BinaryOperator<Double> method = mapOperators.getOrDefault(operator,
			(a, b) -> {throw new IllegalArgumentException("unknown operator");});
	return method.apply(op1,  op2);
}
}
