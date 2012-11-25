package kr.co.talesapp.clouduapi;

import org.apache.http.Header;

public class CUError {
	public static final int SUCCESS=0;
	public static final int ERROR=-1;
	public static final int UNKNOWN=-90;
	public static final int EXCEPTION=-91;
	public static final int NOT_IMPLEMENTED=-999;
	public int error;
	public String errorString;
	public Header contentType;
	public long contentLength;
	public byte[] body;
	
	public String toJSON() {
		return "{ \"error\": "+error+", \"error_str\": \""+errorString+"\"}";
	}
}
