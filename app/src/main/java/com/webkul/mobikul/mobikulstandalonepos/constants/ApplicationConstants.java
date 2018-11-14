package com.webkul.mobikul.mobikulstandalonepos.constants;

import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;

/**
 * Created by anchit.makkar on 19/12/17.
 */

public interface ApplicationConstants {

    int DEFAULT_BACK_PRESSED_TIME_TO_CLOSE = 2000;
    String DB_FILEPATH = "/data/" + BuildConfig.APPLICATION_ID + "/databases/db_pos.db";

    //    more fragment option ids
    int MORE_MENU_CASH_DRAWER = 1;
    int MORE_MENU_SYNC_WITH_STORE = 2;
    int MORE_MENU_SALES_AND_REPORTING = 3;
    int MORE_MENU_MY_ACCOUNT_INFO = 4;
    int MORE_MENU_CUSTOMERS = 5;
    int MORE_MENU_CATEGORIES = 6;
    int MORE_MENU_PRODUCTS = 7;
    int MORE_MENU_OPTIONS = 16;
    int MORE_MENU_GIFT_CARD = 8;
    int MORE_MENU_DISCOUNTS_AND_CART_RULES = 9;
    int MORE_MENU_TAXES = 10;
    int MORE_MENU_PAYMENT_METHODS = 11;
    int MORE_MENU_SHIPPING_METHODS = 12;
    int MORE_MENU_POS_USERS = 13;
    int MORE_MENU_USER_ROLES = 14;
    int MORE_MENU_OTHERS = 17;
    int LOG_OUT = 15;

    // pdf alignment
    int PDF_ALIGNMENT_LEFT = 0;
    int PDF_ALIGNMENT_CENTER = 1;
    int PDF_ALIGNMENT_RIGHT = 2;
    // mail credentails
    String HOST_FOR_MAIL = "YOUR_HOST_FOR_MAIL";
    String USERNAME_FOR_SMTP = "YOUR_SMTP_USER_NAME";
    String PASSWORD_FOR_SMTP = "YOUR_SMTP_PASSWORD";

    //    error messages
    String ERROR_MSG = "Something went wrong";
    String ERROR_MSG_2 = "Wrong Credentials";

    //success message
    String SUCCESS_MSG_1_SIGN_UP = "Welcome to Mobikul POS";
    String SUCCESS_MSG_2_SIGN_IN = "You have Successfully loggedIn!!";
    //category msgs
    String SUCCESS_MSG_3_ADD_CATEGORY = "Category has been created successfully.";
    String SUCCESS_MSG_4_UPDATE_CATEGORY = "Category Updated.";
    String SUCCESS_MSG_5_DELETE_CATEGORY = "This Category has been deleted!";
    //product msgs
    String SUCCESS_MSG_6_ADD_PRODUCT = "Product has been added successfully!";
    String SUCCESS_MSG_7_DELETE_PRODUCT = "This Product has been deleted successfully!";
    String SUCCESS_MSG_8_UPDATE_PRODUCT = "Product Updated.";
    String SUCCESS_MSG_10_SKU_ALLREADY_EXIST = "SKU ALREADY EXIST.";

    //customer msg
    String SUCCESS_MSG_6_ADD_CUSTOMER = "Customer has been registered successfully!";
    String SUCCESS_MSG_7_DELETE_CUSTOMER = "This Customer has been deleted successfully!";
    String SUCCESS_MSG_8_UPDATE_CUSTOMER = "Customer details Updated.";
    String SUCCESS_MSG_9_CUSTOMER_ALL_READY_EXIST = "CUSTOMER ALREADY EXIST.";
    //orderplace
    String SUCCESS_MSG_9_ORDER_PLACED = "Order placed successfully.";
    String SUCCESS_MSG_7 = "Wrong Credentials";
    String SUCCESS_MSG = "Success";
    //hold
    String SUCCESS_MSG_1_ADD_HOLD_CART = "You cart data is successfully added in hold cart.";
    String SUCCESS_MSG_2_DELETE_HOLD_CART = "Deleted.";

    //    admin
    String SUCCESS_MSG_1_UPDATE_ADMIN_DETAILS = "Details Updated.";

    //option
    String SUCCESS_MSG_1_ADD_OPTION = "Success: You have added option successfully.";
    String SUCCESS_MSG_2_DELETE_OPTION = "This Options has been deleted!";
    String SUCCESS_MSG_3_UPDATE_OPTION = "Option Updated.";
    //tax
    String SUCCESS_MSG_1_ADD_TAX_RATE = "Success: You have added tax rate successfully.";
    String SUCCESS_MSG_2_DELETE_TAX = "This tax rate has been deleted!";
    String SUCCESS_MSG_3_UPDATE_TAX = "Tax rate Updated.";


    //    error codes
    int ERROR_CODE = 1;

}
