package telran.net.dto;

import java.io.Serializable;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	public ResponseCode responseCode;
	public Serializable responseData;
	public Response(ResponseCode responseCode, Serializable responseData) {
		this.responseCode = responseCode;
		this.responseData = responseData;
	}
	

}
