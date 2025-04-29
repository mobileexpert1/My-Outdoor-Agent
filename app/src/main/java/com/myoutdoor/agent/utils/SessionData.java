package com.myoutdoor.agent.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class SessionData {

    public static final String LOCAL_DATA_VERSION = "2";
    public static final int PREF_SAVED_DATA = 99;
    public static final int PREF_SAVED_DATA_2 = 100;
    public static final int PREF_KEY_VERSION = 21;
    static SessionData instance;
    public LocalData localData;
    protected String fileName;
    public static String LOGIN_SUCCESSFULL = "LOGIN_SUCCESSFULL";
    public static String  USER_ID="USER_ID";

    protected SharedPreferences preferences;
    String version = "0";
    Gson gson = new Gson();
    private Context context;
    public boolean networkNotAvailable = false;

    public SessionData() {

    }



//    public ApiInterface getApiInterface(){
////        if (!Utils.INSTANCE.isNetworkConnected(context)) {
////            context.sendBroadcast(new Intent(Constants.ACTION_CHECK_NETWORK));
////             networkNotAvailable = true;
////        }else{
////            networkNotAvailable = false;
////        }
//        return ApiClient.INSTANCE.getClient().create(ApiInterface.class);
//    }
//
//    public ApiInterface getKycApiInterface(){
////        if (!Utils.INSTANCE.isNetworkConnected(context)) {
////            context.sendBroadcast(new Intent(Constants.ACTION_CHECK_NETWORK));
////             networkNotAvailable = true;
////        }else{
////            networkNotAvailable = false;
////        }
//        return ApiClient.INSTANCE.getKycClient().create(ApiInterface.class);
//    }
//
//    public ApiInterface getWithoutAuthApiInterface(){
////        if (!Utils.INSTANCE.isNetworkConnected(context)) {
////            context.sendBroadcast(new Intent(Constants.ACTION_CHECK_NETWORK));
////             networkNotAvailable = true;
////        }else{
////            networkNotAvailable = false;
////        }
//        return ApiClient.INSTANCE.getClientWithoutAuth().create(ApiInterface.class);
//    }
//
//    public ApiInterface getClientWithBearerSessionApiInterface(){
////        if (!Utils.INSTANCE.isNetworkConnected(context)) {
////            context.sendBroadcast(new Intent(Constants.ACTION_CHECK_NETWORK));
////             networkNotAvailable = true;
////        }else{
////            networkNotAvailable = false;
////        }
//        return ApiClient.INSTANCE.getClientWithBearerSession().create(ApiInterface.class);
//    }



    public static  SessionData I() {
        if (instance == null) {
            instance = new  SessionData();
        }
        return instance;
    }

    public LocalData getLocalData() {
        return localData;
    }

    public void setLocalData(LocalData localData) {
        this.localData = localData;
    }

    public void init(Context context) {
        this.context = context;
        fileName = context.getPackageName() + ".prefFile";

       preferences= context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        version = readString(PREF_KEY_VERSION);
        String data = readString(PREF_SAVED_DATA_2);
        if (!version.equals(LOCAL_DATA_VERSION) || data.length() <= 1) {
            version = LOCAL_DATA_VERSION;
            localData = new LocalData();
            saveLocalData();

        } else {

            if (data.length() > 1) {
                try {
                    this.localData = (gson.fromJson(data, LocalData.class));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }

    }

    public void makeIntentClearHistory(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public void saveLocalData() {
        writeString(PREF_KEY_VERSION, version);

        String data = gson.toJson(localData, LocalData.class);
        writeString(PREF_SAVED_DATA_2, data);
    }

    private void writeString(int key, String value) {
        preferences.edit().putString(String.valueOf(key), value).apply();

    }

    private String readString(int key) {
        switch (key) {
            case PREF_SAVED_DATA:
                return preferences.getString(String.valueOf(key), "");
        }
        return preferences.getString(String.valueOf(key), "");
    }


    public void clearLocalData() {
        localData = new LocalData();
        saveLocalData();
    }


}
