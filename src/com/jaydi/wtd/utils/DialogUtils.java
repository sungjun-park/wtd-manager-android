package com.jaydi.wtd.utils;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.appspot.wtd_manager.wtd.model.Code;
import com.jaydi.wtd.fragments.CodeSelectDialog;
import com.jaydi.wtd.fragments.WaitingDialog;

public class DialogUtils {
	public static void networkAlert(final Context context, final boolean finishable) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Network Error");
		builder.setPositiveButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (finishable)
					((Activity) context).finish();
			}

		});

		builder.show();
	}

	public static DialogFragment getWaitingDialog(FragmentManager fm) {
		WaitingDialog dialog = new WaitingDialog();
		dialog.show(fm, "Waiting");
		return dialog;
	}

	public static void showCodeSelectDialog(FragmentManager fm, List<Code> codes, CodeSelectDialog.CodeSelectInter inter) {
		CodeSelectDialog dialog = new CodeSelectDialog();
		dialog.setCodes(codes);
		dialog.setInter(inter);
		dialog.show(fm, "CodeSelect");
	}

}
