package com.monjya.android.net;

/**
 * Created by xuemingxiang on 16-11-14.
 */

public class URL {

//    public static final String HOST = "http://192.168.0.104:3000";
    public static final String HOST = "http://121.42.248.212";

    public static final String PREFIX = HOST + "/api";

    public static final String URL_PRODUCTS = PREFIX + "/products.json";

    public static final String URL_SIGN_IN = PREFIX + "/users/sign_in.json";

    public static final String URL_SIGN_UP = PREFIX + "/users/sign_up.json";

    public static final String URL_TRAVEL_AGENTS = PREFIX + "/travel_agents.json";

    public static final String URL_VISITORS = PREFIX + "/visitors.json";

    public static final String URL_ORDERS = PREFIX + "/orders.json";

    public static String urlProduct(Long productId) {
        return PREFIX + "/products/" + productId + ".json";
    }

    public static String urlUser(Long userId) {
        return PREFIX + "/users/" + userId + ".json";
    }

    public static String urlVisitor(Long visitorId) {
        return PREFIX + "/visitors/" + visitorId + ".json";
    }

    public static String urlSetTravelAgent(Long userId) {
        return PREFIX + "/users/" + userId + "/set_travel_agent.json";
    }

    public static String urlOrder(Long orderId) {
        return PREFIX + "/orders/" + orderId + ".json";
    }
}
