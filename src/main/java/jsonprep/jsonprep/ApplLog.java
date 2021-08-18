package jsonprep.jsonprep;

import java.security.Timestamp;
import java.util.concurrent.TimeUnit;

public class ApplLog {

	String id;
	String state;
	String type;
	String host;
	String timestamp;
	
	public ApplLog(String id, String state, String type, String host, String timestamp) {
		super();
		this.id = id;
		this.state = state;
		this.type = type;
		this.host = host;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ApplLog [id=" + id + ", state=" + state + ", type=" + type + ", host=" + host + ", timestamp="
				+ timestamp + "]";
	}
	
	
}
