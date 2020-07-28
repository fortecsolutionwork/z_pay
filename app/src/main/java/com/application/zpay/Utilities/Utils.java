package com.application.zpay.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.application.zpay.R;
import com.google.android.material.snackbar.Snackbar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Gaganjot Singh on 02/07/2020.
 */
public class Utils {

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    public static boolean isInternetConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public  static void showWarningAlert(Context context,String title){
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.setCustomImage(R.drawable.ic_warning);
        sweetAlertDialog.show();

        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#2f8acb"));
    }

    public  static void showSuccessAlert(Context context,String title){
        SweetAlertDialog sweetAlertDialog= new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog .setContentText(title);
        sweetAlertDialog .show();

        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#064482"));

    }

    public  static void showErrorAlert(Context context,String title){
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Oops...");
        sweetAlertDialog.setContentText(title);
        sweetAlertDialog.show();

        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(Color.parseColor("#064482"));
    }


    public static Dialog showDialog(Context context, String customerName, String monthlyRecharge, String nextRechargeDate, String status, String planName, String balance) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View alertView = inflater.inflate(R.layout.dialog, null);
        alert.setView(alertView);

        TextView customerNameTv = (TextView) alertView.findViewById(R.id.customerNameTv);
        TextView monthlyRechargeTv = (TextView) alertView.findViewById(R.id.monthlyRechargeTv);
        TextView nextRechargeDateTv = (TextView) alertView.findViewById(R.id.nextRechargeDateTv);
        TextView statusTv = (TextView) alertView.findViewById(R.id.statusTv);
        TextView planNameTv = (TextView) alertView.findViewById(R.id.planNameTv);
        TextView balanceTv = (TextView) alertView.findViewById(R.id.balanceTv);

        customerNameTv.setText(customerName);
        monthlyRechargeTv.setText(monthlyRecharge);
        nextRechargeDateTv.setText(nextRechargeDate);
        statusTv.setText(status);
        planNameTv.setText(planName);
        balanceTv.setText(balance);


        final AlertDialog alertDialog = alert.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*Button dialogButton = (Button) alertView.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });*/

        return alertDialog;



        /*final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//this line MUST BE BEFORE setContentView
        alertDialog.setContentView(R.layout.dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LinearLayout iconBackground = (LinearLayout) alertDialog.findViewById(R.id.iconbackground);
        ImageView icon = (ImageView) alertDialog.findViewById(R.id.icon);

        if (responseType.equals("200") || responseType.equals("success")) {
            iconBackground.setBackgroundColor(context.getResources().getColor(R.color.loginClr));
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.dialogcheck));
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);

        Button dialogButton = (Button) alertDialog.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        return alertDialog;*/
    }

    public static Dialog showGasDialog(Context context, String billAmount, String customerName, String dueAmt, String duedate) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View alertView = inflater.inflate(R.layout.dialog_gas, null);
        alert.setView(alertView);

        TextView customerNameTv = (TextView) alertView.findViewById(R.id.customerNameTv);
        TextView billAmountTv = (TextView) alertView.findViewById(R.id.billAmountTv);
        TextView dueDateTv = (TextView) alertView.findViewById(R.id.dueDateTv);
        TextView dueAmountTv = (TextView) alertView.findViewById(R.id.dueAmountTv);


        customerNameTv.setText(customerName);
        billAmountTv.setText(billAmount);
        dueDateTv.setText(dueAmt);
        dueAmountTv.setText(duedate);



        final AlertDialog alertDialog = alert.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*Button dialogButton = (Button) alertView.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });*/

        return alertDialog;



        /*final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//this line MUST BE BEFORE setContentView
        alertDialog.setContentView(R.layout.dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LinearLayout iconBackground = (LinearLayout) alertDialog.findViewById(R.id.iconbackground);
        ImageView icon = (ImageView) alertDialog.findViewById(R.id.icon);

        if (responseType.equals("200") || responseType.equals("success")) {
            iconBackground.setBackgroundColor(context.getResources().getColor(R.color.loginClr));
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.dialogcheck));
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);

        Button dialogButton = (Button) alertDialog.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        return alertDialog;*/
    }


    public static Dialog showPostpaidFetchBill(Context context, String billAmount, String netAmount, String billDate, String duedate) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View alertView = inflater.inflate(R.layout.dialog_postpaidbill, null);
        alert.setView(alertView);

        TextView customerNameTv = (TextView) alertView.findViewById(R.id.customerNameTv);
        TextView billAmountTv = (TextView) alertView.findViewById(R.id.billAmountTv);
        TextView dueDateTv = (TextView) alertView.findViewById(R.id.dueDateTv);
        TextView dueAmountTv = (TextView) alertView.findViewById(R.id.dueAmountTv);


        customerNameTv.setText(billAmount);
        billAmountTv.setText(netAmount);
        dueDateTv.setText(billDate);
        dueAmountTv.setText(duedate);



        final AlertDialog alertDialog = alert.show();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        //alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /*Button dialogButton = (Button) alertView.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });*/

        return alertDialog;



        /*final Dialog alertDialog = new Dialog(context);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//this line MUST BE BEFORE setContentView
        alertDialog.setContentView(R.layout.dialog);
        alertDialog.setCanceledOnTouchOutside(false);
        LinearLayout iconBackground = (LinearLayout) alertDialog.findViewById(R.id.iconbackground);
        ImageView icon = (ImageView) alertDialog.findViewById(R.id.icon);

        if (responseType.equals("200") || responseType.equals("success")) {
            iconBackground.setBackgroundColor(context.getResources().getColor(R.color.loginClr));
            icon.setImageDrawable(context.getResources().getDrawable(R.drawable.dialogcheck));
        }
        TextView text = (TextView) alertDialog.findViewById(R.id.text_dialog);
        text.setText(message);

        Button dialogButton = (Button) alertDialog.findViewById(R.id.btn_dialogOK);
        dialogButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        return alertDialog;*/
    }






    public static void showSnackBar(Context context,View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }




}
