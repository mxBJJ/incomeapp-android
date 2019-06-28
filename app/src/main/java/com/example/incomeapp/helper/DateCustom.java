package com.example.incomeapp.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String currentDate(){

        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(date);


        return dataString;
    }


    public static String formatDate(String data){

       String newDate[] =  data.split("/");
       String month = newDate[1];
       String year = newDate[2];

       return month + year;

    }
}

