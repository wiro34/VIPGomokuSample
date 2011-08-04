package vip2011.tic;

import java.awt.Color;

/**
 * N×Mサイズのボードを扱うクラスです。
 * ボードの各マス目には整数の値を格納することが出来ます。
 * ボードを画面上に描画するには BoardViewer クラスを利用してください。
 * <code><pre>
 * Board board = new Board(20, 20);
 * BoardViewer viewer = new BoardViewer(board, 10);
 * 
 * Graphics g = ...;
 * viewer.paint(g, x, y);
 * <pre></code>
 */
public class Board {
	//-------------------------------------------------------------------------
	// フィールド
	//-------------------------------------------------------------------------
	/** データが何も格納されていないことを表す定数です。 */
	public static final int NODATA = 0;

	private int[][] data;
	private int width, height;
	private boolean changed;

	//-------------------------------------------------------------------------
	// メソッド
	//-------------------------------------------------------------------------
	/**
	 * 指定したサイズの、指定したマスのサイズ及び、指定された枠の太さと色で表示するボードを新しく作成します。
	 * 
	 * @param width ボードの横幅
	 * @param height ボードの縦幅
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new int[width][height];
		
		clear();
		changed = false;
	}
	
	/**
	 * 全てのマスを NODATA にクリアします。
	 */
	public void clear() {
		clear(NODATA);
	}
	
	/**
	 * 指定したデータでボードをクリアします。
	 * 
	 * @param data
	 */
	public void clear(int data) {
		fillRect(0, 0, width, height, data);
	}
	
	/**
	 * 指定した(srcX, srcY)にあるデータを、(dstX, dstY)に移動させます。
	 * また、移動後の(srcX,srcY)には NODATA 定数が設定されます。
	 * このメソッドは、以下のコードと等価です。
	 * <code><pre>
	 * int data = getData(srcX, srcY);
	 * setData(dstX, dstY, data);
	 * setData(srcX, srcY, NODATA);
	 * </pre></code>
	 * 
	 * @param srcX 移動元のX座標
	 * @param srcY 移動元のY座標
	 * @param dstX 移動元のX座標
	 * @param dstY 移動先のY座標
	 */
	public void moveData(int srcX, int srcY, int dstX, int dstY) {
		int data = getData(srcX, srcY);
		setData(dstX, dstY, data);
		setData(srcX, srcY, NODATA);
	}
	
	/**
	 * 指定した座標のセルを指定した値に変更します。
	 * 指定した座標がボードの範囲を越えている場合、このメソッドは {@link IllegalArgumentException} をスローします。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @param data 設定する値
	 */
	public void setData(int x, int y, int data) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException("不正な座標です。");
		this.data[x][y] = data;
		changed = true;
	}
	
	/**
	 * 指定した座標のセルの値を取得します。
	 * 指定した座標がボードの範囲を越えている場合、このメソッドは {@link IllegalArgumentException} をスローします。
	 * 
	 * @param x X座標
	 * @param y Y座標
	 * @return　指定した座標のセルの色
	 */
	public int getData(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException("不正な座標です。");
		return data[x][y];
	}

	/**
	 * ボードの横幅を取得します。
	 * 
	 * @return ボードの横幅
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * ボードの縦幅を取得します。
	 * 
	 * @return ボードの縦幅
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 以前にこのメソッドをコールしたときから、ボードの状態が変更されたかどうかを取得します。
	 * 
	 * @return ボードの状態が変更されたかどうか
	 */
	boolean isChanged() {
		boolean now = changed;
		changed = false;
		return now;
	}

	/**
	 * このボードを描画するための {@link BoardDrawer} を生成します。
	 * アプレット画面にボードを描画する場合は、このメソッドを利用すると便利です。
	 * 
	 * @param cellSize マスのサイズ
	 * @param gridWidth 枠の太さ
	 * @param gridColor 枠の色
	 * @return 生成された {@link BoardDrawer}
	 */
	public BoardDrawer createDrawer(int cellSize, int gridWidth, Color gridColor) {
		return new BoardDrawer(this, cellSize, gridWidth, gridColor);
	}

	/**
	 * 指定した矩形範囲を data で塗りつぶします。
	 * 
	 * @param x 矩形の左上のX座標
	 * @param y 矩形の左上のY座標
	 * @param width 矩形の横幅
	 * @param height 矩形の縦幅
	 * @param data 塗りつぶす値
	 */
	public void fillRect(int x, int y, int width, int height, int data) {
		for (int tx = x; tx < x + width; ++tx)
			for (int ty = y; ty < y + height; ++ty)
				setData(tx, ty, data);
		changed = true;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param data
	 */
	public void fillOval(int x, int y, int width, int height, int data) {
		int a = width  / 2 + width  % 2;
		int b = height / 2 + height % 2;
	    int xx = a * 64;
	    int yy = 0;
	    int tx = 0;
	    int ty = 0;

	    x += width  / 2;
	    y += height / 2;

		while (xx >= 0) {
	        tx = xx / 64;
	        ty = yy / 64;
	        setData(x+tx, y+ty, data);
	        setData(x-tx, y-ty, data);
	        setData(x-tx, y+ty, data);
	        setData(x+tx, y-ty, data);
	        yy += xx * b / a / 64;
	        xx -= yy * a / b / 64;
	    }
	    
		changed = true;
	}
}
