package com.jaydi.wtd.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.appspot.wtd_manager.wtd.model.Code;

public class CodeSelectDialog extends DialogFragment {
	private List<Code> codes;
	private CodeSelectInter inter;

	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}

	public void setInter(CodeSelectInter inter) {
		this.inter = inter;
	}

	public interface CodeSelectInter {
		public abstract void onCodeSelect(Code code);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String[] items = new String[codes.size()];
		for (int i = 0; i < items.length; i++)
			items[i] = codes.get(i).getName();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				inter.onCodeSelect(codes.get(which));
			}

		});

		return builder.create();
	}

}
