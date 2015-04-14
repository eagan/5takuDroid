package jp.eagan.gotaku;

import java.io.Serializable;

/**
 * 1 ジャンルの定義およびそのジャンルに含まれる問題を保持します。
 * 
 * @author Nagae Hidetake
 *
 */
public class QuizCategory implements Serializable {

	static final long serialVersionUID = 0;
	
	private String name;
	private int quizNum;
	private int skip;
	private Quiz[] quiz;

	/**
	 * @param name セットするジャンル名。
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return ジャンル名。
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param quizNum セットする問題数。
	 */
	public void setQuizNum(int quizNum) {
		this.quizNum = quizNum;
	}
	/**
	 * @return 問題数。
	 */
	public int getQuizNum() {
		return quizNum;
	}
	/**
	 * 
	 * @param skip セットするskip。
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}
	public int getSkip() {
		return skip;
	}
	/**
	 * @param quiz セットする問題の配列。
	 */
	public void setQuiz(Quiz[] quiz) {
		this.quiz = quiz;
	}
	/**
	 * @return 問題の配列。
	 */
	public Quiz[] getQuiz() {
		return quiz;
	}
}
