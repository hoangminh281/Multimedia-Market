package com.thm.hoangminh.multimediamarket.fomular;

public class NumberFormular {
    private int number;
    private String[] unitArr;

    public NumberFormular(int number, String[] unitArr) {
        this.number = number;
        this.unitArr = unitArr;
    }

    public int getNumber() {
        int tmp = number;
        if (number > 999) {
            do {
                tmp /= 1000;
            }
            while (tmp > 999);
        }
        return tmp;
    }

    public String getUnit() {
        String st = number + "";
        int i = (st.length() - 1) / 3;
        return unitArr[i];
    }
}
