package vip2011.tic.event;


/**
 * キーの状態を表すクラスです。
 */
public class Key extends InputState {
	private int keyCode;

	/**
	 * 指定したキーの状態を表します。
	 * 
	 * @param keyCode キーコード (KeyEventクラスの定数)
	 * @see KeyEvent
	 */
	public Key(int keyCode) {
		this.keyCode = keyCode;
	}

	/**
	 * このインスタンスが保持しているキーのキーコードを取得します。
	 * 
	 * @return キーコード
	 */
	public int getKeyCode() {
		return keyCode;
	}
}
