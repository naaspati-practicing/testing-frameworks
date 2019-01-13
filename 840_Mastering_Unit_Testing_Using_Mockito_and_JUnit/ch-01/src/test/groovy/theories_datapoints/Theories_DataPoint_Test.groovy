package theories_datapoints
import static org.junit.Assert.*

import org.junit.Test
import org.junit.experimental.theories.DataPoint
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

@RunWith(Theories)
class Theories_DataPoint_Test {
	@DataPoint
	public static jack = 'Jack', mike = 'Mike'
	
	@Theory
	void test_1(String name) {
		println name
	}
	@Theory
	void test_2(String name, String name2) {
		println name + '  ' + name2
	}
}
