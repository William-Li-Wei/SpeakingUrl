import app.TopHitURL;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by william on 03.04.16.
 */
public class TopHitURLTest extends TestCase {
    @Test
    public void testEntries() {
        TopHitURL topHit = TopHitURL.getInstance();
        topHit.addEntry("Adidas", "tag=21");
        topHit.addEntry("gender=female", "Women");

        assertEquals("tag=21", topHit.getEntry("Adidas"));
        assertEquals("Women", topHit.getEntry("gender=female"));
    }
}
