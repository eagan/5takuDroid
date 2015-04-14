package jp.eagan.gotaku;

public class QuizBook {
	private String name;
	private QuizCategory[] categories;

	/**
	 * 問題集の名前を設定します。
	 * 
	 * @param name 問題集の名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 問題集の名前を取得します。
	 * 
	 * @return name 問題集の名前
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 問題ジャンルの配列を設定します。
	 * 
	 * @param categories 問題ジャンルの配列
	 */
	public void setCategories(QuizCategory[] categories) {
		this.categories = categories;
	}

	/**
	 * 問題ジャンルの配列を取得します。
	 * 
	 * @return categories 問題ジャンルの配列
	 */
	public QuizCategory[] getCategories() {
		return this.categories;
	}
}
