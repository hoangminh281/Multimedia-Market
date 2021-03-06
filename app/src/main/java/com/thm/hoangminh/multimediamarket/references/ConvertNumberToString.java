package com.thm.hoangminh.multimediamarket.references;

public class ConvertNumberToString {
    private int downloaded;
    private String[] unitArr;

    public ConvertNumberToString(int downloaded, String[] unitArr) {
        this.downloaded = downloaded;
        this.unitArr = unitArr;
    }

    public int getNumber() {
        int tmp = downloaded;
        if (downloaded > 999) {
            do {
                tmp /= 1000;
            }
            while (tmp > 999);
        }
        return tmp;
    }

    public String getUnit() {
        String st = downloaded + "";
        int i = (st.length() - 1) / 3;
        return unitArr[i];
    }
}
