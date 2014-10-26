package com.haui.japanese.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public abstract class DialogNotify extends AlertDialog.Builder {

	String tile;
	String content;
	String okTitle;
	String cancelTitle;

	public abstract void onOKClick();

	public abstract void onCancelClick();

	public DialogNotify(Context arg0, String tile, String content,
			String okTitle, String cancelTitle) {
		super(arg0);
		this.content = content;
		this.tile = tile;
		this.okTitle = okTitle;
		this.cancelTitle = cancelTitle;
		setIcon(android.R.drawable.stat_sys_warning);
		setTitle(tile);
		setMessage(content);
		setCancelable(false);
		setNegativeButton(okTitle, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onOKClick();
			}
		});

		setNeutralButton(cancelTitle, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onCancelClick();
			}
		});

		show();
	}
	
	public DialogNotify(Context arg0, String tile, String content,
			String okTitle) {
		super(arg0);
		this.content = content;
		this.tile = tile;
		this.okTitle = okTitle;
		setIcon(android.R.drawable.stat_sys_warning);
		setTitle(tile);
		setMessage(content);
		setCancelable(false);
		setNegativeButton(okTitle, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onOKClick();
			}
		});

	

		show();
	}


}
