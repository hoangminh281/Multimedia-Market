package com.thm.hoangminh.multimediamarket.model;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

public class File {
    private String name;
    private Size size; //default byte
    private Uri uri;

    public enum Units {B, KB, MB, GB, TB}

    public File(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                this.uri = uri;
                name = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                if (!cursor.isNull(sizeIndex)) {
                    size = new Size();
                    size.value = Integer.valueOf(cursor.getString(sizeIndex));
                    size.unit = Units.B;
                }
            }
        } finally {
            cursor.close();
        }
    }

    public File(String name, Size size, Uri uri) {
        this.name = name;
        this.size = size;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Size getAbsoluteSize() {
        int count = 0;
        Size s = new Size(this.size);
        for (; s.value > 1024; ++count) {
            s.value /= 1024;
        }

        double d = s.value;
        d *= 10d;
        d = Math.ceil(d);
        d /= 10d;

        s.value = d;
        s.unit = File.Units.values()[count];
        return s;
    }

    public class Size {
        private double value;
        private Units unit;

        public Size(Size size) {
            this.value = size.value;
            this.unit = size.unit;
        }

        public Size() {

        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public Units getUnit() {
            return unit;
        }

        public void setUnit(Units unit) {
            this.unit = unit;
        }
    }
}
