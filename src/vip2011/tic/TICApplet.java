package vip2011.tic;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JPanel;

import vip2011.tic.event.Key;
import vip2011.tic.event.Mouse;
import vip2011.tic.event.MouseButton;

/**
 * 
 * @author inoue
 */
public abstract class TICApplet extends JApplet implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	//-------------------------------------------------------------------------
	// フィールド
	//-------------------------------------------------------------------------
	private static final long serialVersionUID = -461530548260448039L;
	
	private TICCanvas canvas;

	private BufferedImage buffer;

	private Map<Integer, Key> keyMap = new HashMap<Integer, Key>();
	private MouseButton[] btnMap = new MouseButton[3];
	private Mouse mouse = new Mouse();
	
	private FPSController fpscon = new FPSController(0);

	//-------------------------------------------------------------------------
	// メソッド
	//-------------------------------------------------------------------------
	/**
	 * アプレットを初期化します。
	 * 
	 * 継承したクラスでアプレットの初期化を行いたい場合は、
	 * TICApplet#initialize()メソッドをオーバライドしてください。
	 */
	@Override
	public final void init() {
		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		canvas = new TICCanvas();
		canvas.setPreferredSize(new Dimension(getWidth(), getHeight()));

		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseWheelListener(this);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(canvas, BorderLayout.CENTER);
		
		initialize();
	}
	
	/**
	 * 
	 */
	public final void start() {
		canvas.requestFocus();
		
		// メインループを開始
		new Thread(this).start();
	}

	/**
	 * メインループ
	 */
	@Override
	public final void run() {
		Graphics2D g = buffer.createGraphics();
		long lastTime = System.nanoTime();
		while (true) {
			long elapsed = System.nanoTime() - lastTime;
			lastTime = System.nanoTime();
			
			// 更新処理
			updateFrame(elapsed);
			
			// 描画処理
			drawCanvas(g);
			
			// キャンバスにコピー
			Graphics cg = canvas.getGraphics();
			if (cg != null)
				cg.drawImage(buffer, 0, 0, null);
			
			// FPSを調整
			fpscon.sleep();
		}
	}
	
	
	/**
	 * 画面を描画します。
	 * 
	 * @param g Graphics2D
	 */
	public abstract void drawCanvas(Graphics2D g);

	/**
	 * アプレットを初期化します。
	 */
	public abstract void initialize();
	
	/**
	 * ゲームを更新します。
	 * 
	 * @param elapsed 前フレームからの経過時間(ns)
	 */
	public abstract void updateFrame(long elapsed);

	/**
	 * ゲームループのフレームレートを指定したFPSになるべく近づけるように設定します。
	 * 
	 * @param requestFPS FPSの目標値
	 */
	public void setRequestFPS(int requestFPS) {
		fpscon = new FPSController(requestFPS);
	}
	
	/**
	 * 指定したキーを取得します。
	 * 
	 * @param keyCode キーコード（KeyEventクラスの定数）
	 * @return {@link Key}
	 * @see KeyEvent
	 */
	public final Key getKey(int keyCode) {
		if (keyCode < 0 || keyCode >= KeyEvent.CHAR_UNDEFINED)
			throw new IllegalArgumentException("使用できないキーです。");
		Key ak = new Key(keyCode);
		keyMap.put(keyCode, ak);
		return ak;
	}
	
	/**
	 * 指定したマウスボタンを取得します。
	 * 
	 * @param button ボタン（MouseEventクラスの定数）
	 * @return {@link MouseButton}
	 * @see MouseEvent
	 */
	public final MouseButton getButton(int button) {
		if (button <= 0 || button > MouseEvent.BUTTON3)
			throw new IllegalArgumentException("使用できないボタンです。");
		if (btnMap[button - 1] == null)
			btnMap[button - 1] = new MouseButton(button);
		return btnMap[button - 1];
	}
	
	/**
	 * マウスを取得します。
	 * 
	 * @return {@link Mouse}
	 */
	public final Mouse getMouse() {
		return mouse;
	}

	//-------------------------------------------------------------------------
	// イベントリスナ（各メソッドをオーバーロードして適宜処理を行ってください。）
	//-------------------------------------------------------------------------
	/** {@inheritDoc} */
	public void keyPressed(KeyEvent e) {}

	/** {@inheritDoc} */
	public void keyReleased(KeyEvent e) {}

	/** {@inheritDoc} */
	public void keyTyped(KeyEvent e) {}

	/** {@inheritDoc} */
	public void mouseClicked(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseDragged(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseEntered(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseExited(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseMoved(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mousePressed(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseReleased(MouseEvent e) {}

	/** {@inheritDoc} */
	public void mouseWheelMoved(MouseWheelEvent e) {}
	
	//-------------------------------------------------------------------------
	// インナクラス
	//-------------------------------------------------------------------------
	/*
	 * キャンバス
	 */
	class TICCanvas extends JPanel {
		private static final long serialVersionUID = 3765321532635226678L;
		
		public TICCanvas() {
			// フォーカス可能にする（これをしないとJPanelでキーイベントを取れない）
			setFocusable(true);
			
			// イベントを設定
			enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | 
					AWTEvent.MOUSE_MOTION_EVENT_MASK/* | AWTEvent.MOUSE_WHEEL_EVENT_MASK*/);
		}
		
		@Override
		protected void processEvent(AWTEvent e) {
			// キーイベント
			if (e instanceof KeyEvent) {
				Key key = keyMap.get(((KeyEvent)e).getKeyCode());
				if (key != null) {
					switch (e.getID()) {
					case KeyEvent.KEY_PRESSED:  key.press();   break;
					case KeyEvent.KEY_RELEASED: key.release(); break;
					}
				}
			}
			
			// マウスイベント
			else if (e instanceof MouseEvent) {
				MouseEvent me = (MouseEvent)e;
	
				mouse.setX(me.getX());
				mouse.setY(me.getY());
				
				if (me.getButton() > MouseEvent.NOBUTTON) {
					MouseButton btn = btnMap[me.getButton() - 1];
					if (btn != null) {
						switch (e.getID()) {
						case MouseEvent.MOUSE_PRESSED:  btn.press();   break;
						case MouseEvent.MOUSE_RELEASED: btn.release(); break;
						}
					}
				}
			}
			
			super.processEvent(e);
		}
	}
}
