package com.jaydi.wtd.constants;

import java.util.List;

import com.appspot.wtd_manager.wtd.model.Code;
import com.jaydi.wtd.R;
import com.jaydi.wtd.utils.ResourceUtils;

public class Codes {
	private static List<Code> locCodes;
	private static List<Code> catACodes;
	private static List<Code> catBCodes;

	public static List<Code> getLocCodes() {
		return locCodes;
	}

	public static void setLocCodes(List<Code> locCodes) {
		Codes.locCodes = locCodes;
	}

	public static List<Code> getCatACodes() {
		return catACodes;
	}

	public static void setCatACodes(List<Code> catACodes) {
		Codes.catACodes = catACodes;
	}

	public static List<Code> getCatBCodes() {
		return catBCodes;
	}

	public static void setCatBCodes(List<Code> catBCodes) {
		Codes.catBCodes = catBCodes;
	}

	public static String getLocCodeString(int locCode) {
		for (Code code : locCodes)
			if (code.getCode().equals(Long.valueOf(locCode)))
				return code.getName();
		return ResourceUtils.getString(R.string.none);
	}

	public static String getCatACodeString(int catACode) {
		for (Code code : catACodes)
			if (code.getCode().equals(Long.valueOf(catACode)))
				return code.getName();
		return ResourceUtils.getString(R.string.none);
	}

	public static String getCatBCodeString(int catBCode) {
		for (Code code : catBCodes)
			if (code.getCode().equals(Long.valueOf(catBCode)))
				return code.getName();
		return ResourceUtils.getString(R.string.none);
	}

}
