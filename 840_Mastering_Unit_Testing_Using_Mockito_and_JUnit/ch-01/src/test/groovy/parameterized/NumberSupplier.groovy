package parameterized
import java.util.List

import org.junit.experimental.theories.ParameterSignature
import org.junit.experimental.theories.ParameterSupplier
import org.junit.experimental.theories.PotentialAssignment

import static org.junit.experimental.theories.PotentialAssignment.forValue;

// see ParameterSupplierTest
class NumberSupplier extends ParameterSupplier {

	@Override
	public List<PotentialAssignment> getValueSources(ParameterSignature sig) throws Throwable {
		[ 
			forValue('long', 2L),
			forValue('float', 5f),
			forValue('double', 89d),
			]
	}

}
