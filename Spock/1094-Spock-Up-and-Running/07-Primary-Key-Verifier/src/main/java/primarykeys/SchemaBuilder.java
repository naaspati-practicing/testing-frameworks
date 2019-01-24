package primarykeys;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;
import java.util.logging.Logger;

public class SchemaBuilder {
	private static final Logger LOGGER = Logger.getLogger(SchemaBuilder.class.getName());

	private final Connection connection;
	private final String[] tableNames = {"foo", "bar", "baz"};
	
	public SchemaBuilder(Connection connection) {
		this.connection = connection;
	}
	
	public void createSchema() throws SQLException {
		execute("create table %s (id varchar(32) primary key, name varchar(255));\n");
	}
	public void destroySchema() throws SQLException {
		execute("drop table %s if exists;\n");
	} 
	
	private void execute(String format) throws SQLException {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		
		for (String s : tableNames) 
			formatter.format(format, s);
		
		formatter.close();
		String s = sb.toString();
		LOGGER.fine(() ->  sb.insert(0, "\nexecute SQL: \n").toString());
		
		try(Statement st = connection.createStatement()) {
			st.execute(s);
		}
	}
	

}
