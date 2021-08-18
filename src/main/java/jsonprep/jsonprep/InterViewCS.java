package jsonprep.jsonprep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InterViewCS {

	@SuppressWarnings("null")
	public static void main(String[] args) throws ParseException, IOException, SQLException {


		final Logger logger = Logger.getLogger(InterViewCS.class);
		logger.info("The Application started successfully");
		
        File file = new File("./resource/jsonfiles/applogjson.txt");
		//File file = new File(args[0]);
		FileReader filereader = new FileReader(file);
		BufferedReader bfreader = new BufferedReader(filereader);

		String line;
		ApplLog appllog;
		LinkedHashMap<String, ApplLog> event = new LinkedHashMap<String, ApplLog>();

		Connection con = null;

		int result = 0;

		try {
			logger.info("Opening JDBC connection for HSQL DB");	
			Class.forName("org.hsqldb.jdbc.JDBCDriver"); 
		    con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb;hsqldb.lock_file=false","SA",""); 
		    logger.info("HSQLDB connection established successfully.");
		    logger.info("Creating application_log table");
			String createquery =" CREATE TABLE application_log (\r\n" + 
					"id VARCHAR(50) NOT NULL,\r\n" + 
					"alert VARCHAR(50),\r\n" + 
					"duration VARCHAR(50),\r\n" + 
					"state VARCHAR(50) NOT NULL, \r\n" + 
					"type VARCHAR(50),\r\n" + 
					"host VARCHAR(50)\r\n" + 
					")";
					  
			PreparedStatement preparedStmtCreateTble = con.prepareStatement(createquery);
			preparedStmtCreateTble.executeUpdate();
			
			logger.info("The application_log table created successfully");
			logger.info("Reading JSON event log file line by line from buffer and putting in Linked hasset");
			while ((line = bfreader.readLine()) != null) {

				JSONParser parser = new JSONParser();
				JSONObject jsonobject = (JSONObject) parser.parse(line);
			//	System.out.println(jsonobject.toString());

				String id = (String) jsonobject.get("id");
				String state = (String) jsonobject.get("state");
				String type = (String) jsonobject.get("type");
				String host = (String) jsonobject.get("host");
				String timestamp = (String) jsonobject.get("timestamp");
				appllog = new ApplLog(id, state, type, host, timestamp);
				event.put(id + state, appllog);

			}					 

			logger.info("Processing records from file and setting alert and duration diff from start and end of event");
			
			HashMap<String, ApplLogAlert> dbrow = new HashMap<String, ApplLogAlert>();
			
			for (Entry<String, ApplLog> entry : event.entrySet()) {
			//			System.out.println(entry.getValue().toString());
			if (!(dbrow.containsKey(entry.getValue().id))) {
				ApplLogAlert appalert = new ApplLogAlert(entry.getValue().id, "", "", entry.getValue().host,
								entry.getValue().type, entry.getValue().state, entry.getValue().timestamp);
				dbrow.put(entry.getValue().id, appalert);
			} else {
				ApplLogAlert appalertupdate;
				String times1 = dbrow.get(entry.getValue().id).timestamp;
				String times2 = entry.getValue().timestamp;
				Long diff = Long.parseLong(times1) - Long.parseLong(times2);
					
				if (diff < 0) 
					diff = diff * (-1);
				else 
					diff = diff * 1;	

					if (diff >= 4) {
						appalertupdate = new ApplLogAlert(entry.getValue().id, "True", diff.toString(),
								entry.getValue().host, entry.getValue().type, entry.getValue().state,
								entry.getValue().timestamp);
					} else {
						appalertupdate = new ApplLogAlert(entry.getValue().id, "False", diff.toString(),
								entry.getValue().host, entry.getValue().type, entry.getValue().state,
								entry.getValue().timestamp);
					}
					dbrow.replace(entry.getValue().id, appalertupdate);
				}
			}
			logger.info("Processed events are writing into DB table ");
			for (Entry<String,ApplLogAlert> ent: dbrow.entrySet()) {
					System.out.println("Final entries "+ ent.getValue().toString());
					String query =" insert into application_log (id, alert, duration, state, type, host)" +
							  " values (?, ?, ?, ?, ?, ?)";
							  
					PreparedStatement preparedStmt = con.prepareStatement(query);
					 
					preparedStmt.setString (1, ent.getValue().id);  
					preparedStmt.setString (2, ent.getValue().alert); 
					preparedStmt.setString (3, ent.getValue().duration);
					preparedStmt.setString (4, ent.getValue().state );
					preparedStmt.setString (5, ent.getValue().type); 
					preparedStmt.setString (6, ent.getValue().host); 

					result = preparedStmt.executeUpdate();
					System.out.println(result + " rows effected");
					System.out.println("Rows inserted successfully");
					con.commit(); 
			logger.info("The event inserting into DB table is done successfully");
			}
			
			bfreader.close();
			con.close();
			logger.info("DB Connection closed and other resources released");
		} catch (Exception e) {
			logger.error("Run time exception occured......................!");
			e.printStackTrace();
		}
		
	}	
}