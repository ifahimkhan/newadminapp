package com.fahim.newapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fahim.newapp.R;


public class QustomDialogBuilder extends AlertDialog.Builder {

    /**
     * The custom_body layout
     */
    private View mDialogView;

    /**
     * optional dialog title layout
     */
    private TextView mTitle;
    /**
     * optional alert dialog image
     */
    private ImageView mIcon;
    /**
     * optional message displayed below title if title exists
     */
    private TextView mMessage;
    /**
     * The colored holo divider. You can set its color with the setDividerColor method
     */
    private View mDivider;

    private LinearLayout contentLayout;

    public QustomDialogBuilder(Context context) {
        super(context);

        mDialogView = View.inflate(context, R.layout.qustom_dialog_layout, null);
        setView(mDialogView);

        mTitle = mDialogView.findViewById(R.id.alertTitle);
        mMessage = mDialogView.findViewById(R.id.message);
        mIcon = mDialogView.findViewById(R.id.icon);
        mDivider = mDialogView.findViewById(R.id.titleDivider);
        contentLayout = mDialogView.findViewById(R.id.contentPanel);
    }

    /**
     * Use this method to color the divider between the title and content.
     * Will not display if no title is set.
     *
     * @param colorString for passing "#ffffff"
     */
    public QustomDialogBuilder setDividerColor(int colorString) {
        mDivider.setBackgroundColor(colorString);
        return this;
    }

    @Override
    public QustomDialogBuilder setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    public QustomDialogBuilder setTitleColor(int colorString) {
        mTitle.setTextColor(colorString);
        return this;
    }

    @Override
    public QustomDialogBuilder setMessage(int textResId) {
        mMessage.setText(textResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setMessage(CharSequence text) {
        mMessage.setText(text);
        return this;
    }

    @Override
    public QustomDialogBuilder setIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    @Override
    public QustomDialogBuilder setIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }


    /**
     * This allows you to specify a custom layout for the area below the title divider bar
     * in the dialog. As an example you can look at example_ip_address_layout.xml and how
     * I added it in TestDialogActivity.java
     *
     * @param resId   of the layout you would like to add
     * @param context
     */
    public QustomDialogBuilder setCustomView(View customVIew, Context context) {
        /*	View customView = View.inflate(context, resId, null);*/
        ((FrameLayout) mDialogView.findViewById(R.id.customPanel)).addView(customVIew);
        return this;
    }

    @Override
    public AlertDialog show() {
        if (mTitle.getText().equals(""))
            mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
        return super.show();
    }

    public void hideContentPanel(View alertLayout, Context context) {
        contentLayout.setVisibility(View.GONE);
    }
//
//    public void dismissDialog(Context context, View alertLayout) {
//
//    }
}
