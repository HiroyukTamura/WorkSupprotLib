/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib.Entity;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static com.cks.hiroyuki2.worksupprotlib.Util.getCalFromTimeEvent;

/**
 * {@link TimeEvent}の兄弟分
 */

public class TimeEventRange implements Serializable{

    private TimeEvent start;
    private TimeEvent end;
    private int colorNum = 0;

    public TimeEventRange(@NonNull TimeEvent start, @NonNull TimeEvent end){
        this.start = start;
        this.end = end;
//        sort();
    }

    public TimeEvent getStart() {
        return start;
    }

    public void setStart(TimeEvent start) {
        this.start = start;
//        sort();
    }

    public TimeEvent getEnd() {
        return end;
    }

    public void setEnd(TimeEvent end) {
        this.end = end;
//        sort();
    }

    public int getColorNum() {
        return colorNum;
    }

    public void setColorNum(int colorNum) {
        this.colorNum = colorNum;
    }

    public TimeEvent getTimeEve(int position){
        switch (position){
            case 0:
                return start;
            case 1:
                return end;
            default:
                throw new IllegalArgumentException("ばーか！");
        }
    }

    public void setTimeEvent(@NonNull TimeEvent timeEvent, @IntRange(from = 0, to = 1) int pos){
        switch (pos){
            case 0:
                start = timeEvent;
                break;
            case 1:
                end = timeEvent;
                break;
        }

//        sort();
    }

//    public void sort(){
//        if (start.getOffset() > end.getOffset() ||
//                getCalFromTimeEvent(start).compareTo(getCalFromTimeEvent(end)) > 0){
//            convert();
//        }
//    }
//
//    private void convert(){
//        TimeEvent t = end;
//        end = start;
//        start = t;
//    }
}
