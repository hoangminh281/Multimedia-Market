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
    private static final String USER_STATUS = "/status/";

    private static final String ROLE = "roles/";
    private static final String CATEGORY = "categories/";

    private static final String PRODUCT = "products/";
    private static final String PRODUCT_STATUS = "/status/";
    private static final String PRODUCT_PRICE = "/price/";
    private static final String PRODUCT_RATING = "/rating/";
    private static final String PRODUCT_CATEID = "/cateId/";

    private static final String FILE = "files/";

    private static final String CARD = "cards/";
    private static final String CARD_STATUS = "/status/";

    private static final String USERSTORAGE = "users/";
    private static final String USERSTORAGE_IMAGE = "/image/";

    private static final String RECHARGEHISTORY = "recharge_histories/";

    private static final String PRODUCTSTORAGE_IMAGE = "products/";

    private static final String SECTION = "sections/";
    private static String SECTION_PRODUCTIDARR = "/productIdArr/";

    private static final String PRODUCTDETAIL = "product_detail/";
    private static final String PRODUCTDETAIL_CAPACITY = "/capacity/";
    private static final String PRODUCTDETAIL_IMAGELIST = "/imageIdList/";
    private static final String PRODUCTDETAIL_FILE = "/fileId/";
    private static final String PRODUCTDETAIL_PURCHASEDQUANTITY = "/downloaded/";

    private static final String PRODUCTDETAILSTORAGE_IMAGE = "products/";

    private static final String BOOKMARK = "bookmark/";

    private static final String PURCHASEDPRODUCT = "purchased_product/";

    private static final String RATING = "rating/";
    private static String RATING_LIKEDLIST = "/likedList/";

    public static String USER() {
        return USER;
    }

    public static String USER(String userId) {
        return USER + userId;
    }

    public static String USER_BALANCE(String userId) {
        return USER(userId) + USER_BALANCE;
    }

    public static String USER_ROLE(String userId) {
        return USER(userId) + USER_ROLE;
    }

    public static String USER_IMAGE(String userId) {
        return USER(userId) + USER_IMAGE;
    }

    public static String USER_NAME(String userId) {
        return USER(userId) + USER_NAME;
    }

    public static String USER_EMAIL(String userId) {
        return USER(userId) + USER_EMAIL;
    }

    public static String USER_GENDER(String userId) {
        return USER(userId) + USER_GENDER;
    }

    public static String USER_BIRTHDAY(String userId) {
        return USER(userId) + USER_BIRTHDAY;
    }

    public static String USER_STATUS(String userId) {
        return USER(userId) + USER_STATUS;
    }

    public static String ROLE() {
        return ROLE;
    }

    public static String ROLE(int roleId) {
        return ROLE + roleId;
    }

    public static String CATEGORY() {
        return CATEGORY;
    }

    public static String CATEGORY(String cateId) {
        return CATEGORY + cateId;
    }

    public static String PRODUCT() {
        return PRODUCT;
    }

    public static String PRODUCT(String productId) {
        return PRODUCT + productId;
    }

    public static String PRODUCT_STATUS(String productId) {
        return PRODUCT(productId) + PRODUCT_STATUS;
    }

    public static String PRODUCT_RATING(String productId) {
        return PRODUCT(productId) + PRODUCT_RATING;
    }

    public static String PRODUCT_PRICE(String productId) {
        return PRODUCT(productId) + PRODUCT_PRICE;
    }

    public static String PRODUCT_CATEID(String productId) {
        return PRODUCT(productId) + PRODUCT_CATEID;
    }

    public static String RECHARGEHISTORY(String userId) {
        return RECHARGEHISTORY + userId;
    }

    public static String RECHARGEHISTORY(String userId, String transactionId) {
        return RECHARGEHISTORY(userId) + "/" + transactionId;
    }

    public static String SECTION(String cateId) {
        return SECTION + cateId;
    }

    public static String SECTION(String cateId, String sectionId) {
        return SECTION + cateId + "/" + sectionId + SECTION_PRODUCTIDARR;
    }

    public static String SECTION(String cateId, String sectionId, String productId) {
        return SECTION(cateId, sectionId) + productId;
    }

    public static String SECTION_PRODUCTIDARR(String productId) {
        return SECTION_PRODUCTIDARR + productId;
    }

    public static String FILE(String fileId) {
        return FILE + fileId;
    }

    public static String PRODUCTDETAIL() {
        return PRODUCTDETAIL;
    }

    public static String PRODUCTDETAIL(String productId) {
        return PRODUCTDETAIL + productId;
    }

    public static String PRODUCTDETAIL_PURCHASEDQUANTITY(String productId) {
        return PRODUCTDETAIL(productId) + PRODUCTDETAIL_PURCHASEDQUANTITY;
    }

    public static String PRODUCTDETAIL_IMAGELIST(String productId, String imageId) {
        return PRODUCTDETAIL(productId) + PRODUCTDETAIL_IMAGELIST + imageId;
    }

    public static String PRODUCTDETAIL_IMAGELIST(String productId) {
        return PRODUCTDETAIL(productId) + PRODUCTDETAIL_IMAGELIST;
    }

    public static String PRODUCTDETAIL_FILE(String productId) {
        return PRODUCTDETAIL(productId) + PRODUCTDETAIL_FILE;
    }

    public static String PRODUCTDETAIL_CAPACITY(String productId) {
        return PRODUCTDETAIL(productId) + PRODUCTDETAIL_CAPACITY;
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

    public static String BOOKMARK(String userId, String cateId) {
        return BOOKMARK + userId + "/" + cateId;
    }

    public static String BOOKMARK(String userId, String cateId, String productId) {
        return BOOKMARK(userId, cateId) + "/" + productId;
    }

    public static String PURCHASEDPRODUCT(String userId, String cateId) {
        return PURCHASEDPRODUCT + userId + "/" + cateId;
    }

    public static String PURCHASEDPRODUCT(String userId, String cateId, String productId) {
        return PURCHASEDPRODUCT(userId, cateId) + "/" + productId;
    }

    public static String RATING() {
        return RATING;
    }

    public static String RATING(String productId) {
        return RATING + productId;
    }

    public static String RATING(String userId, String productId) {
        return RATING + productId + "/" + userId;
    }

    public static String RATING_LIKEDLIST(String currentUserId, String userId, String productId) {
        return RATING(userId, productId) + RATING_LIKEDLIST + currentUserId;
    }

    public static String CARD() {
        return CARD;
    }

    public static String CARD(int category, int value) {
        return CARD + category + "/" + value;
    }

    public static String CARD(int category, int value, String cardId) {
        return CARD(category, value) + "/" + cardId;
    }

    public static String CARD_STATUS(int category, int value, String cardId) {
        return CARD(category, value, cardId) + CARD_STATUS;
    }
}
