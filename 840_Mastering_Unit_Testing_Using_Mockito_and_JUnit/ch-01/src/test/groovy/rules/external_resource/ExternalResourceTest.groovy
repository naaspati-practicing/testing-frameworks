package rules.external_resource

import static org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource
import org.junit.rules.TestName

class ExternalResourceTest {
	Resource rsrc;
	@Rule
	public TestName name = new TestName()
	@Rule
	public ExternalResource rule = [
		before: {->
			rsrc = new Resource()
			rsrc.open()
			println name.methodName
		},
		after: {->
			rsrc.close()
			println '\n'
		}
	] as ExternalResource

	@Test
	void someTest_1() {
		println rsrc.get()
	}
	@Test
	void someTest_2() {
		println rsrc.get()
	}
}
