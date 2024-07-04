package com.example.e_commerce.Common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseHelper {

    public static String dateTimeToString(Date date){
        String dateFormatPattern = "dd-MM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
        String formattedDateStr = dateFormat.format(date);
        return formattedDateStr;
    }

    public static String intToString(int amount){
        DecimalFormat df = new DecimalFormat("#,###");

        // Định dạng số kiểu int thành chuỗi
        String formattedAmount = df.format(amount) + " VND";

        return formattedAmount;
    }
}
