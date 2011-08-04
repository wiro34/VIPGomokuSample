package vip2011.tic;

/**
 * Frames Per Secondをコントロールするためのクラスです。
 * 
 * <p>メインループ内で{@link #sleep()}をコールすることでフレームレートを一定に保つために適切な時間のsleepを行います。
 * ただし、目標とするFPSが実現可能な範囲を越えている場合は何もしません。</p>
 * 
 * @see <a href="http://d.hatena.ne.jp/aidiary/20070429/1251463673">正確なFPS - 人工知能に関する断想録</a>
 */
class FPSController {
	private long lastTime;
	private long period;
	private long overSleepTime;
	
	private double actualFPS;
	private long frames, calcInterval, preCalcTime;
	
	/**
	 * 指定したFPSになるべく近づけるためのFPSコントローラを作成します。
	 * 
	 * @param requestFPS FPSの目標値
	 */
	public FPSController(int requestFPS) {
		this.period = (requestFPS > 0) ? 1000000000L / requestFPS : 0;
		this.lastTime = System.nanoTime();
	}
	
	/**
	 * FPSを安定させるために一定時間スレッドを停止させます。
	 */
	public void sleep() {
		long current = System.nanoTime();
		long diff = current - lastTime;
		long sleepTime = (period - diff) - overSleepTime;
		
		if (sleepTime > 0) {
			try {
				Thread.sleep(sleepTime / 1000000L, (int)(sleepTime % 1000000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			overSleepTime = (System.nanoTime() - current) - sleepTime;
		} else {
			overSleepTime = 0;
			Thread.yield();
		}
		
		lastTime = System.nanoTime();
	}
	
	/**
	 * 実際のフレームレートを取得します。
	 * この値は500msごとに更新されます。
	 * 
	 * @return フレームレート
	 */
	public double getActualFPS() {
		frames++;
		long now = System.nanoTime();
		long elapsed = now - preCalcTime;
		calcInterval += elapsed;
		if (calcInterval >= 500000000L) {
			actualFPS = ((double)frames / calcInterval) * 2000000000L;
			frames = 0;
			calcInterval = 0;
		}
		preCalcTime = now;
		
		return actualFPS;
	}
}
