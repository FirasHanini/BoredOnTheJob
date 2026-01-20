package tn.manzel.commercee.ApiEndpoints;

public class ApiEndpoints {
    public static final String AUTH_BASE = "/auth";
    public static final String SELLER_BASE = "/seller";
    public static final String BUYER_BASE = "/buyer";
    public static final String PRODUCT_BASE = "/products";
    public static final String ORDER_BASE = "/order";
    public static final String STRIPE_BASE = "/stripe";
    public static final String USR_MANAGEMENT_BASE = "/user-management";
    public static final String CART_BASE = "/cart";
    public static final String KONNEKT_BASE = "/konnekt";

    public static class AuthEnpoints {
        public static final String REGISTER = "/register";
        public static final String LOGIN = "/login";
        public static final String REGISTER_SELLER = "/seller-register";
        public static final String LOGOUT = "/logout";
    }
    public static class productEndpoints {
        public static final String GET_BY_SELLER = "/seller";
    }
    public static class konnektEndpoints {
        public static final String INIT_PAYMENT = "/init-payment";
    }
}
