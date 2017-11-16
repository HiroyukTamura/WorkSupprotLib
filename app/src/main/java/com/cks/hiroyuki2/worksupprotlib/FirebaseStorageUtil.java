/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cks.hiroyuki2.worksupprotlib.Entity.Content;
import com.cks.hiroyuki2.worksupprotlib.Entity.Group;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.cks.hiroyuki2.worksupprotlib.Util.logStackTrace;

/**
 * ストレージまわりを担当するおじさん！渋い！
 * このおじさんはUtilクラスなので、描画やら何やらは全部ShareBoardFragment側でやってください。
 * おじさんは裏方！
 */

public class FirebaseStorageUtil implements OnPausedListener, OnProgressListener{

    private static final String TAG = "MANUAL_TAG: " + FirebaseStorageUtil.class.getSimpleName();
    private Context context;
    private Group group;
    public static final int LIMIT_SIZE = 20 * 1000 * 1000;//storageとやりとりする容量のlimit.
    public static final int LIMIT_SIZE_PROF = 5 * 1000 * 1000;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CODE_EXTERNAL_FILE, CODE_CHASHE_FILE})
    @interface CODE_FILE {}
    public static final int CODE_EXTERNAL_FILE = 0;
    public static final int CODE_CHASHE_FILE = 1;

    public FirebaseStorageUtil(Context context, Group group){
        this.context = context;
        this.group = group;
    }

    //region url取得まわり
    public void getStorageUrl(int listPos, OnSuccessListener<Uri> onSuccessListener, OnFailureListener onFailureListener){
        final Content content = group.contentList.get(listPos);
        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("shareFile")
                .child(group.groupKey)
                .child(content.contentKey);
        ref.getDownloadUrl()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void onSuccessShortenUrl(Uri uri, Callback callback){
        ForOkHttpObj obj = new ForOkHttpObj(uri.toString());
        String json = new Gson().toJson(obj);

        okhttp3.MediaType mediaTypeJson = okhttp3.MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaTypeJson, json);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(Util.URL_SHORTEN_API).post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * @param response okHttpでgoogleの短縮urlのapiを叩いたときのレスポンス
     * @return shortenUrl
     */
    @Nullable
    public static String getShortenUrlFromRes(@NonNull Response response) throws IOException {
        String shortenUrl = null;

        ResponseBody resBody = response.body();
        if (resBody == null)
            return null;

        try {
            JSONObject jsonObject = new JSONObject(resBody.string());
            if (jsonObject.has("id")) {
                shortenUrl = (String) jsonObject.get("id");
            }
        } catch (JSONException e) {
            logStackTrace(e);
        }
        return shortenUrl;
    }

    private class ForOkHttpObj{
        private String longUrl;

        ForOkHttpObj(@NonNull String longUrl){
            this.longUrl = longUrl;
        }
    }
    //endregion

    //region ダウンロードまわり
    @Nullable
    public File createLocalFile(int listPos, @CODE_FILE int witch){
        Content content = group.contentList.get(listPos);
//        StorageReference ref = FirebaseStorage.getInstance().getReference().child("shareFile/"+group.groupKey+"/"+content.contentKey);

        File storageDir = null;
        switch (witch){
            case CODE_EXTERNAL_FILE:
                storageDir = new File(context.getFilesDir(), "Storage");
                break;
            case CODE_CHASHE_FILE:
                storageDir = new File(context.getCacheDir(), "Storage");
                break;
        }

        if(storageDir == null || !storageDir.exists() && !storageDir.mkdirs()){
            Util.onError(context, "!storageDir.exists() && !storageDir.mkdirs()", null);
            return null;
        }

        if (!storageDir.isDirectory()){
            if (!storageDir.delete() || !storageDir.mkdir()){
                Util.onError(context, "storageDir == null", null);
                return null;
            }
        }

        if (!checkFreeSpace()){
            Util.onError(context, "storageDir == null", null);
            return null;
        }

        File file = new File(storageDir, content.contentName);//contentNameは拡張子を含んでいる
        try {
            file.createNewFile();
        } catch (IOException e){
            logStackTrace(e);
            return null;
        }

        return file;
    }

    public void downloadFile(int pos, @NonNull File file,
                      OnSuccessListener<FileDownloadTask.TaskSnapshot> onSuccessListener,
                      OnPausedListener<FileDownloadTask.TaskSnapshot> onPausedListener,
                      OnProgressListener<FileDownloadTask.TaskSnapshot> onProgressListener,
                      OnFailureListener onFailureListener){

        Content content = group.contentList.get(pos);
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("shareFile/"+group.groupKey+"/"+content.contentKey);
        ref.getFile(file).addOnSuccessListener(onSuccessListener)
                .addOnPausedListener(onPausedListener)
                .addOnProgressListener(onProgressListener)
                .addOnFailureListener(onFailureListener);
    }

    private boolean checkFreeSpace(){
        long total = context.getFilesDir().getTotalSpace();
        long usable = context.getFilesDir().getUsableSpace();
        return usable*10 / total >= 1 && usable > LIMIT_SIZE;//割算で少数は切り捨てられること、正常時にtrue, 異常時にfalseを返すことに注意してください
    }
    //endregion


    //region uploadまわり
    public static void uploadFile(@NonNull String childScheme, @NonNull Uri uri, @NonNull OnFailureListener onFailureListener,
                    @NonNull OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener,
                    @NonNull OnPausedListener<UploadTask.TaskSnapshot> onPausedListener,
                    @NonNull OnProgressListener<UploadTask.TaskSnapshot> onProgressListener){
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(childScheme);//"shareFile/" + group.groupKey + "/" + contentsKey
        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener)
                .addOnPausedListener(onPausedListener)
                .addOnProgressListener(onProgressListener);
    }

    public static boolean isOverSize(Uri uri, int limitSize){
        File f = new File(uri.getPath());
        return f.length() > limitSize;
    }
    //endregion

    @Override
    public void onPaused(Object o) {

    }

    @Override
    public void onProgress(Object o) {

    }
}
