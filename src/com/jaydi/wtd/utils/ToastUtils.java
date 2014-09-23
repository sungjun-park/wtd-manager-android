package com.jaydi.wtd.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.jaydi.wtd.R;
import com.jaydi.wtd.application.WtdApplication;

public class ToastUtils {
	public static void show(int resId) {
		show(ResourceUtils.getString(resId));
	}

	public static void show(String msg) {
		final Context context = WtdApplication.getInstance().getApplicationContext();
		final Toast toast = new Toast(context);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView msgView = (TextView) inflater.inflate(R.layout.toast_simple_layout, null, false);
		msgView.setText(msg);

		toast.setView(msgView);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
