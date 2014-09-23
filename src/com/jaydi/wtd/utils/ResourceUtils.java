package com.jaydi.wtd.utils;

import com.jaydi.wtd.application.WtdApplication;


public class ResourceUtils {
	public static String getString(int resId) {
		return WtdApplication.getInstance().getResources().getString(resId);
	}
}
