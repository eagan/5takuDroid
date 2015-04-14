package jp.eagan.gotaku;

import java.io.Serializable;

/**
 * 問題 1 問を保持します。
 * 
 * @author Nagae Hidetake
 *
 */
public class Quiz implements Serializable {
	
	static final long serialVersionUID = 0;
	
	private String question;
	private String answers[];
	private QuizCategory category;
	
	/**
	 * @param question セットする question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param answers セットする answers
	 */
	public void setAnswers(String answers[]) {
		this.answers = answers;
	}

	/**
	 * @return answers
	 */
	public String[] getAnswers() {
		return answers;
	}
	
	public void setCategory(QuizCategory category) {
		this.category = category;
	}
	
	public QuizCategory getCategory() {
		return this.category;
	}
}
