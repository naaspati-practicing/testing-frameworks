package primarykeys

import java.sql.DriverManager
import java.sql.ResultSet
import java.util.logging.Logger

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class PrimaryKeySpec extends Specification {
	private static final Logger LOGGER = Logger.getLogger('PrimaryKeySpec');
	
	@Shared @AutoCleanup
	java.sql.Connection connection;
	@Shared @AutoCleanup('destroySchema')
	SchemaBuilder schemaBuillder;
	
	def setupSpec() {
		Class.forName('org.h2.Driver')
		connection = DriverManager.getConnection('jdbc:h2:mem:test')
		
		schemaBuillder = new SchemaBuilder(connection)
		schemaBuillder.createSchema()
	}
	
	
	private Iterable<String> readTableNames() {
		def list = []
		ResultSet tables = connection.metaData.getTables(null, null, '%', ['TABLE'] as String[])
		try {
			while(tables.next())
				list << tables.getString(3)
		} finally {
			tables.close()
		}
		
		LOGGER.fine "found tableName(s): $list"
		list
	}
	
	def 'checking if table has primary key' () {
		expect:
		keys.next()
		
		cleanup:
		keys.close()
		
		where:
		tableName << readTableNames()
		keys = connection.metaData.getPrimaryKeys(null, null, tableName)
	}
}
