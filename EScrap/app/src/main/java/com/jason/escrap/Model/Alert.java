package com.jason.escrap.Model;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Alert {
    Context context;

    public Alert(Context context) {
        this.context = context;
    }

    public void error_alert(String Title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(Title)
                .setContentText(message).show();

    }

    public void success_alert(String Title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(Title)
                .setContentText(message).show();

    }



}
