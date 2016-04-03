package controller;

import app.controller.LookupController;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.List;

/**
 * Created by william on 03.04.16.
 */
public class LookupControllerTest extends TestCase {

    @Test
    public void testGetSpeakingURLFromParams() {
        LookupController controller = new LookupController();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/products");
        request.setParameter("gender", "female");
        request.setParameter("tag", new String[]{"2", "24"});

        List<String> params = Arrays.asList(new String[]{"gender=female", "tag=2", "tag=24"});
        List<String> urlFragments = Arrays.asList(new String[]{"Women", "Boots", "Michi"});

        assertEquals(controller.PARAMETER_URL, controller.getURLFormat(request));
        assertEquals(params, controller.getRequestParams(request));
        assertEquals(urlFragments, controller.getSpeakingURLFromMappingTable(params));
        assertEquals("/Women/Boots/Michi/", controller.getSpeakingURL(request));
    }

    @Test
    public void testGetParamsURLFromSpeakingURL() {
        LookupController controller = new LookupController();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/Women/Boots/Michi/");

        List<String> params = Arrays.asList(new String[]{"gender=female", "tag=2", "tag=24"});
        List<String> urlFragments = Arrays.asList(new String[]{"Women", "Boots", "Michi"});

        assertEquals(controller.SPEAKING_URL, controller.getURLFormat(request));
        assertEquals(urlFragments, controller.getRequestURLFragments(request));
        assertEquals(params, controller.getParamsFromMappingTable(urlFragments));
        assertEquals("/products?gender=female&tag=2&tag=24", controller.getParameterURL(request));
    }
}
