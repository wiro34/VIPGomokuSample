package vip2011.tic.event;

/**
 * 入力状態を保持するクラスです。
 */
public class InputState {
	/** 入力が無い状態を表す定数です。 */
	protected static final int FREE = 0;
	
	/** 入力が押された瞬間を表す定数です。 */
	protected static final int DOWN = 1;
	
	/** 入力が押されている状態を表す定数です。 */
	protected static final int HOLD = 2;
	
	/** 入力が離された瞬間を表す定数です。 */
	protected static final int UP = 3;
	
	public int state = FREE;

	/**
	 * 入力が無い状態かどうかを調べます。
	 * 
	 * @return 入力が無い状態かどうか
	 */
	public synchronized boolean isFree() {
		return state == FREE;
	}
	
	/**
	 * 入力が押された瞬間かどうかを調べます。
	 * 
	 * @return 入力が押された瞬間かどうか
	 */
	public synchronized boolean isDown() {
		if (state == DOWN) {
			state = HOLD;
			return true;
		}
		return false;
	}
	
	/**
	 * 入力が押されている状態かどうかを調べます。
	 * 
	 * @return 入力が押されている状態かどうか
	 */
	public synchronized boolean isHold() {
		return state == HOLD;
	}

	/**
	 * 入力が離された瞬間かどうかを調べます。
	 * 
	 * @return 入力が離された瞬間かどうか
	 */
	public synchronized boolean isUp() {
		if (state == UP) {
			state = FREE;
			return true;
		}
		return false;
	}
	
	/**
	 * 入力が押されているどうかを調べます。
	 * このメソッドは <code>isDown() || isHold()</code> と等価です。
	 * 
	 * @return 入力があるかどうか
	 */
	public synchronized boolean isPressed() {
		return isDown() || isHold();
	}
	
	/**
	 * 入力が離されたかどうかを調べます。
	 * このメソッドは <code>isUp()</code> と等価です。
	 * 
	 * @return 入力が離されたかどうか
	 */
	public synchronized boolean isReleased() {
		return isUp();
	}
	
	/**
	 * 入力があったことを通知します。
	 */
	public synchronized void press() {
		if (!isPressed())	state = DOWN;
		else				state = HOLD;
	}
	
	/**
	 * 入力が離されたことを通知します。
	 */
	public synchronized void release() {
		if (isPressed())	state = UP;
		else				state = FREE;
	}
}
