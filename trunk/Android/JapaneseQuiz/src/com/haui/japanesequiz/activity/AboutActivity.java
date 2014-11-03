package com.haui.japanesequiz.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AboutActivity extends Activity {
	TextView tvInfor;
	ListView listViewAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("About");
		setContentView(R.layout.activity_about);
		tvInfor = (TextView) findViewById(R.id.tvInfor);
		listViewAbout = (ListView) findViewById(R.id.listViewAbout);

		PackageInfo pInfo;
		String version = "1.0";
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvInfor.setText(layString(R.string.infoContent) + " " + version);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.item_text, R.id.txtAbout,
				new String[] { "Rate App", "Share Over Facebook", "Feedback" });
		listViewAbout.setAdapter(adapter);
		listViewAbout.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} catch (android.content.ActivityNotFoundException anfe) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
					}
					break;
				case 1:
					
					
					
					final String appPackageName1 = getPackageName(); // getPackageName() from Context or Activity object
					
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
	                sharingIntent.setType("text/plain");
	                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
	                sharingIntent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + appPackageName1);
	                PackageManager packManager = getPackageManager();
	                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(sharingIntent,  PackageManager.MATCH_DEFAULT_ONLY);

	                boolean resolved = false;
	                for(ResolveInfo resolveInfo: resolvedInfoList){
	                    if(resolveInfo.activityInfo.packageName.startsWith("com.facebook.katana")){
	                        sharingIntent.setClassName(
	                            resolveInfo.activityInfo.packageName, 
	                            resolveInfo.activityInfo.name );
	                        resolved = true;
	                        break;
	                    }
	                }
	                if(resolved){
	                    startActivity(sharingIntent);
	                }else{

	                     Builder alert  = new AlertDialog.Builder(AboutActivity.this);
	                        alert.setTitle("Warning");
	                        alert.setMessage("Facebook App not found");
	                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,int which) 
	                            {
	                                dialog.dismiss();

	                            }
	                        });
	                        alert.show();
	                } 
					break;
				case 2:
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
							.fromParts("mailto",
									getResources().getString(R.string.email),
									null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
					startActivity(Intent.createChooser(emailIntent,
							"Send email..."));
					break;
				}

			}
		});

	}

	String layString(int id) {
		return getResources().getString(id);
	}

}
