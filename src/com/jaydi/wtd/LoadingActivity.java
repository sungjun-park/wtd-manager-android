package com.jaydi.wtd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.appspot.wtd_manager.wtd.model.Code;
import com.appspot.wtd_manager.wtd.model.CodeCol;
import com.jaydi.wtd.connection.ResponseHandler;
import com.jaydi.wtd.connection.network.NetworkInter;
import com.jaydi.wtd.constants.Codes;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		initCodes();
	}

	private void initCodes() {
		NetworkInter.getCodes(new ResponseHandler<CodeCol>() {

			@Override
			protected void onResponse(CodeCol res) {
				if (res != null && res.getCodes() != null) {
					saveCodes(res);
					goToMain();
				}
			}

		});
	}

	private void saveCodes(CodeCol codeCol) {
		List<Code> codes = codeCol.getCodes();
		List<Code> locCodes = new ArrayList<Code>();
		List<Code> catACodes = new ArrayList<Code>();
		List<Code> catBCodes = new ArrayList<Code>();

		for (Code code : codes)
			if (0 <= code.getCode() && code.getCode() < 10)
				catACodes.add(code);
			else if (10 <= code.getCode() && code.getCode() < 100)
				catBCodes.add(code);
			else if (1000 <= code.getCode() && code.getCode() < 10000)
				locCodes.add(code);

		Codes.setLocCodes(locCodes);
		Codes.setCatACodes(catACodes);
		Codes.setCatBCodes(catBCodes);
	}

	private void goToMain() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

}
