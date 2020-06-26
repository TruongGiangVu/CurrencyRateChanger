package com.giangvu.currencyratechanger.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDialogLoading(String loadingmessage, boolean cancelable) {
        dialog = ProgressDialog.show(this, "",
                loadingmessage, true);
        dialog.setCancelable(cancelable);
    }
    public void timerDelayRemoveDialog(long time){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, time);
    }
    public void cancleDialogLoading() {
        if (dialog.isShowing())
            dialog.cancel();
    }
}
