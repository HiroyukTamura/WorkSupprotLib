/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cks.hiroyuki2.worksupprotlib.Entity.RecordData;
import com.cks.hiroyuki2.worksupprotlib.Entity.TimeEvent;
import com.cks.hiroyuki2.worksupprotlib.Entity.TimeEventDataSet;
import com.cks.hiroyuki2.worksupprotlib.Entity.TimeEventRange;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.cks.hiroyuki2.worksupprotlib.Util.TEMPLATE_SERIALIZE;
import static com.cks.hiroyuki2.worksupprotlib.Util.TEMPLATE_SERIALIZE_DEFAULT;
import static com.cks.hiroyuki2.worksupprotlib.Util.delimiter;
import static com.cks.hiroyuki2.worksupprotlib.Util.logStackTrace;

/**
 * テンプレ編集を担うおじさん！信頼が厚い！
 */
public class TemplateEditor {

    private static final String TAG = "TemplateEditor";

    @Nullable
    public static List<RecordData> deSerialize(Context context){
        return innerDeSerialize(context, TEMPLATE_SERIALIZE);
    }

    @Nullable
    public static List<RecordData> deSerializeDefault(Context context){
        return innerDeSerialize(context, TEMPLATE_SERIALIZE_DEFAULT);
    }

    @Nullable
    private static List<RecordData> innerDeSerialize(Context context, String fileName){
        List<RecordData> data = null;
        try {
            FileInputStream fis = context.openFileInput(TEMPLATE_SERIALIZE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (List<RecordData>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            logStackTrace(e);
        } catch (ClassNotFoundException e) {
            logStackTrace(e);
        }
        return data;
    }

    public static boolean writeTemplate(int dataNum, @NonNull RecordData data, @NonNull Context context){
        List<RecordData> list = deSerialize(context);
        if (list == null) return false;
        list.set(dataNum, data);
        return applyTemplate(list, context);
    }

    static boolean addRecordData(Context context, RecordData data){
        List<RecordData> list = deSerialize(context);
        if (list == null) return false;
        list.add(data);
        return applyTemplate(list, context);
    }

    public static boolean addRecordData(Context context, RecordData data, int index){
        List<RecordData> list = deSerialize(context);
        if (list == null) return false;
        list.add(index, data);
        return applyTemplate(list, context);
    }

    public static boolean applyTemplate(List<RecordData> list, Context context){
        return innerApplyTemplate(list, context, TEMPLATE_SERIALIZE);
    }

    public static boolean applyTemplateAsDefault(List<RecordData> list, Context context){
        return innerApplyTemplate(list, context, TEMPLATE_SERIALIZE_DEFAULT);
    }

    private static boolean innerApplyTemplate(List<RecordData> list, Context context, String fileName){
        try {
            ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            out.writeObject(list);
            out.close();
            return true;
        } catch (IOException e){
            logStackTrace(e);
            return false;
        }
    }

    public static boolean initDefaultTemplate(Context context) {
        Log.d(TAG, "initFile: fire");
        List<RecordData> list = new ArrayList<>(5);
        for (int i = 0; i < 6; i++) {
            RecordData data = new RecordData();
            data.data = new HashMap<>();
            switch (i) {
                case 0:
                    data.dataType = 0;//ヘッダータグ
                    data.data.put("0", "勤務日" + delimiter + "2");
                    break;
                case 1:
                    data.dataType = 1;//タイムライン
                    TimeEvent wakeUp = new TimeEvent("起床", 0, 7, 30, 0);
                    TimeEvent sleep = new TimeEvent("就寝", 0, 22, 0, -1);
                    TimeEventRange range = new TimeEventRange(sleep, wakeUp);
                    List<TimeEventRange> rangeList = new LinkedList<>();
                    rangeList.add(range);
                    TimeEvent departure = new TimeEvent("出勤", 2, 8, 0, 0);
                    TimeEvent homeBack = new TimeEvent("帰宅", 2, 14, 40, 0);
                    List<TimeEvent> eventList = new LinkedList<>();
                    eventList.add(departure);
                    eventList.add(homeBack);
                    TimeEventDataSet dataSet = new TimeEventDataSet(eventList, rangeList);
                    data.data.put("0", new Gson().toJson(dataSet));
//                    data.data.put("7:00", "起床" + FirebaseConnection.delimiter + "1");
//                    data.data.put("8:50", "出勤");
//                    data.data.put("14:00", "退勤");
//                    data.data.put("22:00", "就寝" + FirebaseConnection.delimiter + "1");
                    break;
                case 2:
                    data.dataType = 2;//タグプール
                    data.dataName = "症状・セルフケア";
                    data.data.put("0", "震え" + delimiter + "0" + delimiter + "true");
                    data.data.put("1", "気分の低下" + delimiter + "1" + delimiter + "true");
                    data.data.put("2", "神経質" + delimiter + "1" + delimiter + "false");
                    data.data.put("3", "休憩をもらう"+delimiter+"2"+delimiter+"true");
                    break;
                case 3:
                    data.dataType = 2;
                    data.dataName = "仕事内容";
                    data.data.put("0", "資料作成" + delimiter + "1" + delimiter + "true");
                    data.data.put("1", "バックヤード" + delimiter + "1" + delimiter + "true");
                    data.data.put("2", "早出" + delimiter + "2" + delimiter + "false");
                    data.data.put("3", "残業" + delimiter + "2" + delimiter + "true");
                    data.data.put("4", "上司面談" + delimiter + "3" + delimiter + "true");
                    break;
                case 4:
                    data.dataType = 4;
                    data.dataName = "備考";
                    data.data.put("comment", null);
                    break;
                case 5:
                    data.dataType = 3;
                    data.dataName = "生活";
                    data.data.put("0", "0" + delimiter + "朝薬" + delimiter + "false");
                    data.data.put("1", "0" + delimiter + "夕薬" + delimiter + "true");
                    data.data.put("2", "0" + delimiter + "頓服" + delimiter + "true");
                    data.data.put("3", "0" + delimiter + "定時出勤" + delimiter + "true" );
                    data.data.put("4", "1" + delimiter +  "気分" + delimiter + "3" + delimiter + "5");
                    break;
            }
            list.add(data);
        }

        return applyTemplate(list, context) && applyTemplateAsDefault(list, context);
    }
}
