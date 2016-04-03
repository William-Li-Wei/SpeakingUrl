import app.MappingTable;
import junit.framework.TestCase;
import org.apache.commons.collections.BidiMap;
import org.junit.Test;

/**
 * Created by william on 03.04.16.
 */
public class MappingTableTest extends TestCase {

    @org.junit.Test
    public void testGetInstance() {
        MappingTable table = MappingTable.getInstance();
        assertEquals(table, MappingTable.getInstance());
    }

    @Test
    public void testGetMaps() {
        MappingTable table = MappingTable.getInstance();
        BidiMap urlMap = table.getUrlMap();
        BidiMap paramMap = table.getParamMap();

        assertEquals(2 + 10 + 10 + 1000, urlMap.size());
        assertEquals("Women", urlMap.get("gender=female"));
        assertEquals("Adidas", urlMap.get("tag=21"));

        assertEquals(2 + 10 + 10 + 1000, paramMap.size());
        assertEquals("gender=female", paramMap.get("Women"));
        assertEquals("tag=21", paramMap.get("Adidas"));
    }
}
