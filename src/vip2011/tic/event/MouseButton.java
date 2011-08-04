package vip2011.tic.event;

/**
 * マウスボタンの状態を保持するクラスです。
 */
public class MouseButton extends InputState {
	private int button;

	/**
	 * 指定したマウスボタンの状態を表します。
	 * 
	 * @param button マウスボタン定数 (MouseEventの定数)
	 * @see MouseEvent
	 */
	public MouseButton(int button) {
		this.button = button;
	}

	/**
	 * 保持しているマウスボタンを取得します。
	 * 
	 * @return マウスボタン定数
	 */
	public int getButton() {
		return button;
	}
}
