/*
 * Copyright (c) $year. Hiroyuki Tamura All rights reserved.
 */

package com.cks.hiroyuki2.worksupprotlib.MaterialCalendarView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.cks.hiroyuki2.worksupportlib.R;
import com.cks.hiroyuki2.worksupprotlib.FirebaseConnection;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.List;

import static com.cks.hiroyuki2.worksupprotlib.Util.cal2date;
import static com.cks.hiroyuki2.worksupprotlib.Util.datePattern;

/**
 * 曜日ごとに色付けしてくれるおじさん！
 */

public class MSVDecorator implements DayViewDecorator{

    private static final String TAG = "MANUAL_TAG: " + MSVDecorator.class.getSimpleName();
    private MaterialCalendarView mcv;
    private Context context;
    private boolean isCurrentMon;
    private List<String> holidayList;

    public MSVDecorator(MaterialCalendarView mcv, Context context){
        this.mcv = mcv;
        this.context = context;
        this.holidayList = FirebaseConnection.getInstance().getHolidayArr();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        isCurrentMon = day.getMonth() == mcv.getCurrentDate().getMonth();
        return checkDayOfWeek(day, Calendar.SUNDAY, isCurrentMon) || isHoliday(day.getCalendar());
    }

    @Override
    public void decorate(DayViewFacade view) {
        int colorId;
        if (isCurrentMon){
            colorId = R.color.red_anton_dark;
        } else {
            colorId = R.color.red_anton;
        }
        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorId)));
    }

    private boolean checkDayOfWeek(CalendarDay day, int dayOfWeek, boolean isCurrentMon){
        int dow = day.getCalendar().get(Calendar.DAY_OF_WEEK);
//        boolean b = isCurrentMon == (day.getMonth() == mcv.getCurrentDate().getMonth());
        return dow == dayOfWeek;
//                && b;
    }

    private boolean isHoliday(Calendar cal){
        String str = cal2date(cal, datePattern);
        return holidayList.contains(str);
    }
}
