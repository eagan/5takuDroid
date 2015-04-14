package jp.eagan.gotaku.session;

import java.util.Random;

import jp.eagan.gotaku.Quiz;
import jp.eagan.gotaku.QuizBook;
import jp.eagan.gotaku.QuizCategory;
import jp.eagan.gotaku.RandomUtil;
import jp.eagan.gotaku.action.GotakuAction;
import jp.eagan.gotaku.action.QuizAction;

public class EndlessGameSession implements GameSession {
	private QuizBook book;
	private Quiz[] quizQueue;
	private int cursor;
	private Random rand;
	
	public void init(QuizBook book) {
		this.rand = new Random();
		this.book = book;
		
		QuizCategory[] categories = book.getCategories();
		int numQuiz = 0;
		for (int i = 0; i < categories.length; i++) {
			numQuiz += categories[i].getQuizNum();
		}
		this.quizQueue = new Quiz[numQuiz];
		int idx = 0;
		for (int i = 0; i < categories.length; i++) {
			Quiz[] categoryQuiz = categories[i].getQuiz();
			for (int j = 0; j < categoryQuiz.length; j++) {
				this.quizQueue[idx] = categoryQuiz[j];
				idx++;
			}
		}
		RandomUtil.shuffle(this.quizQueue, this.rand);
		this.cursor = 0;
	}
	
	public GotakuAction next(GotakuAction response) {
		// TODO 回答に対する処理（集計など？）
		
		// 次の出題
		if (this.cursor >= this.quizQueue.length) {
			RandomUtil.shuffle(this.quizQueue, this.rand);
			this.cursor = 0;
		}
		QuizAction nextAction = new QuizAction();
		nextAction.setQuiz(this.quizQueue[cursor]);
		int[] answersShuffleMap = new int[quizQueue[cursor].getAnswers().length];
		for (int i = 0; i < answersShuffleMap.length; i++)
			answersShuffleMap[i] = i;
		RandomUtil.shuffle(answersShuffleMap, this.rand);
		nextAction.setShuffleMap(answersShuffleMap);
		this.cursor++;
		
		return nextAction;
	}
}
