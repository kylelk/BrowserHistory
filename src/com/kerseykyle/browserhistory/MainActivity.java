package com.kerseykyle.browserhistory;

import android.annotation.TargetApi;
import android.app.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.*;
import android.os.*;
import android.provider.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import org.json.*;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getBrowserHist();
	}

	public void getBrowserHist() {
		try {
			JSONObject parent = new JSONObject();

			String[] proj = new String[] { Browser.BookmarkColumns.TITLE,
					Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE,
					Browser.BookmarkColumns.VISITS };
			String sel = Browser.BookmarkColumns.BOOKMARK + " = 0";
			Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI,
					proj, sel, null, null);
			mCur.moveToFirst();
			if (mCur.moveToFirst() && mCur.getCount() > 0) {
				boolean cont = true;
				while (mCur.isAfterLast() == false && cont) {
					JSONObject jsonObject = new JSONObject();
					String title = mCur.getString(mCur
							.getColumnIndex(Browser.BookmarkColumns.TITLE));
					String url = mCur.getString(mCur
							.getColumnIndex(Browser.BookmarkColumns.URL));
					String date = mCur.getString(mCur
							.getColumnIndex(Browser.BookmarkColumns.DATE));
					String visits = mCur.getString(mCur
							.getColumnIndex(Browser.BookmarkColumns.VISITS));
					jsonObject.put("TITLE", title);
					jsonObject.put("URL", url);
					jsonObject.put("DATE", date);
					jsonObject.put("VISITS", visits);

					parent.put(title, jsonObject);
					mCur.moveToNext();
				}
				EditText output = (EditText) findViewById(R.id.outputArea);
				output.setText(parent.toString(4));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Button copy = (Button) findViewById(R.id.copy);
		copy.setOnClickListener(new OnClickListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onClick(View v) {
				EditText output = (EditText) findViewById(R.id.outputArea);
				String data = output.getText().toString();
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("label", data);
				clipboard.setPrimaryClip(clip);
			}
		});

		Button share = (Button) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText output = (EditText) findViewById(R.id.outputArea);
				String data = output.getText().toString();
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, data);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
	}
}