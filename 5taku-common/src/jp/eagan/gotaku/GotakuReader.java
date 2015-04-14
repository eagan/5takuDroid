package jp.eagan.gotaku;

import java.io.IOException;
import java.io.InputStream;

/**
 * 5TQ 形式ファイルを読み込み、問題オブジェクトを構築します。
 * 
 * @author Nagae Hidetake
 *
 */
public class GotakuReader {
	/** 5TQ 形式における文字コード */
	private static final String CHARSETNAME = "Shift_JIS";
	/** 5TQ 形式におけるブロックサイズ（1 問に相当）*/
	private static final int BLOCKLEN = 256;
	
	/** 5TQ 形式におけるジャンル数 */
	private static final int CATEGORYNUM = 8;
	/** 5TQ 形式におけるジャンル名のサイズ（単位はバイト） */
	private static final int CATNAMELEN = 16;
	/** 5TQ 形式におけるジャンル定義の先頭から問題数フィールドまでのオフセット（単位はバイト）*/
	private static final int CATQUIZNUMOFF = 18;
	/** 5TQ 形式におけるジャンル定義の先頭から skip フィールドまでのオフセット（単位はバイト）*/
	private static final int CATSKIPOFF = 20;
	
	/** 5TQ 形式における質問文の長さ（単位はバイト） */
	private static final int QUESTIONLEN = 116;
	/** 5TQ 形式における回答文の長さ（単位はバイト） */
	private static final int ANSWERLEN = 28;
	/** 5TQ 形式における 1 問あたりの回答文の数 */
	private static final int ANSWERNUM = 5;
	
	public static QuizBook read(InputStream in) throws IOException {
		QuizCategory[] categories = new QuizCategory[CATEGORYNUM];
		
		// まず 8 個のジャンル定義を読み込む
		for (int i = 0; i < categories.length; i++) {
			categories[i] = readCategory(in);
		}
		
		// 各ジャンルに対して問題リストを読み込む
		for (int i = 0; i < categories.length; i++) {
			Quiz[] quiz = new Quiz[categories[i].getQuizNum()];
			for (int j = 0; j < quiz.length; j++) {
				quiz[j] = readQuiz(in);
				quiz[j].setCategory(categories[i]);
			}
			categories[i].setQuiz(quiz);
		}
		
		QuizBook book = new QuizBook();
		book.setCategories(categories);
		return book;
	}
	
	/**
	 * 5TQ 形式の入力からジャンル定義 1 個を読み取る。
	 * @param in 入力。
	 * @return 読み取ったジャンル定義。ただし問題はまだ読み取られていない。
	 * @throws IOException 入力ストームの読み取り中に発生した例外。
	 */
	static QuizCategory readCategory(InputStream in) throws IOException {
		byte[] buf = new byte[BLOCKLEN];
		int offset = 0;
		
		// in から 1 ブロックを読み取る。
		// read は途中で切れる可能性があるのでループで読む。
		while (offset < buf.length) {
			int len = in.read(buf, offset, buf.length - offset);
			if (len == -1) {
				// in が終了した
				return null;
				// TODO size が 0 でない場合はジャンルの途中で終了しているので、何か例外を返すべきでは
			} else {
				offset += len;
			}
		}
		
		// buf から各フィールドへ変換
		String name = new String(buf, 0, CATNAMELEN, CHARSETNAME).trim();
		int quizNumLow = ((int) buf[CATQUIZNUMOFF]);
		if (quizNumLow < 0)
			quizNumLow += 256;
		int quizNumHigh = ((int) buf[CATQUIZNUMOFF + 1]);
		if (quizNumHigh < 0)
			quizNumHigh += 256;
		int quizNum = quizNumHigh * 256 + quizNumLow;
		int skipLow = ((int) buf[CATSKIPOFF]);
		if (skipLow < 0)
			skipLow += 256;
		int skipHigh = ((int) buf[CATSKIPOFF + 1]);
		if (skipHigh < 0)
			skipHigh += 256;
		int skip = skipHigh * 256 + skipLow;
		
		// 読み取ったフィールド値を QuizCategory オブジェクトに設定
		QuizCategory quizCategory = new QuizCategory();
		quizCategory.setName(name);
		quizCategory.setQuizNum(quizNum);
		quizCategory.setSkip(skip);
		
		return quizCategory;
	}
	
	/**
	 * 5TQ 形式の入力から問題 1 問を読み取る。
	 * 
	 * @param in 5TQ 形式の入力ストリーム。
	 * @return 読み取った問題データ。
	 * @throws IOException 入力ストームの読み取り中に発生した例外。
	 */
	static Quiz readQuiz(InputStream in) throws IOException {
		byte[] buf = new byte[BLOCKLEN];
		int offset = 0;
		
		// in から 1 ブロックを読み取る。
		// read は途中で切れる可能性があるのでループで読む。
		while (offset < buf.length) {
			int len = in.read(buf, offset, buf.length - offset);
			if (len == -1) {
				// in が終了した
				return null;
				// TODO size が 0 でない場合は問題の途中で終了しているので、何か例外を返すべきでは
			} else {
				offset += len;
			}
		}
		
		// buf から各フィールドへ変換
		// なぜか問題レコードは最上位ビットが反転している。
		// ただしスペース（0x20）はそのままの様子。難読化？
		boolean sjis = false;
		for (int i = 0; i < buf.length; i++) {
			if (sjis) {
				buf[i] ^= 0x80;
				sjis = false;
			} else {
				if (buf[i] != 0x20)
					buf[i] ^= 0x80;
				if ((byte)0x81 <= buf[i] && buf[i] <= (byte)0x9f || (byte)0xe0 <= buf[i] && buf[i] <= (byte)0xfc)
					sjis = true;
			}
		}
		String question = new String(buf, 0, QUESTIONLEN, CHARSETNAME).trim();
		String[] answers = new String[ANSWERNUM];
		for (int i = 0; i < ANSWERNUM; i++) {
			answers[i] = new String(buf, QUESTIONLEN + i * ANSWERLEN, ANSWERLEN, CHARSETNAME).trim();
		}
		
		// 読み取ったフィールド値を Quiz オブジェクトに設定
		Quiz quiz = new Quiz();
		quiz.setQuestion(question);
		quiz.setAnswers(answers);
		return quiz;
	}
}
