package vip2011.tic.event;

/**
 * マウスの状態（座標）を保持するクラスです。
 */
public class Mouse {
	private int x, y;

	/**
	 * マウスのX座標を取得します。
	 * 
	 * @return マウスのX座標
	 */
	public int getX() {
		return x;
	}

	/**
	 * マウスのX座標を設定します。
	 * 
	 * @param x マウスのX座標
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * マウスのY座標を取得します。
	 * 
	 * @return マウスのY座標
	 */
	public int getY() {
		return y;
	}

	/**
	 * マウスのY座標を設定します。
	 * 
	 * @param y マウスのY座標
	 */
	public void setY(int y) {
		this.y = y;
	}
}
