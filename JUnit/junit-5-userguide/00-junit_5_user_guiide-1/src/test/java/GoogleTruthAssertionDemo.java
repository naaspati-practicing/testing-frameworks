import static com.google.common.truth.Truth8.*;

import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

public class GoogleTruthAssertionDemo {
	
	@Test
	void assertWithGoogleTruth() {
		assertThat(OptionalInt.of(2+1)).hasValue(3);
	}
}
