package com.example.phonenumberlocator.ui.pnlDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.phonenumberlocator.R;


public class PNLResumeLoadingDialog extends Dialog {

    public PNLResumeLoadingDialog(Context context) {
        super(context, R.style.AppTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resume_loading);
    }
}
