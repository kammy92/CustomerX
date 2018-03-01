package com.resultier.customerx.utils;

public class AppConfigURL {
    public static String version = "v1.0";
    //      public static String BASE_URL = "https://project-isdental-cammy92.c9users.io/api/" + version2 + "/";
    public static String BASE_URL = "http://actipatient.com/patient-engagement/api/" + version + "/";
    
    public static String URL_FORGET_PASSWORD = BASE_URL + "forgot-password";
    public static String URL_LOGIN = BASE_URL + "user/login";
    public static String URL_INIT = BASE_URL + "init/application";
    public static String URL_GET_QUESTION = BASE_URL + "survey/question";
    public static String URL_SUBMIT_RESPONSE = BASE_URL + "survey/response";
}