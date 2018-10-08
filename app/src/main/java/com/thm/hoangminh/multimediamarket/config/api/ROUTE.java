package com.thm.hoangminh.multimediamarket.repository.api;

public class ROUTE {
    private static final String USER = "users/";
    private static final String USER_IMAGE = "users/";
    private static final String ROLE = "roles/";
    private static final String CATEGORY = "categories/";
    private static final String PRODUCT = "products/";

    public static String USER(String UID) {
        return USER + UID;
    }

    public static String USER_IMAGE(String UID) {
        return USER_IMAGE + UID;
    }

    public static String ROLE(int UID) {
        return ROLE + UID;
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
}
