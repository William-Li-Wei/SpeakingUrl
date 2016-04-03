package app;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

import java.util.HashMap;

/**
 * Created by william on 02.04.16.
 */
public enum MappingTable {
    INSTANCE;

    private static BidiMap paramToUrl;
    private static BidiMap urlToParam;

    // init the lookup tables
    MappingTable(){
        // TODO: read the mapping data from DB instead of the hard coded.
        initMappingTable();
    }

    // static 'instance' method
    public static MappingTable getInstance( ) {
        return INSTANCE;
    }

    public static BidiMap getUrlMap( ) {
        return paramToUrl;
    }

    public static BidiMap getParamMap( ) {
        return urlToParam;
    }

    // TODO: make the BidiMap thread safe if we need to manipulate the map


    /**
     * generate 200.000 different urls with different params combinations
     * 2 genders * 10 categories * 10 patterns * 1.000 brands = 200.000 combinations
     */
    private static void initMappingTable() {

        paramToUrl = new DualHashBidiMap();
        urlToParam = new DualHashBidiMap();

        paramToUrl.put("gender=female", "Women");
        paramToUrl.put("gender=male", "Men");

        // 10 different categories
        paramToUrl.put("tag=1", "Shoes");
        paramToUrl.put("tag=2", "Boots");
        paramToUrl.put("tag=3", "Sneakers");
        paramToUrl.put("tag=4", "Slippers");
        paramToUrl.put("tag=5", "Boat-Shoes");
        paramToUrl.put("tag=6", "Clothing");
        paramToUrl.put("tag=7", "T-Shirts");
        paramToUrl.put("tag=8", "Shirts");
        paramToUrl.put("tag=9", "Pants");
        paramToUrl.put("tag=10", "Coats");

        // 10 different patterns
        paramToUrl.put("tag=11", "Animal-Print");
        paramToUrl.put("tag=12", "Batik");
        paramToUrl.put("tag=13", "Camouflage");
        paramToUrl.put("tag=14", "Cartoon");
        paramToUrl.put("tag=15", "Dots");
        paramToUrl.put("tag=16", "Ethnic");
        paramToUrl.put("tag=17", "Floral");
        paramToUrl.put("tag=18", "Leopard");
        paramToUrl.put("tag=19", "Plaid");
        paramToUrl.put("tag=20", "Print");

        // 1000 different brands
        paramToUrl.put("tag=21", "Adidas");
        paramToUrl.put("tag=22", "Nike");
        paramToUrl.put("tag=23", "H.M");
        paramToUrl.put("tag=24", "Michi");
        paramToUrl.put("tag=25", "NSF");
        for(int i = 0; i < 995; i++) {
            paramToUrl.put("tag=" + (i + 26), "Other-Brand" + (i + 1));
        }

        // get inverse BidiMap
        urlToParam = paramToUrl.inverseBidiMap();
    }
}