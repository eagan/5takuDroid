package jp.eagan.gotaku.action;

import jp.eagan.gotaku.Quiz;

public class QuizAction implements GotakuAction {
	static final long serialVersionUID = 0;
	
	private Quiz quiz;
	private long timeout;
	private String[] shuffledAnswers;
	private int correctChoice;
	private int usersChoice;
	
	private static final int CHOICE_NOT_SELECTED = -1;
	
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
		this.shuffledAnswers = null;
		this.usersChoice = CHOICE_NOT_SELECTED;
	}
	
	public void setShuffleMap(int[] map) {
		String[] answers = this.quiz.getAnswers();
		this.shuffledAnswers = new String[map.length];
		for (int i = 0; i < map.length; i++) {
			shuffledAnswers[i] = answers[map[i]];
			if (map[i] == 0)
				this.correctChoice = i;
		}
	}
	
	public String getQuestion() {
		return this.quiz.getQuestion();
	}
	
	public String[] getAnswers() {
		String[] answers;
		if (this.shuffledAnswers == null)
			answers = this.quiz.getAnswers();
		else
			answers = this.shuffledAnswers;
		return answers;
	}
	
	public int getCorrect() {
		int correctChoice;
		if (this.shuffledAnswers == null)
			correctChoice = 0;
		else
			correctChoice = this.correctChoice;
		return correctChoice;
	}
	
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public long getTimeout() {
		return this.timeout;
	}
	
	public void setUsersChoice(int choice) {
		this.usersChoice = choice;
	}
	
	public int getUsersChoice() {
		return this.usersChoice;
	}
}
