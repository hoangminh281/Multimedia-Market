package com.thm.hoangminh.multimediamarket.fomular;

import java.text.DecimalFormat;

public class MoneyFormular {
    public static String format(double balance) {
        DecimalFormat format = new DecimalFormat("##,###,###.##");
        return format.format(balance) + " đ";
    }
}
