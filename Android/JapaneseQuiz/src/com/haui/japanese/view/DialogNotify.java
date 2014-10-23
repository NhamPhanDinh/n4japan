package com.haui.japanese.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class DialogNotify extends AlertDialog.Builder {

	String content;

	public abstract void onOKClick();

	public abstract void onCancelClick();

	public DialogNotify(Context arg0, String content) {
		super(arg0);
		this.content = content;
		setMessage(content);
		setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onOKClick();
			}
		});

		setNeutralButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onCancelClick();
			}
		});

		show();
	}

}
