package com.thm.hoangminh.multimediamarket.constant;

public class Constants {
    public final static String Home = "home";

    //Product
    public final static int ProductLimit = 5;
    public final static int ProductEnable = 1;
    public final static int ProductDisable = 0;
    public final static int ProductLimitInPage = 12;
    public final static String ProductTitle = "title";
    public final static String ProductStatus = "status";
    public final static int entireProductLimitInSection = 15;
    public static final int MinAgeLimit = 0;
    public static final int MaxAgeLimit = 100;

    //User
    public final static int UserEnable = 1;
    public static String UserImageDefault = "user.png";

    //
    public final static String Admin = "admin";

    //Role
    public static final int AdminRole = 0;
    public static final int ModRole = 1;
    public static final int UserRole = 2;

    //Bundle
    public static String BundleKey = "bundle";
    public static String HomeKey = "home";
    public static String BundleOptionKey = "optionKey";
    public final static String SectionIdKey = "sectionId";
    public final static String SectionTitleKey = "sectionTitle";
    public final static String CateIdKey = "cateId";
    public final static String ProductIdKey = "productId";
    public final static String UserIdKey = "userId";
    public final static String CateTitleKey = "cateTitle";
    public final static String CardObjectKey = "cardObjectKey";
    public final static String TransactionKey = "transactionKey";

    //Bundle option
    public static String AdminOption = "admin";
    public static String BookmarkOption = "bookmark";
    public static String SectionOption = "section";
    public static String UserOption="user";
    public static String SearchOption = "search";

    //Request code
    public final static String Result = "result";
    public final static int CardRequestCode = 1111;
    public final static int UserRequestCode = 2222;

    //Rating
    public final static String RatingListKey = "ratingList";
    public final static String RatingPointKey = "ratingPoint";
    public final static String RatingLimitKey = "ratingLimit";
    public final static int RatingLimit = 3;
    public final static int RatingLiked = 1;
    public final static int RatingUnlike = 0;

    //
    public final static String SearchResults = "searchResults";

    public final static int SectionLimit = 5;

    public static final String googleApiKey = "AIzaSyBzvhnvsvpM2Kpy6_2ceRthi59uJx2Lyxg";

    //Card
    public static int CardActive = 1;
    public static int CardInactive = 0;
    public static int EditCard = 0;
    public static int AddCard = 1;
    public final static int[] CardValueList = {10000, 20000, 50000, 100000, 200000, 500000};
    public final static String[] CardCategoryList = {"Viettel", "Mobiphone", "Vinaphone", "Vietnamobile", "Garena"};

    //Section
    public static int SectionProductEnable = 1;
    public static int SectionProductDisable = 0;
    public static String SectionTitle = "title";

    //Tag
    public final static int HasImageTag = 0;
    public final static int NotHasImageTag = 1;

    //Bookmark
    public static int BookMarkEnable = 1;
    public static int BookMarkDisable = 0;
    public static String AdminProductKey = "adminKey";
}