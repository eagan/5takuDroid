package jp.eagan.gotaku;

public class QuizBook {
	private String name;
	private QuizCategory[] categories;

	/**
	 * ���W�̖��O��ݒ肵�܂��B
	 * 
	 * @param name ���W�̖��O
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ���W�̖��O���擾���܂��B
	 * 
	 * @return name ���W�̖��O
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���W�������̔z���ݒ肵�܂��B
	 * 
	 * @param categories ���W�������̔z��
	 */
	public void setCategories(QuizCategory[] categories) {
		this.categories = categories;
	}

	/**
	 * ���W�������̔z����擾���܂��B
	 * 
	 * @return categories ���W�������̔z��
	 */
	public QuizCategory[] getCategories() {
		return this.categories;
	}
}
