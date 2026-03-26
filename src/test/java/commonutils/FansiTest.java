package commonutils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FansiTest {

    @Test
    public void testFansiStyling() {
        // Test that Fansi methods don't throw exceptions and produce output
        Fansi fansi = new Fansi();
        String output = fansi.red().bold().append("Test").reset().render();
        assertNotNull(output);
        assertTrue(output.contains("Test"));
    }

    @Test
    public void testGradient() {
        String gradient = Fansi.create().gradientFg("Hello", "255;0;0", "0;255;0").render();
        assertNotNull(gradient);
        assertTrue(gradient.length() > 0);
    }

    // Add more specific tests as needed
}