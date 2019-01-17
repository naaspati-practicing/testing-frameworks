import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * html not rendering properly 
 * @author Sameer
 *
 */
@DisplayName("display name demo: class")
public class DisplayNameDemo {
	@Test
    @DisplayName("Custom test name containing spaces")
    void testWithDisplayNameContainingSpaces() {
    }

    @Test
    @DisplayName("╯°□°）╯")
    void testWithDisplayNameContainingSpecialCharacters() {
    }

    @Test
    @DisplayName("😱")
    void testWithDisplayNameContainingEmoji() {
    }
}
