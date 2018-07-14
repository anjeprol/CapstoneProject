package com.bharadwaj.android.capstoneproject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bharadwaj.android.capstoneproject.constants.Constants;
import timber.log.Timber;

import static com.bharadwaj.android.capstoneproject.constants.Constants.androidns;

public class SeekBarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar mSeekBar;
    private TextView mValueText;
    private Context mContext;

    private String mDialogMessage, mSuffix;
    private int mDefault, mMax, mValue = 0;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context,attrs);
        Timber.d("Entering SeekBarPreference");

        mContext = context;

        int mDialogMessageId = attrs.getAttributeResourceValue(androidns, Constants.DIALOG_MESSAGE, 0);
        if(mDialogMessageId == 0)
            mDialogMessage = attrs.getAttributeValue(androidns, Constants.DIALOG_MESSAGE);
        else
            mDialogMessage = mContext.getString(mDialogMessageId);

        int mSuffixId = attrs.getAttributeResourceValue(androidns, Constants.TEXT, 0);
        if(mSuffixId == 0)
            mSuffix = attrs.getAttributeValue(androidns, Constants.TEXT);
        else
            mSuffix = mContext.getString(mSuffixId);

        mDefault = attrs.getAttributeIntValue(androidns, Constants.DEFAULT_VALUE, 1);
        mMax = attrs.getAttributeIntValue(androidns, Constants.MAX, 100);
        Timber.d("Leaving SeekBarPreference");
    }


    //DialogPreference methods
    @Override
    protected View onCreateDialogView() {
        Timber.d("Entering onCreateDialogView");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6,6,6,6);

        TextView mSplashText = new TextView(mContext);
        mSplashText.setPadding(30, 10, 30, 10);
        if (mDialogMessage != null)
            mSplashText.setText(mDialogMessage);
        layout.addView(mSplashText);

        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(mContext);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, params);

        if (shouldPersist())
            mValue = getPersistedInt(mDefault);

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);

        Timber.d("Leaving onCreateDialogView");
        return layout;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        Timber.d("Entering onBindDialogView");
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
        Timber.d("Leaving onBindDialogView");
    }


    //Preference method
    @Override
    protected void onSetInitialValue(boolean restore, Object defaultValue)
    {
        super.onSetInitialValue(restore, defaultValue);
        Timber.d("Entering onSetInitialValue");
        if (restore)
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        else
            mValue = (Integer)defaultValue;
        Timber.d("Leaving onSetInitialValue");
    }



    // OnSeekBarChangeListener methods :
    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch)
    {
        Timber.d("Entering onProgressChanged");
        String t = String.valueOf(value);
        mValueText.setText(mSuffix == null ? t : t.concat(" " + mSuffix));
        Timber.d("Leaving onProgressChanged");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {}
    @Override
    public void onStopTrackingTouch(SeekBar seek) {}

    @Override
    public void showDialog(Bundle state) {
        Timber.d("Entering showDialog");
        super.showDialog(state);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(this);
        Timber.d("Leaving showDialog");
    }

    @Override
    public void onClick(View v) {
        Timber.d("Entering onClick");

        if (shouldPersist()) {

            mValue = mSeekBar.getProgress();
            persistInt(mSeekBar.getProgress());
        }
        getDialog().dismiss();
        Timber.d("Leaving showDialog");
    }
}

