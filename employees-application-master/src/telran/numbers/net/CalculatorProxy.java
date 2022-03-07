package telran.numbers.net;

import telran.numbers.service.Calculator;
import java.io.*;
import java.net.*;
public class CalculatorProxy implements Calculator, Closeable  {
	PrintStream writer;
	BufferedReader reader;
	Socket socket;
	public CalculatorProxy(String host, int port) throws Exception{
		socket = new Socket(host, port);
		writer = new PrintStream(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public double compute(String operator, double op1, double op2) {
		try {
			writer.println(String.format("%s#%f#%f", operator, op1, op2));
			
			return Double.parseDouble(reader.readLine());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	@Override
	public void close() throws IOException {
		socket.close();
		
	}

}
