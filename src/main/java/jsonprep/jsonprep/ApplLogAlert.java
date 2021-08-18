package jsonprep.jsonprep;

public class ApplLogAlert {
	String id;
	String alert;
	String duration;
	String host;
	String type;
	String state;
	String timestamp;
	
	public ApplLogAlert(String id, String alert, String duration, String host, String type,String state,String timestamp) {
		super();
		this.id = id;
		this.alert = alert;
		this.duration = duration;
		this.host = host;
		this.type = type;
		this.state = state;
		this.timestamp =timestamp;
	}

	@Override
	public String toString() {
		return "ApplLogAlert [event_id=" + id + ", alert=" + alert + ", duration=" + duration + ", host=" + host
				+ ", type=" + type + ", state=" + state + ", timestamp=" + timestamp + "]";
	}

	
}
