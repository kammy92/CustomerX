package com.resultier.customerx.utils;

public class Constants {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static String app_link_uri = "android-app://com.actiknow.bloodkonnect/http/host/path";
    public static String font_name = "AvenirNextLTPro-Regular.otf";
    
    public static double latitude = 0.0;
    public static double longitude = 0.0;
    public static int splash_screen_first_time = 0; // 0 => default
    
    public static boolean show_log = true;
    public static String server_time = "";
    public static String location_tagging_start_time = "08:00";
    public static String location_tagging_end_time = "20:00";
    public static String api_key = "ebaa3e8571444005cc0b609824e90dc8";
    
    //        public static String sms_provider = "+917011396606";
    public static String sms_provider = "INSPLY";
}