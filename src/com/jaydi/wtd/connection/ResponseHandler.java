package com.jaydi.wtd.connection;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;

public abstract class ResponseHandler<T> extends Handler {
	private DialogFragment dialog;

	public ResponseHandler() {
		super();
	}

	public ResponseHandler(DialogFragment dialog) {
		super();
		this.dialog = dialog;
	}

	public ResponseHandler(Looper looper) {
		super(looper);
	}

	public ResponseHandler(Looper looper, DialogFragment dialog) {
		super(looper);
		this.dialog = dialog;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message m) {
		if (dialog != null)
			dialog.dismiss();
		onResponse((T) m.obj);
	}

	protected abstract void onResponse(T res);
}
