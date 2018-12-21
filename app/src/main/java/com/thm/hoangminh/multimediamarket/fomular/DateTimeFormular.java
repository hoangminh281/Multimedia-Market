package com.thm.hoangminh.multimediamarket.fomular;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeFormular {
    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        return dateFormatter.format(Calendar.getInstance().getTime());
    }
}
