package com.example.wireframe.view;

import java.util.Calendar;

import android.util.Log;

public class SpecialCalendar {
    /**
     * ����������
     * @author willm zhang
     *
     */

    private int daysOfMonth = 0;      //ĳ�µ�����
    private int dayOfWeek = 0;        //����ĳһ�������ڼ�

    
    
    
    // �ж��Ƿ�Ϊ����
    public boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    //�õ�ĳ���ж�������
    public int getDaysOfMonth(boolean isLeapyear, int month) {
        switch (month) {
        case 1:
        case 3:
        case 5:
        case 7:
        case 8:
        case 10:
        case 12:
            daysOfMonth = 31;
            break;
        case 4:
        case 6:
        case 9:
        case 11:
            daysOfMonth = 30;
            break;
        case 2:
            if (isLeapyear) {
                daysOfMonth = 29;
            } else {
                daysOfMonth = 28;
            }

        }
        return daysOfMonth;
    }
    
    //ָ��ĳ���е�ĳ�µĵ�һ�������ڼ�
    public int getWeekdayOfMonth(int year, int month){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
        return dayOfWeek;
    }
    
    
}