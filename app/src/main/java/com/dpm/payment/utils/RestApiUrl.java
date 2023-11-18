package com.dpm.payment.utils;

/*import static com.payment.BuildConfig.BASE_URL;
import static com.payment.BuildConfig.BASE_URL_MAIN;
import static com.payment.BuildConfig.BASE_URL_TERMS;*/

import androidx.core.content.ContextCompat;

import com.payment.BuildConfig;

public interface RestApiUrl {

    public static String BASE_URL ="http://wardc.org/apiv2";
    public static String BASE_URL_MAIN ="http://wardc.org/apiv2";
    public static String BASE_URL_TERMS ="https:l";


    String URL_CASHIER_LOGIN = BASE_URL + "/admin/login";
    String URL_CASHIER_SEARCH_PROPERTY= BASE_URL + "/admin/search-property";
    String URL_CASHIER_SAVE_PAYMENT= BASE_URL + "/admin/payment/";
    String URL_CASHIER_LANDLORD_EDIT_PROFILE= BASE_URL + "/admin/landlord/";

    String URL_LANDLORD_EDIT_PROFILE= BASE_URL + "/landlord/landlord/";
    String URL_LANDLORD_PROPERTY_APPROVE= BASE_URL + "/landlord/propertyapprove/";

// user login
    String URL_LANDLORD_LOGIN = BASE_URL + "/landlord/login";
    String URL_LANDLORD_OTP_VERIFY = BASE_URL + "/landlord/otp";
    String URL_LANDLORD_PROPERTY_LIST = BASE_URL + "/landlord/search-property";
    String URL_LANDLORD_PAYMENT = BASE_URL_MAIN + "/paypal?";


}
