package app.controller;

import app.MappingTable;
import app.TopHitURL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by william on 02.04.16.
 */
@RestController
public class LookupController {

    public static final String BASE_PATH = "products";
    public static final String PARAMETER_URL = "parameters URL";
    public static final String SPEAKING_URL = "speaking URL";

    @RequestMapping("/**")
    public ResponseEntity lookupUrl(HttpServletRequest request) {
        String resultURL;

        // TODO: handle other requestPath that do not start with '/products'
        String urlFormat = getURLFormat(request);

        if(PARAMETER_URL.equals(urlFormat)) {
            resultURL = getSpeakingURL(request);
        } else {
            resultURL = getParameterURL(request);
        }

        // TODO: handle other requests better
        return ResponseEntity.ok(resultURL);
    }


    /**
     * determine if the current request URI is in speaking URL format.
     * URL with parameters always starts with /products
     * @param request
     * @return url format
     * e.g. requesting '/products?gender=female&tag=2&tag=24'
     *      returns PARAMETER_URL
     *      requesting '/Women/Boots/Michi/'
     *      returns SPEAKING_URL
     */
    public String getURLFormat(HttpServletRequest request) {
        String format = SPEAKING_URL;
        if(request.getRequestURI().contains("/" + BASE_PATH)) {
            format = PARAMETER_URL;
        }
        return format;
    }


    /**
     * get the speaking url corresponding to the given params-based request url
     * @param request
     * @return speaking url
     * e.g. requesting '/products?gender=female&tag=2&tag=24'
     *      returns '/Women/Boots/Michi/'
     */
    public String getSpeakingURL(HttpServletRequest request) {
        String speakingURL = "/FASHION/";

        // get params from the request
        List<String> params = getRequestParams(request);
        // translate params to speaking url fragments
        List<String> URLFragments = getSpeakingURLFromMappingTable(params);

        // assemble the final speaking url with fragments
        if(URLFragments.size() > 0) {
            speakingURL = "/";
            for(String fragment : URLFragments) {
                speakingURL += fragment + "/";
            }
        }

        return speakingURL;
    }

    /**
     * return the params-based url corresponding to the given speaking url
     * @param request
     * @return params-based url
     * e.g. requesting '/Women/Boots/Michi/'
     *      returns '/products?gender=female&tag=2&tag=24'
     */
    public String getParameterURL(HttpServletRequest request) {
        String parameterURL = "/" + BASE_PATH;

        // get speaking url fragments from the request
        List<String> URLFragments = getRequestURLFragments(request);
        // translate fragments to params
        List<String> params = getParamsFromMappingTable(URLFragments);

        // assemble the final params-based url
        if(params.size() > 0) {
            parameterURL += "?";
            for(String param : params) {
                parameterURL += param + "&";
            }
            parameterURL = parameterURL.substring(0, parameterURL.length() - 1);
        }

        return parameterURL;
    }

    /**
     * get params from the requests
     * @param request
     * @return the params list
     * e.g. requesting '/products?gender=female&tag=2&tag=24'
     *      returns ["gender=female", "tag=2", "tag=24"]
     */
    public List<String> getRequestParams(HttpServletRequest request) {
        List<String> params = new ArrayList<>();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for(String key : parameterMap.keySet()) {
            String[] values = parameterMap.get(key);
            for(String value : values) {
                params.add(key + "=" + value);
            }
        }

        return params;
    }

    /**
     * get fragments of the speaking url
     * @param request
     * @return List of url fragments
     * e.g. requesting '/Women/Boots/Michi/'
     *      returns ["Women", "Boots", "Michi"]
     */
    public List<String> getRequestURLFragments(HttpServletRequest request) {
        String uriWithoutFirstSlash = request.getRequestURI();
        if(uriWithoutFirstSlash.indexOf("/") == 0) {
            uriWithoutFirstSlash = uriWithoutFirstSlash.substring(1, uriWithoutFirstSlash.length());
        }
        List<String> fragments = Arrays.asList(uriWithoutFirstSlash.split("/"));
        return fragments;
    }

    /**
     * get the speaking url fragments from the mapping table, based on the given params
     * @param params
     * @return speaking url fragment
     * e.g. given ["gender=female", "tag=2", "tag=24"]
     *      returns ["Women", "Boots", "Michi"]
     */
    public List<String> getSpeakingURLFromMappingTable(List<String> params) {
        List<String> urlFragments = new ArrayList<>();
        MappingTable mappingTable = MappingTable.getInstance();
        TopHitURL topHit = TopHitURL.getInstance();

        for(String param : params) {
            String speakingURLFragment;

            // query the top hit table first
            speakingURLFragment = topHit.getEntry(param);

            // found a hit, add to the list urlFragments
            if(speakingURLFragment != null) {
                urlFragments.add(speakingURLFragment);
            }
            // not found, query the mapping table
            else {
                speakingURLFragment = (String) mappingTable.getUrlMap().get(param);

                // found a hit in the mapping table
                if(speakingURLFragment != null) {
                    // add to the list urlFragments
                    urlFragments.add(speakingURLFragment);
                    // add the hit to topHip table
                    topHit.addEntry(param, speakingURLFragment);
                }
            }
        }

        return urlFragments;
    }

    /**
     * get the params-based url from the mapping table, based on the given url fragments
     * @param urlFragments
     * @return the params
     * e.g. given ["Women", "Boots", "Michi"]
     *      returns ["gender=female", "tag=2", "tag=24"]
     */
    public List<String> getParamsFromMappingTable(List<String> urlFragments) {
        List<String> params = new ArrayList<>();
        MappingTable mappingTable = MappingTable.getInstance();
        TopHitURL topHit = TopHitURL.getInstance();

        for(String fragment : urlFragments) {
            String param;
            // query the top hit table first
            param = topHit.getEntry(fragment);

            // found a hit, add to the list params
            if(param != null) {
                params.add(param);
            }
            // not found, query the mapping table
            else {
                param = (String) mappingTable.getParamMap().get(fragment);
                // found a hit in the mapping table
                if(param != null) {
                    // add to the list params
                    params.add(param);
                    // add the hit to topHip table
                    topHit.addEntry(fragment, param);
                }
            }
        }

        return params;
    }
}
