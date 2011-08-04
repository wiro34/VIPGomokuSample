package sample.gomoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import vip2011.tic.Board;
import vip2011.tic.BoardDrawer;
import vip2011.tic.TICApplet;
import vip2011.tic.event.Mouse;
import vip2011.tic.event.MouseButton;

/**
 * 五目並べっぽい何か
 * 三三とかは無視してます。
 */
public class Gomoku extends TICApplet {
	//-------------------------------------------------------------------------
	// フィールド
	//-------------------------------------------------------------------------
	private static final long serialVersionUID = 7430678560091910053L;
	
	private static final int BOARD_WIDTH    = 20;
	private static final int BOARD_HEIGHT   = 20;
	private static final int BOARD_CELLSIZE = 20;

	private static final int FIRST  = 1;
	private static final int SECOND = 2;
	private static final int CURSOR = 3;
	
	private Board board;
	private BoardDrawer viewer;
	
	private Mouse mouse;
	private MouseButton button1;
	
	private boolean turn, finished;

	//-------------------------------------------------------------------------
	// メソッド
	//-------------------------------------------------------------------------
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialize() {
		// ボードの作成
		board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
		viewer = board.createDrawer(BOARD_CELLSIZE, 1, Color.BLACK);
		
		// 色を設定
		viewer.bindColor(FIRST, Color.BLUE);
		viewer.bindColor(SECOND, Color.RED);
		viewer.bindColor(CURSOR, new Color(128, 128, 128, 128));
		
		// マウスの取得
		mouse = getMouse();
		
		// マウスボタンの取得
		button1 = getButton(MouseEvent.BUTTON1);
		
		// FPSを設定
		setRequestFPS(30);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateFrame(long elapsed) {
		int boardX = viewer.getCenteringX(getWidth());
		int boardY = viewer.getCenteringY(getHeight());
		int selX = viewer.getBoardXFromMouseX(boardX, mouse.getX());
		int selY = viewer.getBoardYFromMouseY(boardY, mouse.getY());
		
		// マウスの重なっているマスの色付け
		if (0 <= selX && 0 <= selY) {
			viewer.setCursor(selX, selY, CURSOR);
			
			// クリックされたマスに手を置く
			if (!finished && button1.isDown() && board.getData(selX, selY) == Board.NODATA) {
				board.setData(selX, selY, (turn) ? FIRST : SECOND);
				if (checkFinish(selX, selY)) {
					finished = true;
				} else {
					turn = !turn;
				}
			}
		} else
			viewer.removeCursor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawCanvas(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int x = viewer.getCenteringX(getWidth());
		int y = viewer.getCenteringY(getHeight());
		viewer.draw(g, x, y);
		
		String turnStr = (turn) ? "後攻" : "先攻";
		g.setColor(Color.BLACK);
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		g.drawString(turnStr + "の番です。", x, y - 30);
		
		if (finished) {
			g.setColor(Color.RED);
			g.drawString(turnStr + "の勝利！", x + 200, y - 30);
		}
	}
	
	/**
	 * ゲーム終了をチェックします。
	 * 
	 * @return
	 */
	public boolean checkFinish(int x, int y) {
		int id = board.getData(x, y);
		return checkFinishInner(x, y, -1, -1, id, 1) |
		       checkFinishInner(x, y,  0, -1, id, 1) |
		       checkFinishInner(x, y,  1, -1, id, 1) |
		       checkFinishInner(x, y, -1,  0, id, 1) |
		       checkFinishInner(x, y,  1,  0, id, 1) |
		       checkFinishInner(x, y, -1,  1, id, 1) |
		       checkFinishInner(x, y,  0,  1, id, 1) |
		       checkFinishInner(x, y,  1,  1, id, 1);
	}
	
	/*
	 * 指定した座標の8近傍を調べて同じIDのマスがあるか調べる
	 * 
	 * #こういう関数は内部関数にしたいなあ
	 */
	private boolean checkFinishInner(int x, int y, int dx, int dy, int id, int count) {
		int tx = x + dx, ty = y + dy;
		if (0 <= tx && tx < board.getWidth() && 0 <= ty && ty < board.getHeight()) {
			if (count == 5)
				return true;
			else if (board.getData(tx, ty) == id)
				return checkFinishInner(tx, ty, dx, dy, id, count + 1);
		}
		return false;
	}
}
