package jp.eagan.gotaku;

import java.io.Serializable;

/**
 * 1 �W�������̒�`����т��̃W�������Ɋ܂܂�����ێ����܂��B
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
	 * @param name �Z�b�g����W���������B
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return �W���������B
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param quizNum �Z�b�g�����萔�B
	 */
	public void setQuizNum(int quizNum) {
		this.quizNum = quizNum;
	}
	/**
	 * @return ��萔�B
	 */
	public int getQuizNum() {
		return quizNum;
	}
	/**
	 * 
	 * @param skip �Z�b�g����skip�B
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}
	public int getSkip() {
		return skip;
	}
	/**
	 * @param quiz �Z�b�g������̔z��B
	 */
	public void setQuiz(Quiz[] quiz) {
		this.quiz = quiz;
	}
	/**
	 * @return ���̔z��B
	 */
	public Quiz[] getQuiz() {
		return quiz;
	}
}
