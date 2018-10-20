package com.thm.hoangminh.multimediamarket.api;

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

    private static String PRODUCTSTORAGE_IMAGE = "products/";

    private static String SECTION = "sections/";

    private static String PRODUCTDETAIL = "product_detail/";
    private static String PRODUCTDETAIL_CAPACITY = "/capacity/";
    private static String PRODUCTDETAIL_IMAGELIST = "/imageList/";
    private static final String PRODUCTDETAIL_FILE = "/downloadLink/";
    private static String PRODUCTDETAIL_PURCHASEDQUANTITY = "/downloaded/";

    private static String PRODUCTDETAILSTORAGE_IMAGE = "products/";

    private static String BOOKMARK = "bookmark/";

    private static Object PURCHASEDPRODUCT = "purchased_product/";

    private static String RATING = "rating/";

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

    public static String PRODUCT(String productId) {
        return PRODUCT + productId;
    }

    public static String RECHARGEHISTORY(String userId) {
        return RECHARGEHISTORY + userId;
    }

    public static String SECTION(String cateId) {
        return SECTION + cateId;
    }

    public static String FILE(String fileId) {
        return FILE + fileId;
    }

    public static String PRODUCTDETAIL(String productId) {
        return PRODUCTDETAIL + productId;
    }

    public static String PRODUCTDETAIL_PURCHASEDQUANTITY(String productId) {
        return PRODUCTDETAIL + productId + PRODUCTDETAIL_PURCHASEDQUANTITY;
    }

    public static String PRODUCTDETAIL_IMAGELIST(String productId, String imageId) {
        return PRODUCTDETAIL + productId + PRODUCTDETAIL_IMAGELIST + imageId;
    }

    public static String PRODUCTDETAIL_IMAGELIST(String productId) {
        return PRODUCTDETAIL + productId + PRODUCTDETAIL_IMAGELIST;
    }

    public static String PRODUCTDETAIL_FILE(String productId) {
        return PRODUCTDETAIL + productId + PRODUCTDETAIL_FILE;
    }

    public static String PRODUCTDETAIL_CAPACITY(String productId) {
        return PRODUCTDETAIL + productId + PRODUCTDETAIL_CAPACITY;
    }

    public static String USERSTORAGE_IMAGE(String photoId) {
        return USERSTORAGE + photoId;
    }

    public static String PRODUCTDETAILSTORAGE_IMAGE(String photoId) {
        return PRODUCTDETAILSTORAGE_IMAGE + photoId;
    }

    public static String PRODUCTSTORAGE_IMAGE(String productImageId) {
        return PRODUCTSTORAGE_IMAGE + productImageId;
    }

    public static String BOOKMARK(String userId, String cateId, String productId) {
        return BOOKMARK + userId + "/" + cateId + "/" + productId;
    }

    public static String PURCHASEDPRODUCT(String userId, String cateId, String productId) {
        return PURCHASEDPRODUCT + userId + "/" + cateId + "/" + productId;
    }

    public static String RATING(String productId) {
        return RATING + productId;
    }

    public static String RATING(String userId, String productId) {
        return RATING + productId + "/" + userId;
    }
}
