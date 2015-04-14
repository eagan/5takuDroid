package jp.eagan.gotaku;

import java.io.IOException;
import java.io.InputStream;

/**
 * 5TQ �`���t�@�C����ǂݍ��݁A���I�u�W�F�N�g���\�z���܂��B
 * 
 * @author Nagae Hidetake
 *
 */
public class GotakuReader {
	/** 5TQ �`���ɂ����镶���R�[�h */
	private static final String CHARSETNAME = "Shift_JIS";
	/** 5TQ �`���ɂ�����u���b�N�T�C�Y�i1 ��ɑ����j*/
	private static final int BLOCKLEN = 256;
	
	/** 5TQ �`���ɂ�����W�������� */
	private static final int CATEGORYNUM = 8;
	/** 5TQ �`���ɂ�����W���������̃T�C�Y�i�P�ʂ̓o�C�g�j */
	private static final int CATNAMELEN = 16;
	/** 5TQ �`���ɂ�����W��������`�̐擪�����萔�t�B�[���h�܂ł̃I�t�Z�b�g�i�P�ʂ̓o�C�g�j*/
	private static final int CATQUIZNUMOFF = 18;
	/** 5TQ �`���ɂ�����W��������`�̐擪���� skip �t�B�[���h�܂ł̃I�t�Z�b�g�i�P�ʂ̓o�C�g�j*/
	private static final int CATSKIPOFF = 20;
	
	/** 5TQ �`���ɂ����鎿�╶�̒����i�P�ʂ̓o�C�g�j */
	private static final int QUESTIONLEN = 116;
	/** 5TQ �`���ɂ�����񓚕��̒����i�P�ʂ̓o�C�g�j */
	private static final int ANSWERLEN = 28;
	/** 5TQ �`���ɂ����� 1 �₠����̉񓚕��̐� */
	private static final int ANSWERNUM = 5;
	
	public static QuizBook read(InputStream in) throws IOException {
		QuizCategory[] categories = new QuizCategory[CATEGORYNUM];
		
		// �܂� 8 �̃W��������`��ǂݍ���
		for (int i = 0; i < categories.length; i++) {
			categories[i] = readCategory(in);
		}
		
		// �e�W�������ɑ΂��Ė�胊�X�g��ǂݍ���
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
	 * 5TQ �`���̓��͂���W��������` 1 ��ǂݎ��B
	 * @param in ���́B
	 * @return �ǂݎ�����W��������`�B���������͂܂��ǂݎ���Ă��Ȃ��B
	 * @throws IOException ���̓X�g�[���̓ǂݎ�蒆�ɔ���������O�B
	 */
	static QuizCategory readCategory(InputStream in) throws IOException {
		byte[] buf = new byte[BLOCKLEN];
		int offset = 0;
		
		// in ���� 1 �u���b�N��ǂݎ��B
		// read �͓r���Ő؂��\��������̂Ń��[�v�œǂށB
		while (offset < buf.length) {
			int len = in.read(buf, offset, buf.length - offset);
			if (len == -1) {
				// in ���I������
				return null;
				// TODO size �� 0 �łȂ��ꍇ�̓W�������̓r���ŏI�����Ă���̂ŁA������O��Ԃ��ׂ��ł�
			} else {
				offset += len;
			}
		}
		
		// buf ����e�t�B�[���h�֕ϊ�
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
		
		// �ǂݎ�����t�B�[���h�l�� QuizCategory �I�u�W�F�N�g�ɐݒ�
		QuizCategory quizCategory = new QuizCategory();
		quizCategory.setName(name);
		quizCategory.setQuizNum(quizNum);
		quizCategory.setSkip(skip);
		
		return quizCategory;
	}
	
	/**
	 * 5TQ �`���̓��͂����� 1 ���ǂݎ��B
	 * 
	 * @param in 5TQ �`���̓��̓X�g���[���B
	 * @return �ǂݎ�������f�[�^�B
	 * @throws IOException ���̓X�g�[���̓ǂݎ�蒆�ɔ���������O�B
	 */
	static Quiz readQuiz(InputStream in) throws IOException {
		byte[] buf = new byte[BLOCKLEN];
		int offset = 0;
		
		// in ���� 1 �u���b�N��ǂݎ��B
		// read �͓r���Ő؂��\��������̂Ń��[�v�œǂށB
		while (offset < buf.length) {
			int len = in.read(buf, offset, buf.length - offset);
			if (len == -1) {
				// in ���I������
				return null;
				// TODO size �� 0 �łȂ��ꍇ�͖��̓r���ŏI�����Ă���̂ŁA������O��Ԃ��ׂ��ł�
			} else {
				offset += len;
			}
		}
		
		// buf ����e�t�B�[���h�֕ϊ�
		// �Ȃ�����背�R�[�h�͍ŏ�ʃr�b�g�����]���Ă���B
		// �������X�y�[�X�i0x20�j�͂��̂܂܂̗l�q�B��ǉ��H
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
		
		// �ǂݎ�����t�B�[���h�l�� Quiz �I�u�W�F�N�g�ɐݒ�
		Quiz quiz = new Quiz();
		quiz.setQuestion(question);
		quiz.setAnswers(answers);
		return quiz;
	}
}
