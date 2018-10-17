package com.thm.hoangminh.multimediamarket.repository.api;

public class ROUTE {
    private static final String USER = "users/";
    private static final String USER_BALANCE = "/balance/";
    private static final String USER_ROLE = "/role/";
    private static final String USER_IMAGE = "/image/";
    private static final String USER_NAME = "/name/";
    private static final String USER_EMAIL = "/email/";
    private static final String USER_GENDER = "/sex/";
    private static final String USER_BIRTHDAY = "/birthday/";

    private static final String ROLE = "roles/";
    private static final String CATEGORY = "categories/";
    private static final String PRODUCT = "products/";

    private static final String FILE = "files/";

    private static final String CARD = "cards/";
    private static final String CARD_STATUS = "/status/";

    private static final String USERSTORAGE = "users/";
    private static final String USERSTORAGE_IMAGE = "/image/";

    private static String RECHARGEHISTORY = "recharge_histories/";

    public static String USER(String userId) {
        return USER + userId;
    }

    public static String USER_BALANCE(String userId) {
        return USER + userId + USER_BALANCE;
    }

    public static String USER_ROLE(String userId) {
        return USER + userId + USER_ROLE;
    }

    public static String USER_IMAGE(String userId) {
        return USER + userId + USER_IMAGE;
    }

    public static String USER_NAME(String userId) {
        return USER + userId + USER_NAME;
    }

    public static String USER_EMAIL(String userId) {
        return USER + userId + USER_EMAIL;
    }

    public static String USER_GENDER(String userId) {
        return USER + userId + USER_GENDER;
    }

    public static String USER_BIRTHDAY(String userId) {
        return USER + userId + USER_BIRTHDAY;
    }

    public static String ROLE(int roleId) {
        return ROLE + roleId;
    }

    public static String CATEGORY() {
        return CATEGORY;
    }

    public static String CATEGORY(String CID) {
        return CATEGORY + CID;
    }

    public static String PRODUCT() {
        return PRODUCT;
    }

    public static String USERSTORAGE_IMAGE(String photoId) {
        return USERSTORAGE + photoId;
    }

    public static String RECHARGEHISTORY(String userId) {
        return RECHARGEHISTORY + userId;
    }
}
