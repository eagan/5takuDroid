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
	 * �A�N�e�B�r�e�B�����������܂��B
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// �W���������X�g
		ListView categoryList = (ListView) findViewById(R.id.category_list);
		categoryListAdapter = new CategoryAdapter(
				getApplicationContext(),
				R.layout.category_list_row,
				new ArrayList<QuizCategory>());
		categoryList.setAdapter(categoryListAdapter);
		
		// �J�n�{�^��
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
	 * ���t�@�C���I���A�N�e�B�r�e�B���N�����܂��B
	 * ���ʂ� onActivityResult ���\�b�h�Ŏ󂯎��܂��B
	 */
	void selectBook() {
		Intent intent = new Intent(getApplicationContext(),
				BookSelectActivity.class);
		startActivityForResult(intent, BOOK_SELECT_ACTIVITY);
	}
	
	/**
	 * ���t�@�C����ǂݍ��݂܂��B
	 * �����f�[�^����щ�ʂ��X�V���܂��B
	 * 
	 * @param infile ���t�@�C��
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
	 * �X�^�[�g�{�^���������̏������������܂��B
	 */
	void startClicked() {
		this.session = new EndlessGameSession();	// TODO �؂�ւ�����@
		this.session.init(this.book);
		this.currentAction = null;
		
		dispatchNextAction();
	}
	
	/**
	 * �A�N�e�B�r�e�B�̌��ʂ��󂯎��܂��B
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
				// BookSelectActivity ����L�����Z���Ŗ߂��Ă���
				if (book == null) {
					// ����̓ǂݍ��݂��L�����Z�����ꂽ�ꍇ�̓A�v���I��
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
