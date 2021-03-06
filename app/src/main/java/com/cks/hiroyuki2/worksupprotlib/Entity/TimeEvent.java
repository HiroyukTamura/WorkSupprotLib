/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib.Entity;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

import static com.cks.hiroyuki2.worksupprotlib.Util.DATE_PATTERN_COLON_HM;
import static com.cks.hiroyuki2.worksupprotlib.Util.cal2date;

/**
 * {@link RecordData#dataType} == 1の場合にこのクラスが使われます。
 */

public class TimeEvent implements Serializable{

    private Calendar cal;//日付部分は使用しない。時間としてのみ使い、日付はoffsetで管理する。
    private String name;
    private int colorNum;
    private int offset;

    public TimeEvent(@NonNull String name, int colorNum, @IntRange(from = 0, to = 24) int hour, @IntRange(from = 0, to = 60) int min, int offset){
        this.name = name;
        this.colorNum = colorNum;
        this.offset = offset;
        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
    }

    public TimeEvent(@NonNull String name, int colorNum, @NonNull Calendar cal, int offset){
        this.name = name;
        this.cal = cal;
        this.colorNum = colorNum;
        this.offset = offset;
    }

    public int getHour() {
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMin() {
        return cal.get(Calendar.MINUTE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHour(int hour) {
        cal.set(Calendar.HOUR_OF_DAY, hour);
    }

    public void setMin(int min) {
        cal.set(Calendar.MINUTE, min);
    }

    public Calendar getCal(){
        return cal;
    }

    public String getTimeStr(){
        return cal2date(cal, DATE_PATTERN_COLON_HM);
    }

    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public float getHourFloat(){
        return getHour() + (float) ((float) getMin())/60f;
    }
}
