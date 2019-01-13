package theories_datapoints
import static org.junit.Assert.*

import org.junit.Test
import org.junit.experimental.theories.DataPoint
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

@RunWith(Theories)
class Theories_DataPoints_Test {
	@DataPoints
	public static char[] values = ['A', 'B', 'C']
	
	@Theory
	void test_2(char a, char b) {
		println "$a $b"
	}
}
