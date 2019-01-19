package squawker.jdbi

import java.util.logging.Logger

class TestDB extends BasePersistenceSpec {
	Logger logger = Logger.getLogger("TestDB");
	
	def setup() {
		handle.execute('create table TEXTS (_text varchar(20))')
	}
	def cleanup() {
		handle.execute('drop table TEXTS if exists')
	}
	
	def 'main_test'() {
		given:
		def msg = "Hello world";
		handle.execute('INSERT INTO TEXTS VALUES(?)', msg)
		
		when:
		def actual = handle.createQuery('select * from TEXTS').mapTo(String).first()
		logger.fine("returned: "+actual)
		
		then:
		msg == actual 
	}
	
}
