package com.pos.restokasir.tools;

import android.graphics.drawable.Drawable;

public class NavigationItem {
    private String mText;
    private Drawable mDrawable;
    public String mKet, mTotal, mJumlah, mHP, mEmail;

    public NavigationItem(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public static NavigationItem histItem(String text, Drawable drawable, String desc, String total) {
        NavigationItem x=new NavigationItem(text, drawable);
        x.mKet=desc;
        x.mTotal=total;
        return x;
    }

    public static NavigationItem discitem(String text, String jumlah) {
        NavigationItem x=new NavigationItem(text, null);
        x.mJumlah=jumlah;
        return x;
    }

    public static NavigationItem cusitem(String text, Drawable drawable, String hp, String email) {
        NavigationItem x=new NavigationItem(text, drawable);
        x.mHP=hp;
        x.mEmail=email;
        return x;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
