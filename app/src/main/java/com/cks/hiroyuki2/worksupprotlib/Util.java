/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cks.hiroyuki2.worksupportlib.R;
import com.cks.hiroyuki2.worksupprotlib.Entity.RecordData;
import com.cks.hiroyuki2.worksupprotlib.Entity.TimeEvent;
import com.cks.hiroyuki2.worksupprotlib.Entity.TimeEventDataSet;
import com.cks.hiroyuki2.worksupprotlib.Entity.User;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Contract;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 便利屋おじさん！
 */
public class Util {
    public static final String datePattern = "yyyyMMdd";
    public static final String delimiter = "9mVSv";
    public static final String delimiterOfNum = ",";

    public final static float ELEVATION = 4;
    public final static String PREF_NAME = "pref";
    public final static String PREF_KEY_TEMPLATE = "template";
    public final static String PREF_KEY_FIRST_LAUNCH = "FIRST_LAUNCH";
    public final static String PREF_KEY_PW ="PREF_KEY_PW";
    public final static String PREF_KEY_PROF = "prof_params";
    public final static String PREF_KEY_TEMPLATE_DES = "template_des";
    public final static String PREF_KEY_COLOR = "color";
    public final static String PREF_KEY_START_OF_WEEK = "start_of_week";
    public  final static String PREF_KEY_PHOTO_UPLOADED = "photo_uploaded";//最初はこの値はfalseとなっている。一旦画像を設定すると、trueになる。
    public final static String PREF_KEY_WIDTH = "width";
//    public final static String PREF_KEY_GROUP_USER_DATA = "group_in_userdata_node";
    public final static String TEMPLATE_SERIALIZE = "template.bat";
    public final static String TEMPLATE_SERIALIZE_DEFAULT = "template_def.bat";
    public final static String QR_FILE_NAME = "uid_qr.img";
    public final static String PREF_KEY_PARAM_RADIO_ID = "param_radio_id";
    public final static String DATE_PATTERN_YM = "yyyyMM";
    public final static String DATE_PATTERN_DOT_YM = "yyyy.MM";
    public final static String DATE_PATTERN_COLON_HM = "HH:mm";
    public final static String DATE_PATTERN_DOT_YMD = "yyyy.MM.dd";
    public final static String DATE_PATTERN_DOT_MD = "MM.dd";
    public final static String DATE_PATTERN_SLASH_MD = "M/dd";

    public final static String DEFAULT = "DEFAULT";
    public final static String UNSET_NAME = "名称未設定";
    public final static String PARAM_ITEM_LEN = "PARAM_ITEM_LEN";

    public final static String PREF_KEY_QR_PW = "qr_pw";
    public final static List<String> listParam = Util.makeParam();

    public final static int PARAMS_SLIDER_MAX_MAX = 10;

    public final static String TEMPLATE_TIME_PAIR = "TEMPLATE_TIME_PAIR";
    public final static int CALLBACK_TEMPLATE_TIME_PAIR = 1000;
    public final static String TEMPLATE_TIME_PAIR_DES = "TEMPLATE_TIME_PAIR_DES";
    public final static int CALLBACK_TEMPLATE_TIME_PAIR_DES = 1010;
    public final static String TEMPLATE_TIME_COLOR = "TEMPLATE_TIME_COLOR";
    public final static String COLOR_NUM = "COLOR_NUM";
    public final static int CALLBACK_TEMPLATE_TIME_COLOR = 1020;
    public final static String CARD_INT = "CARD_INT";
//    final static String TEMPLATE_TAG_ADD = "TEMPLATE_TAG_ADD";
//    final static int CALLBACK_TEMPLATE_TAG_ADD = 1050;
    public final static String TEMPLATE_TAG_EDIT ="TEMPLATE_TAG_EDIT";
    public final static int CALLBACK_TEMPLATE_TAG_EDIT = 1060;
    public final static String PARAMS_NAME = "PARAMS_NAME";
    public final static int CALLBACK_PARAMS_NAME = 1100;
    public final static String TEMPLATE_PARAMS_NAME = "TEMPLATE_PARAMS_NAME";
    public final static int CALLBACK_TEMPLATE_PARAMS_NAME = 1110;
    public final static String PARAMS_VALUES = "PARAMS_VALUES";
    public final static String TEMPLATE_PARAMS_SLIDER_MAX = "TEMPLATE_PARAMS_SLIDER_MAX";
    public final static int CALLBACK_TEMPLATE_PARAMS_SLIDER_MAX = 1120;
    public final static String TEMPLATE_PARAMS_ITEM = "TEMPLATE_PARAMS_ITEM";
    public final static int CALLBACK_TEMPLATE_PARAMS_ITEM = 1130;
    public final static String TEMPLATE_PARAMS_ADD = "TEMPLATE_PARAMS_ADD";
    public final static int CALLBACK_TEMPLATE_PARAMS_ADD = 1140;
    public static final String UID = "UID";
    public static final String IS_DATA_MINE ="IS_DATA_MINE";
    public static final int NOTIFICATION_ID = 15973;
    public static final String NOTIFICATION_CHANNEL = "CHANNEL_0";
    public static final int NOTIFICATION_ID_SECOND = 15974;
    public static final String NOTIFICATION_CHANNEL_SECOND = "CHANNEL_1";
    public static final String LIST_MAP_HOUR = "HOUR";
    public static final String LIST_MAP_MIN = "MIN";
    public static final String LIST_MAP_VALUE = "VALUE";
    public static final String INDEX = "INDEX";
    public static final int RC_SIGN_IN = 100;
    public static final String TIME_EVENT_RANGE = "TIME_EVENT_RANGE";

    //region Dialog周りの定数
    public static final String INTENT_KEY_ISSUCCESS = "IKI";
    public static final String INTENT_KEY_METHOD = "IKM";
    public static final String INTENT_KEY_NEW_PARAM = "INTENT_KEY_NEW_PARAM";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    //endregion

    final static String storageRoot = "gs://worksupprotlib.appspot.com/";
    public static final String URL_SHORTEN_API = "https://www.googleapis.com/urlshortener/v1/url"
            + "?key=AIzaSyDDrs60sI8h7JeNjR-VTAJcbdnwRB5bVrk";

    private static final String TAG = "MANUAL_TAG: " + Util.class.getSimpleName();

    //region Calender⇔Date
    public static String cal2date(@NonNull Calendar cal, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    public static Calendar date2Cal(String string, String pattern) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(string));
        return cal;
    }
    //endregion

    @Nullable
    public static String convertCalPattern(String calStr, String prePattern, String toPattern){
        Calendar cal;
        try {
            cal = date2Cal(calStr, prePattern);
        } catch (ParseException e) {
            logStackTrace(e);
            return null;
        }
        return cal2date(cal, toPattern);
    }

    //region px, dp, spなどの変換まわり
    public static int dp2px(@NonNull Context context, int dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (dp * scale + 0.5f);
    }

    public static float px2dp(@NonNull Context context, int px){
        final float scale = context.getResources().getDisplayMetrics().density;
        return px/scale;
    }

    public static float sp2px(float sp, @NonNull Context context) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float px2sp(float px, @NonNull Context content){
        return px / content.getResources().getDisplayMetrics().scaledDensity;
    }
    //endregion

    @NonNull
    public static String time2String(int hour, int min){
        StringBuilder sb = new StringBuilder();
        modifyDigit(sb, hour);
        sb.append(":");
        modifyDigit(sb, min);
        return sb.toString();
    }

    private static void modifyDigit(StringBuilder sb, int num){
        if (num == 0){
            sb.append("00");
        } else if (num<10){
            sb.append("0").append(num);
        } else {
            sb.append(num);
        }
    }

    //scrollViewのスクショ撮るときに使ってください
    public static Bitmap loadBitmapFromView(@NonNull View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    public static RecyclerView setRecycler(@NonNull Context context, @NonNull View view, @NonNull RecyclerView.Adapter adapter, int id){
        RecyclerView recyclerView = view.findViewById(id);
        recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    //region guava代替まわり
    /**************************** guava代替まわり ここから ***************************/
    @Contract("null, null -> true; null, !null -> false; !null, null -> false")
    public static boolean nullableEqual(@Nullable Object x, @Nullable Object y){
        if (x == null && y == null) return true;

        if (x == null || y == null) return false;

        return x.equals(y);
    }

    public static boolean setStrEqual(Set<String> x, Set<String> y){
        Set<String> subX = new HashSet<>(x);
        Set<String> subY = new HashSet<>(y);
        subX.removeAll(x);
        subY.removeAll(y);
        return subX.isEmpty() && subY.isEmpty();
    }

    private static List<String> makeParam(){
        List<String> list = new ArrayList<>();
        list.add("動悸・汗をかく");
        list.add("焦り");
        list.add("自殺願望");
        return list;
    }

    @NonNull
    public static String joinArr(@NonNull String[] strings, @NonNull String delimiter){
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
            sb.append(delimiter);
        }
        sb.delete(sb.lastIndexOf(delimiter), sb.length());
        return sb.toString();
    }

    @NonNull
    public static String joinArr(@NonNull String[] strings){
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }

    /**************************** guava代替 ここまで ***************************/
    //endregion

    //region data⇔Bundle
    /**************************** data⇔Bundle **********************************/

    /**
     * @return Nonnullかつ、エラー時にはemptyであることに注意してください
     */
    @NonNull
    public static List<Bundle> data2Bundle(@NonNull RecordData data){
        Log.d(TAG, "setDateData: fire");

        List<Bundle> list = new ArrayList<>();
        if (data.data == null || data.data.isEmpty())
            return list;

        List<String> keyList = new ArrayList<>(data.data.keySet());
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String x, String y) {
                String[] timeX = x.split(":");
                String[] timeY = y.split(":");
                if (!timeX[0].equals(timeY[0]))
                    return Integer.parseInt(timeX[0]) - Integer.parseInt(timeY[0]);
                else
                    return Integer.parseInt(timeX[1]) - Integer.parseInt(timeY[1]);
            }
        });

        int i =0;
        for (String key : keyList) {
            Bundle bundle = new Bundle();
            String[] time = key.split(":");
            bundle.putInt(LIST_MAP_HOUR, Integer.parseInt(time[0]));
            bundle.putInt(LIST_MAP_MIN, Integer.parseInt(time[1]));
            String value = (String)data.data.get(key);
            bundle.putString(LIST_MAP_VALUE, value);
            bundle.putInt(INDEX, i);
            list.add(bundle);
            i++;
        }

        return list;
    }

    @NonNull
    public static RecordData bundle2Data(List<Bundle> bundles, @Nullable String dataName, int dataType, int year, int mon, int day){
        RecordData data = new RecordData();
        data.dataName = dataName;
        data.dataType = dataType;
        data.year = year;
        data.mon = mon;
        data.day = day;
        data.data = innerBundle2Data(bundles);
        return data;
    }

    @NonNull
    public static RecordData bundle2Data(List<Bundle> bundles, @Nullable String dataName, int dataType, @NonNull Calendar cal){
        RecordData data = new RecordData();
        data.dataName = dataName;
        data.dataType = dataType;
        data.year = cal.get(Calendar.YEAR);
        data.mon = cal.get(Calendar.MONTH);
        data.day = cal.get(Calendar.DATE);
        data.data = innerBundle2Data(bundles);
        return data;
    }

    @NonNull
    private static HashMap<String, Object> innerBundle2Data(@NonNull List<Bundle> bundles){
        HashMap<String, Object> hashMap = new HashMap<>(bundles.size());
        for (Bundle bundle : bundles) {
            int hour = bundle.getInt(LIST_MAP_HOUR);
            int min = bundle.getInt(LIST_MAP_MIN);
            String value = bundle.getString(LIST_MAP_VALUE);
            String timeStr = Util.time2String(hour, min);
            hashMap.put(timeStr, value);
        }
        return hashMap;
    }

    /**************************** data⇔Bundle ここまで**********************************/
    //endregion

    //region data⇔BundleParams
    /**************************** data⇔BundleParams ここから**********************************/

    /**
     * @return Nonnullかつ、エラー時にはemptyであることに注意してください
     */
    @NonNull
    public static List<Bundle> data2BundleParams(@NonNull RecordData data){
        List<Bundle> list = new ArrayList<>();
        if (data.data == null || data.data.isEmpty())
            return list;

        int i=0;
        for (String key : data.data.keySet()) {
            Bundle bundle = new Bundle();
            String value = (String) data.data.get(key);
            String[] values = value.split(delimiter);
            bundle.putStringArray(PARAMS_VALUES, values);
            bundle.putInt(INDEX, i);
            list.add(bundle);
            i++;
        }
        return list;
    }

    public static RecordData bundle2DataParams(@NonNull List<Bundle> bundles, @Nullable String dataName, int year, int mon, int day){
        RecordData data = new RecordData();
        data.year = year;
        data.mon = mon;
        data.day = day;
        data.dataType = 3;
        data.dataName = dataName;
        data.data = innerBundle2DataParams(bundles);
        return data;
    }

    public static RecordData bundle2DataParams(@NonNull List<Bundle> bundles, @Nullable String dataName, Calendar cal){
        RecordData data = new RecordData();
        data.year = cal.get(Calendar.YEAR);
        data.mon = cal.get(Calendar.MONTH);
        data.day = cal.get(Calendar.DATE);
        data.dataType = 3;
        data.dataName = dataName;
        data.data = innerBundle2DataParams(bundles);
        return data;
    }

    private static HashMap<String, Object> innerBundle2DataParams(@NonNull List<Bundle> bundles){
        HashMap<String, Object> data = new HashMap<>();
        for (int i = 0; i < bundles.size(); i++) {
            String[] strings = bundles.get(i).getStringArray(PARAMS_VALUES);
            if (strings == null)
                continue;
            String string = Util.joinArr(strings, delimiter);
            data.put(Integer.toString(i), string);
        }
        return data;
    }

    /**************************** data⇔BundleParams ここまで**********************************/
    //endregion

    public static void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public static void logAnalytics(@NonNull String eventName, @NonNull Context context){
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(context);
        analytics.logEvent(eventName, new Bundle());
    }

    public static void logStackTrace(@NonNull Exception e){
        e.printStackTrace();
        Crashlytics.logException(e);
    }

    /**
     * Fabricを必ずセットアップしないと、落ちます。
     */
    public static void onError(@NonNull Context context, @NonNull String string, @StringRes int toastRes){
        Log.w(TAG, string);
        logAnalytics(string, context);
        Answers.getInstance().logCustom(new CustomEvent(string));
        Toast.makeText(context, toastRes, Toast.LENGTH_LONG).show();
    }

    /**
     * Fabricを必ずセットアップしないと、落ちます。
     */
    public static void onError(@NonNull Fragment fragment, @NonNull String string, @StringRes int toastRes){
        if (fragment.getContext() == null)
            return;
        onError(fragment.getContext(), string, toastRes);
    }

    /**
     * Fabricを必ずセットアップしないと、落ちます。
     */
    public static void onError(@NonNull Context context, @NonNull String string, @Nullable String toast){
        Log.w(TAG, string);
        logAnalytics(string, context);
        Answers.getInstance().logCustom(new CustomEvent(string));
        if (toast != null)
            Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    /**
     * Fabricを必ずセットアップしないと、落ちます。
     */
    public static void onError(@NonNull Fragment fragment, @NonNull String string, @Nullable String toast){
        if (fragment.getContext() == null)
            return;
        onError(fragment.getContext(), string, toast);
    }

    public static void toastNullable(@Nullable Context context, @StringRes int stringRes){
        if (context == null)
            return;
        Toast.makeText(context, stringRes, Toast.LENGTH_LONG).show();
    }

    public static void toastNullable(@Nullable Context context, @NonNull String string){
        if (context == null)
            return;
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    @Contract(pure = true)
    @NonNull
    public static String getTextNullable(@Nullable String txt, @NonNull String subTxt){
        return txt == null ? subTxt : txt;
    }

    /**
     * @see "https://goo.gl/f6kZaG"
     */
    public static String getExtension(@NonNull Context context, Uri uri){
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static String getMimeType(@NonNull Context context, Uri uri){
        ContentResolver cR = context.getContentResolver();
        return cR.getType(uri);
    }

    //region setImgFromStorage
    public static void setImgFromStorage(@NonNull User user, ImageView iv, @DrawableRes int errorDrw){
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("null")){
            setImgWithPicasso(user.getPhotoUrl(), errorDrw, iv);
        } else {
            iv.setImageResource(errorDrw);
        }
    }

    public static void setImgFromStorage(@NonNull FirebaseUser user, ImageView iv, @DrawableRes int errorDrw){
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().toString().equals("null")){
            setImgWithPicasso(user.getPhotoUrl(), errorDrw, iv);
        } else {
            iv.setImageResource(errorDrw);
        }
    }

    public static void setImgFromStorage(@Nullable String photoUrl, ImageView iv, @DrawableRes int errorDrw){
        if (photoUrl != null && !photoUrl.equals("null")){
            setImgWithPicasso(photoUrl, errorDrw, iv);
        } else {
            iv.setImageResource(errorDrw);
        }
    }

    private static void setImgWithPicasso(Uri uri, @DrawableRes int errorImg, ImageView target){
        Picasso.get()
                .load(uri)
                .error(errorImg)
                .into(target);
    }

    private static void setImgWithPicasso(String uri, @DrawableRes int errorImg, ImageView target){
        Picasso.get()
                .load(uri)
                .error(errorImg)
                .into(target);
    }
    //endregion


    //region safにintent送る
    public static void kickSaf(@NonNull Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void kickSaf(@NonNull Fragment fragment, int requestCode){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fragment.startActivityForResult(intent, requestCode);
    }
    //endregion

    @Nullable
    public static String getFileName(Context context, @NonNull Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static void intentKicker(@NonNull Context context, @NonNull String contentName, @NonNull Uri uri, @NonNull String action, @Nullable String type){
        final Intent share = new Intent(action);
        share.putExtra(Intent.EXTRA_SUBJECT, contentName);
        share.putExtra(Intent.EXTRA_TEXT, uri.toString());
        if (type != null)
            share.setDataAndType(uri, type);
        context.startActivity(share);
    }

    public static void setNullableText(@NonNull TextView tv, @Nullable String string){
        if (string != null)
            tv.setText(string);
    }

    @Nullable
    static String getNullableText(@NonNull EditText editText){
        Editable e = editText.getText();
        if (e == null)
            return null;
        return e.toString();
    }

    @Nullable
    public static String getNullableText(@NonNull TextView textView){
        CharSequence c = textView.getText();
        if (c == null)
            return null;
        return c.toString();
    }

    public static void toast(@NonNull Context context, boolean succeed, @StringRes int trueStr, @StringRes int falseStr){
        if (succeed)
            Toast.makeText(context, trueStr, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, falseStr, Toast.LENGTH_LONG).show();
    }

    public static void kickIntentIcon(@NonNull Fragment fragment, int intentKey){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fragment.getActivity().startActivityFromFragment(fragment, intent, intentKey);
    }

    public static void kickIntentIcon(@NonNull Activity activity, int intentKey){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent, intentKey);
    }

    @NonNull
    public static String makeScheme(@NonNull String... params){
        StringBuilder sb = new StringBuilder();
        for (String param: params) {
            sb.append(param);
            sb.append("/");
        }
        return sb.toString();
    }

    @Nullable
    public static FirebaseUser getUserMe(){
       return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void initRecycler(@NonNull Context context, @NonNull RecyclerView rv, RecyclerView.Adapter adapter){
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(adapter);
    }

    public static Calendar getCalFromTimeEvent(@NonNull TimeEvent timeEvent){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, timeEvent.getHour());
        calendar.set(Calendar.MINUTE, timeEvent.getMin());
        calendar.add(Calendar.DATE, timeEvent.getOffset());
        return calendar;
    }

    @Contract("null -> null") @Nullable
    public static TimeEventDataSet getTimeEveDataSetFromRecordData(@Nullable RecordData recordData){
        if (recordData == null)
            return null;

        String string = (String) recordData.data.get("0");
        if (string == null)
            return null;

        return new Gson().fromJson(string, TimeEventDataSet.class);
    }

    @NonNull
    public static String getStrOffset(@NonNull Context context, @NonNull TimeEvent timeEvent){
        if (timeEvent.getOffset() <0){
            return context.getString(R.string.yesterday);
        } else if (timeEvent.getOffset() >0){
            return context.getString(R.string.tomorrow);
        } else {
            return "";
        }
    }

    @Nullable
    public static RecordData getRecordDataByType(@NonNull List<RecordData> dataList, int dataType){
        for (RecordData data: dataList) {
            if (data.getDataType() == dataType){
                return data;
            }
        }
        return null;
    }

    @Contract("null, _, _ -> null")
    @Nullable
    public static View makeCircleAndTxt(@Nullable Context context, @NonNull String value, int colorNum){
        if (context == null) return null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return null;

        View view = inflater.inflate(R.layout.analytics_table_tag, null);
        TextView tv = view.findViewById(R.id.tv);
        tv.setText(value);
        int colorId = UtilSpec.colorId.get(colorNum);
        int color = ContextCompat.getColor(context, colorId);
        ImageView iv = view.findViewById(R.id.circle);
        iv.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC);
        return view;
    }

    public static Calendar getCopyOfCal(@NonNull Calendar target){
        Calendar cal = Calendar.getInstance();
        cal.setTime(target.getTime());
        return cal;
    }

    @NonNull
    public static String cal2DateWithSlash(@NonNull Calendar cal, @NonNull Context context){
        String md = cal2date(cal, DATE_PATTERN_SLASH_MD);
        String[] dof = context.getResources().getStringArray(R.array.dof);
        return md +" "+ dof[cal.get(Calendar.DAY_OF_WEEK)-1];//Calendar.DAY_OF_WEEK-1は1はじまり
    }

    public static int getToolBarHeight(@NonNull Context context) {
        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return toolBarHeight;
    }

    @Contract(pure = true)
    public static int getMonthIllust(@IntRange(from = 0, to = 11) int mon){
        switch (mon){
            case 0:
                return R.drawable.january;
            case 1:
                return R.drawable.february;
            case 2:
                return R.drawable.march;
            case 3:
                return R.drawable.april;
            case 4:
                return R.drawable.may;
            case 5:
                return R.drawable.june;
            case 6:
                return R.drawable.july;
            case 7:
                return R.drawable.august;
            case 8:
                return R.drawable.september;
            case 9:
                return R.drawable.october;
            case 10:
                return R.drawable.november;
            case 11:
                return R.drawable.december;
            default:
                return -1;
        }
    }

    //region Ntfまわり
    public static void showUploadingNtf(Class target, Context context, UploadTask.TaskSnapshot taskSnapshot, String fileName, int id){
        String text = context.getString(R.string.msg_start_upload);
        NotificationCompat.Builder builder = createNtfBase(target, context, fileName, text, id)
                .setAutoCancel(false)
                .setProgress((int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred(), false);
        if (SDK_INT >= 21)
            builder.setCategory(Notification.CATEGORY_PROGRESS);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        showNtf(context, id, notification);
    }

    public static void showDownloadingNtf(Class target, Context context, FileDownloadTask.TaskSnapshot taskSnapshot, String fileName, int id){
        String text = context.getString(R.string.msg_succeed_download);
        NotificationCompat.Builder builder = createNtfBase(target, context, fileName, text, id)
                .setAutoCancel(false)
                .setProgress((int) taskSnapshot.getTotalByteCount(), (int) taskSnapshot.getBytesTransferred(), false);
        if (SDK_INT >= 21)
            builder.setCategory(Notification.CATEGORY_PROGRESS);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        showNtf(context, id, notification);
    }

    public static void showCompleteNtf(Class target, Context context, String fileName, int id, @StringRes int textRes){
        String text = context.getString(textRes);
        NotificationCompat.Builder builder = createNtfBase(target, context, fileName, text, id);
        if (SDK_INT >= 21)
            builder.setCategory(Notification.CATEGORY_STATUS);
        showNtf(context, id, builder.build());
    }

    private static PendingIntent createPendingIntent(Context context, int id, Class target){
        Intent intent = new Intent(context, target);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP  // 起動中のアプリがあってもこちらを優先する
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED  // 起動中のアプリがあってもこちらを優先する
                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS  // 「最近利用したアプリ」に表示させない
        );
        return PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
    }

    private static NotificationCompat.Builder createNtfBase(Class target, Context context, String fileName, String text, int id){
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_cloud_upload_white_24dp)// TODO: 2017/11/19 これ直すこと
                .setContentTitle(fileName)
                .setContentText(text)
                .setTicker(text)
                .setContentIntent(createPendingIntent(context, id, target));
    }

    private static void showNtf(Context context, int id, Notification ntf){
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(id, ntf);
    }

    public static void showFcmMsg(String messageBody, Context context, Class target) {
        final int ntfId = (int)System.currentTimeMillis();
        String title = context.getString(R.string.app_name);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_SECOND)
                        .setSmallIcon(R.drawable.ic_info_white_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setTicker(title)
                        .setContentIntent(createPendingIntent(context, ntfId, target));

        if (SDK_INT >= 21)
            notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);

        showNtf(context, ntfId, notificationBuilder.build());
    }
    //endregion

    @Nullable @Contract(pure = true)
    public static String checkAdmittionAsMember(@Nullable DataSnapshot dataSnapshot, @NonNull String uid){
        if (dataSnapshot == null)
            return "dataSnapshot == null";
        if (!dataSnapshot.exists())
            return "!dataSnapshot.exists() グループ消滅？";
        if (!dataSnapshot.hasChild("member")){
            return "!dataSnapshot.hasChild(\"member\")　グループ消滅？";
        }

        DataSnapshot memberSnap = dataSnapshot.child("member");
        if (!memberSnap.hasChild(uid)){
            return "!dataSnapshot.hasChild(uid) グループを既に退会？";
        }
        if (!memberSnap.child(uid).child("isChecked").getValue(Boolean.class)){
            return "グループ未加入？";
        }
        return null;
    }

    /**
     * @return 例外時Integer.MAX_VALUE
     */
    public static int getPosFromUid(@NonNull List<User> userList, @NonNull String uid){
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserUid().equals(uid)){
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
}
