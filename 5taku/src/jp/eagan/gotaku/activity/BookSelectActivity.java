package jp.eagan.gotaku.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import jp.eagan.gotaku.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BookSelectActivity extends ListActivity {
	private ArrayAdapter<String> bookListAdapter;
	
	public static final String EXTRA_OUT_DIRNAME = "DIRNAME";
	public static final String EXTRA_OUT_FILENAME = "FILENAME";
	
	public static final int RESULT_CANCELED = 0;
	public static final int RESULT_SUCCESS = 1;
	
	private static final String DATADIR = Environment.getExternalStorageDirectory().getPath() + "/5taku";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// View 初期化
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_select);
		
		bookListAdapter = new ArrayAdapter<String>(
				getApplicationContext(),
				R.layout.book_list_row,
				new ArrayList<String>());
		setListAdapter(bookListAdapter);
		Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						okClicked();
					}
				});
		Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						cancelClicked();
					}
				});
		
		// ファイル一覧取得
		File dir = new File(DATADIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String[] filenames = dir.list();
		for (int i = 0; i < filenames.length; i++) {
			bookListAdapter.add(filenames[i]);
		}
		if (bookListAdapter.isEmpty()) {
			// 空の場合はヘルプを表示
			InputStream help_in = getResources().openRawResource(R.raw.help_no_books);
			TextView help_text = (TextView) findViewById(R.id.no_book_help);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(help_in, "UTF-8"));
				StringBuffer buf = new StringBuffer();
				String line;
				while ((line = reader.readLine()) != null)
					buf.append(line);
				Spanned span = Html.fromHtml(buf.toString());
				help_text.setText(span);
			} catch (IOException e) {
				// リソースの読み込みに失敗した場合、できることはほとんどない
				help_text.setText("もうだめぽ");
			} finally {
				try {
					help_in.close();
				} catch (IOException e) {
					// close で例外が発生しても無視
				}
			}
		}
	}
	
	void okClicked() {
		ListView list = (ListView) findViewById(android.R.id.list);
		int position = list.getCheckedItemPosition();
		if (position == ListView.INVALID_POSITION) {
			// ファイルを選択せずに OK が押された場合は何もせずに戻る
			return;
		}
		String filename = this.bookListAdapter.getItem(position);
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_OUT_DIRNAME, DATADIR);
		intent.putExtra(EXTRA_OUT_FILENAME, filename);
		Log.d("5taku", "DIRNAME = " + DATADIR + ", FILENAME = " + filename);
		
		setResult(RESULT_SUCCESS, intent);
		finish();
	}
	
	void cancelClicked() {
		setResult(RESULT_CANCELED, null);
		finish();
	}
}
