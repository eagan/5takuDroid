package jp.eagan.gotaku.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jp.eagan.gotaku.GotakuReader;
import jp.eagan.gotaku.QuizBook;
import jp.eagan.gotaku.QuizCategory;
import jp.eagan.gotaku.R;
import jp.eagan.gotaku.action.GotakuAction;
import jp.eagan.gotaku.action.QuizAction;
import jp.eagan.gotaku.session.EndlessGameSession;
import jp.eagan.gotaku.session.GameSession;
import jp.eagan.gotaku.widget.CategoryAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private QuizBook book;
	private CategoryAdapter categoryListAdapter;
	private GameSession session;
	private GotakuAction currentAction;
	
	private static final int BOOK_SELECT_ACTIVITY = 1;
	private static final int QUIZ_ACTIVITY = 2;
	
	/**
	 * アクティビティを初期化します。
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// ジャンルリスト
		ListView categoryList = (ListView) findViewById(R.id.category_list);
		categoryListAdapter = new CategoryAdapter(
				getApplicationContext(),
				R.layout.category_list_row,
				new ArrayList<QuizCategory>());
		categoryList.setAdapter(categoryListAdapter);
		
		// 開始ボタン
		Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startClicked();
					}
				});
		
		this.book = null;
		this.session = null;
		this.currentAction = null;
		
		selectBook();
	}
	
	/**
	 * 問題ファイル選択アクティビティを起動します。
	 * 結果は onActivityResult メソッドで受け取ります。
	 */
	void selectBook() {
		Intent intent = new Intent(getApplicationContext(),
				BookSelectActivity.class);
		startActivityForResult(intent, BOOK_SELECT_ACTIVITY);
	}
	
	/**
	 * 問題ファイルを読み込みます。
	 * 内部データおよび画面を更新します。
	 * 
	 * @param infile 問題ファイル
	 */
	void readBook(File infile) {
		InputStream in;
		try {
			in = new FileInputStream(infile);
			this.book = GotakuReader.read(in);
			in.close();
			this.book.setName(infile.getName());
			
			TextView bookNameView = (TextView) findViewById(R.id.book_name);
			bookNameView.setText(this.book.getName());
			
			QuizCategory[] categories = this.book.getCategories();
			categoryListAdapter.clear();
			for (int i = 0; i < categories.length; i++) {
				categoryListAdapter.add(categories[i]);
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}
	
	/**
	 * スタートボタン押下時の処理を実装します。
	 */
	void startClicked() {
		this.session = new EndlessGameSession();	// TODO 切り替える方法
		this.session.init(this.book);
		this.currentAction = null;
		
		dispatchNextAction();
	}
	
	/**
	 * アクティビティの結果を受け取ります。
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case BOOK_SELECT_ACTIVITY:
			if (resultCode == BookSelectActivity.RESULT_SUCCESS) {
				Bundle extras = data.getExtras();
				String dirname = extras.getString(BookSelectActivity.EXTRA_OUT_DIRNAME);
				String filename = extras.getString(BookSelectActivity.EXTRA_OUT_FILENAME);
				File book = new File(dirname, filename);
				readBook(book);
			} else {
				// BookSelectActivity からキャンセルで戻ってきた
				if (book == null) {
					// 初回の読み込みをキャンセルされた場合はアプリ終了
					finish();
				}
			}
			break;
		case QUIZ_ACTIVITY:
			if (resultCode == QuizActivity.RESULT_SELECTED) {
				Bundle extras = data.getExtras();
				int answer = extras.getInt(QuizActivity.EXTRA_OUT_ANSWER);
				QuizAction quizAction = (QuizAction) currentAction;
				quizAction.setUsersChoice(answer);
				dispatchNextAction();
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	void dispatchNextAction() {
		this.currentAction = this.session.next(this.currentAction);
		
		if (this.currentAction instanceof QuizAction) {
			QuizAction quizAction = (QuizAction) this.currentAction;
			Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
			intent.putExtra(QuizActivity.EXTRA_IN_QUIZ, quizAction);
			startActivityForResult(intent, QUIZ_ACTIVITY);
		}
	}
}
