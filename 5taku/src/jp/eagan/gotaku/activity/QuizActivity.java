package jp.eagan.gotaku.activity;

import jp.eagan.gotaku.R;
import jp.eagan.gotaku.action.QuizAction;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizActivity extends Activity {
	
	public static final String EXTRA_IN_QUIZ = "QUIZ";
	public static final String EXTRA_OUT_ANSWER = "ANSWER";
	
	public static final int RESULT_CANCELED = 0;
	public static final int RESULT_SELECTED = 1;
	public static final int RESULT_TIMEOUT = 2;
	
	private QuizAction quiz;
	
	private Button answer01Button;
	private Button answer02Button;
	private Button answer03Button;
	private Button answer04Button;
	private Button answer05Button;
	
	private ImageView answer01Image;
	private ImageView answer02Image;
	private ImageView answer03Image;
	private ImageView answer04Image;
	private ImageView answer05Image;
	
	private ImageView answer01Mask;
	private ImageView answer02Mask;
	private ImageView answer03Mask;
	private ImageView answer04Mask;
	private ImageView answer05Mask;
	
	private Animation answer01Animation;
	private Animation answer02Animation;
	private Animation answer03Animation;
	private Animation answer04Animation;
	private Animation answer05Animation;
	
	/**
	 * アクティビティを初期化します。
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz);
		
		this.answer01Button = (Button) findViewById(R.id.answer01_button);
		this.answer02Button = (Button) findViewById(R.id.answer02_button);
		this.answer03Button = (Button) findViewById(R.id.answer03_button);
		this.answer04Button = (Button) findViewById(R.id.answer04_button);
		this.answer05Button = (Button) findViewById(R.id.answer05_button);
		
		this.answer01Image = (ImageView) findViewById(R.id.answer01_image);
		this.answer02Image = (ImageView) findViewById(R.id.answer02_image);
		this.answer03Image = (ImageView) findViewById(R.id.answer03_image);
		this.answer04Image = (ImageView) findViewById(R.id.answer04_image);
		this.answer05Image = (ImageView) findViewById(R.id.answer05_image);
		
		this.answer01Mask = (ImageView) findViewById(R.id.answer01_mask);
		this.answer02Mask = (ImageView) findViewById(R.id.answer02_mask);
		this.answer03Mask = (ImageView) findViewById(R.id.answer03_mask);
		this.answer04Mask = (ImageView) findViewById(R.id.answer04_mask);
		this.answer05Mask = (ImageView) findViewById(R.id.answer05_mask);
		
		this.answer01Animation = AnimationUtils.loadAnimation(this, R.anim.answer_mask);
		this.answer02Animation = AnimationUtils.loadAnimation(this, R.anim.answer_mask);
		this.answer03Animation = AnimationUtils.loadAnimation(this, R.anim.answer_mask);
		this.answer04Animation = AnimationUtils.loadAnimation(this, R.anim.answer_mask);
		this.answer05Animation = AnimationUtils.loadAnimation(this, R.anim.answer_mask);
		
		// インテントからパラメータを呼び出す
		this.quiz = (QuizAction) getIntent().getSerializableExtra(EXTRA_IN_QUIZ);
		String question = this.quiz.getQuestion();
		String[] answers = this.quiz.getAnswers();
		
		// 問題文の表示
		TextView questionView = (TextView) findViewById(R.id.question_text);
		questionView.setText(question);
		
		// 回答ボタンの初期化
		this.answer01Button.setText(answers[0]);
		this.answer01Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView answer01Image = (ImageView) findViewById(R.id.answer01_image);
				answerClicked(0, (Button) v, answer01Image);
			}
		});
		this.answer02Button.setText(answers[1]);
		this.answer02Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView answer02Image = (ImageView) findViewById(R.id.answer02_image);
				answerClicked(1, (Button) v, answer02Image);
			}
		});
		this.answer03Button.setText(answers[2]);
		this.answer03Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView answer03Image = (ImageView) findViewById(R.id.answer03_image);
				answerClicked(2, (Button) v, answer03Image);
			}
		});
		this.answer04Button.setText(answers[3]);
		this.answer04Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView answer04Image = (ImageView) findViewById(R.id.answer04_image);
				answerClicked(3, (Button) v, answer04Image);
			}
		});
		this.answer05Button.setText(answers[4]);
		this.answer05Button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView answer05Image = (ImageView) findViewById(R.id.answer05_image);
				answerClicked(4, (Button) v, answer05Image);
			}
		});
	}
	
	/**
	 * 回答に応じた処理を行います。
	 * 
	 * @param selected 選択された回答の番号
	 * @param image 回答の正誤を表示する ImageView
	 */
	void answerClicked(int selected, Button button, ImageView image) {
		// 二度押しを禁止
		this.answer01Button.setClickable(false);
		this.answer02Button.setClickable(false);
		this.answer03Button.setClickable(false);
		this.answer04Button.setClickable(false);
		this.answer05Button.setClickable(false);
		
		// 回答を戻り値として設定
		Intent intent = new Intent();
		intent.putExtra(EXTRA_OUT_ANSWER, selected);
		setResult(RESULT_SELECTED, intent);
		
		int correct = this.quiz.getCorrect();
		
		// 正誤を画像で通知
		int resultDrawableId;
		if (selected == correct) {
			resultDrawableId = R.drawable.correct;
		} else {
			resultDrawableId = R.drawable.incorrect;
		}
		image.setImageResource(resultDrawableId);
		image.setVisibility(View.VISIBLE);
		
		if (correct != 0) {
			this.answer01Mask.setVisibility(View.VISIBLE);
			this.answer01Mask.startAnimation(this.answer01Animation);
		}
		if (correct != 1) {
			this.answer02Mask.setVisibility(View.VISIBLE);
			this.answer02Mask.startAnimation(this.answer02Animation);
		}
		if (correct != 2) {
			this.answer03Mask.setVisibility(View.VISIBLE);
			this.answer03Mask.startAnimation(this.answer03Animation);
		}
		if (correct != 3) {
			this.answer04Mask.setVisibility(View.VISIBLE);
			this.answer04Mask.startAnimation(this.answer04Animation);
		}
		if (correct != 4) {
			this.answer05Mask.setVisibility(View.VISIBLE);
			this.answer05Mask.startAnimation(this.answer05Animation);
		}
		
		CountDownTimer finishTimer = new CountDownTimer(3000, 3000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// onTick での動作は特になし
			}
			
			@Override
			public void onFinish() {
				// アクティビティを終了
				finish();
			}
		};
		finishTimer.start();
	}
	
	/**
	 * アクティビティを終了します。
	 */
	@Override
	public void onStop() {
		super.onStop();
		
		// 正誤通知イメージをすべて透明化
		this.answer01Image.setVisibility(View.INVISIBLE);
		this.answer02Image.setVisibility(View.INVISIBLE);
		this.answer03Image.setVisibility(View.INVISIBLE);
		this.answer04Image.setVisibility(View.INVISIBLE);
		this.answer05Image.setVisibility(View.INVISIBLE);
		
		this.answer01Mask.setVisibility(View.GONE);
		this.answer02Mask.setVisibility(View.GONE);
		this.answer03Mask.setVisibility(View.GONE);
		this.answer04Mask.setVisibility(View.GONE);
		this.answer05Mask.setVisibility(View.GONE);
	}
}
