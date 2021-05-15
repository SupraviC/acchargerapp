package com.supravin.accharger.AlertDialog;

/*
 * Created by Admin on 1/30/2018.
 **/

import android.content.DialogInterface;


public interface DialogClickInterface {

    public void onClickPositiveButton(DialogInterface pDialog, int pDialogIntefier);

    public void onClickNegativeButton(DialogInterface pDialog, int pDialogIntefier);
}