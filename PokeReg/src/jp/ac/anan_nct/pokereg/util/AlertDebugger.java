package jp.ac.anan_nct.pokereg.util;
import android.app.AlertDialog;
import android.content.Context;

public class AlertDebugger {
	public static void Out(String message, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Alert Debug");
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
	}
}
