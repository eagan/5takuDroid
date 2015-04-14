package jp.eagan.gotaku.session;

import jp.eagan.gotaku.QuizBook;
import jp.eagan.gotaku.action.GotakuAction;

public interface GameSession {
	public void init(QuizBook book);
	public GotakuAction next(GotakuAction response);
}
