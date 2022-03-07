package telran.net;

import java.io.*;
import java.net.*;
import telran.net.dto.*;

public class TcpSender implements Sender {
 private Socket socket;
 private ObjectOutputStream writer;
 private ObjectInputStream reader;
 public TcpSender(String host, int port) throws Exception {
	 socket = new Socket(host, port);
	 writer = new ObjectOutputStream(socket.getOutputStream());
	 reader = new ObjectInputStream(socket.getInputStream());
 }
	@SuppressWarnings("unchecked")
	@Override
	public <T> T send(String requestType, Serializable requestData){
		try {
			Request request = new Request(requestType, requestData);
			writer.writeObject(request);
			Response response = (Response) reader.readObject();
			if (response.responseCode != ResponseCode.OK) {
				throw new RuntimeException((String)response.responseData);
			}
			return (T)response.responseData;
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void close() throws IOException {
		socket.close();
		
	}

}
