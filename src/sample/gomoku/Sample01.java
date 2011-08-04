package sample.gomoku;

import java.awt.Color;
import java.awt.Graphics2D;

import vip2011.tic.Board;
import vip2011.tic.BoardDrawer;
import vip2011.tic.TICApplet;

public class Sample01 extends TICApplet {

	private Board board;
	private BoardDrawer drawer;
	
	private float ballX, ballY;
	private float ballVX, ballVY;
	
	@Override
	public void initialize() {
		this.board = new Board(40, 40);
		this.drawer = board.createDrawer(10, 1, Color.GRAY);
		
		// ボードの色を設定
		drawer.bindColor(1, Color.BLUE);
		
		// ボールの速度
		ballVX = 2f;
		ballVY = 7f;
		
		// ボールの初期位置
		ballX = 0;
		ballY = 39;
		
		// ボールのデータをセット
		board.setData((int)ballX, (int)ballY, 1);

		board.fillRect(2, 2, 4, 4, 1);
		board.fillOval(10, 2, 10, 6, 1);
		
		// fps
		setRequestFPS(10);
	}

	@Override
	public void updateFrame(long elapsed) {
		int oldX = (int)ballX;
		int oldY = (int)ballY;
		
		ballX += ballVX;
		ballY += ballVY;
		if (ballX >= 39 || ballX <= 0) {
			ballVX *= -1;
			ballX = (ballX >= 39) ? 39 : 0;
		}
		if (ballY >= 39) {
			ballVY = -8f;
			ballY = 39;
		}
		ballVY += 1f;
		
		// データの更新
		board.moveData(oldX, oldY, (int)ballX, (int)ballY);
		
		System.out.println(ballX + "," + ballY);
	}
	
	@Override
	public void drawCanvas(Graphics2D g) {
		drawer.draw(g, 0, 0);
	}

}
