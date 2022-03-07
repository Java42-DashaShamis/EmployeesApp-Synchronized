package telran.numbers.net;
import java.io.*;
import java.net.*;

import telran.numbers.service.*;
public class CalculatorNetworkApplication {
	final static String HOST="localhost";
	final static int PORT = 2000;
	static Calculator calculator = new CalculatorImpl();
public static void main(String[] args) throws Exception{
	ServerSocket serverSocket = new ServerSocket(PORT);
	System.out.println("Server is listening on the port " + PORT);
	while(true) {
		Socket socket = serverSocket.accept();
		runClient(socket);
	}
}
private static void runClient(Socket socket) {
	try(BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintStream writer = new PrintStream(socket.getOutputStream())){
		while (true) {
			String request = reader.readLine();
			if (request == null || request.equals("q")) {
				System.out.println("client closed connection gracefully");
				break;
			}
			
			String response = getResponse(request);
			writer.println(response);
		}
		
	}catch (Exception e) {
		System.out.println("client closed connection abnormally");
	}
	
}
private static String getResponse(String request) {
	String [] tokens = request.split("#");
	if (tokens.length != 3) {
		return "Wrong Request";
	}
	try {
		double res = calculator.compute(tokens[0], Double.parseDouble(tokens[1]),
				Double.parseDouble(tokens[2]));
		return "" + res;
	} catch (Exception e) {
		return e.getMessage();
	}
	
}
}
