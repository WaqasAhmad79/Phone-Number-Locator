package com.example.phonenumberlocator.admob_ads;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.phonenumberlocator.R;


public class ResumeLoadingDialog extends Dialog {

    public ResumeLoadingDialog(Context context) {
        super(context, R.style.AppTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resume_loading);
        Log.d("Dialog", "onCreate: this");
    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
