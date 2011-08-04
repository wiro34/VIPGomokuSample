package vip2011.tic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;

/**
 * ボードを画面に描画するためのクラスです。
 * ボードの各マスの値に対する色を {@link #bindColor(int, Color)} メソッドで設定することで、
 * 任意の値に対する色付けを行うことが出来ます。
 * 
 * @author wiro
 */
public class BoardDrawer {
	//-------------------------------------------------------------------------
	// フィールド
	//-------------------------------------------------------------------------
	private Board board;
	private int width, height;
	
	private Map<Integer, Color> colorMap;
	
	private int gridWidth;
	private Color gridColor;
	private int cellSize;
	
	private int curX = -1, curY = -1;
	private Color curColor;
	
	private Color defaultColor = Color.WHITE;

	//-------------------------------------------------------------------------
	// メソッド
	//-------------------------------------------------------------------------
	/**
	 * 指定したマスのサイズと、枠の色と太さで、ボードを描画するためのビューアを新しく作成します。
	 * 
	 * @param board ボード
	 * @param cellSize マスのサイズ
	 * @param gridWidth 枠の太さ
	 * @param gridColor 枠の色
	 */
	BoardDrawer(Board board, int cellSize, int gridWidth, Color gridColor) {
		this.board = board;
		this.cellSize = cellSize;
		this.gridWidth = gridWidth;
		this.gridColor = gridColor;
		
		this.width  = board.getWidth();
		this.height = board.getHeight();
		
		this.colorMap = new HashMap<Integer, Color>();
	}
	
	/**
	 * 指定したボードの値に対応する色を設定します。
	 * 
	 * @param id ボードの値
	 * @param color 色
	 */
	public void bindColor(int id, Color color) {
		colorMap.put(id, color);
	}
	
	/**
	 * ボードを指定したコンテキスト上に描画します。
	 * 
	 * @param g {@link Graphics}
	 * @param x 描画するX座標
	 * @param y 描画するY座標
	 */
	public void draw(Graphics g, int x, int y) {
		draw(g, x, y, 0, 0, width, height);
	}
	
	/**
	 * ボードの指定した範囲を、指定したコンテキスト上に描画します。
	 * ボードの描画する範囲は、(boardX, boardY)を左上として範囲 
	 * 
	 * @param g {@link Graphics}
	 * @param drawX 描画するX座標
	 * @param drawY 描画するY座標
	 * @param boardX ボードの描画する範囲の左上のX座標
	 * @param boardY ボードの描画する範囲の左上のY座標
	 * @param width ボードの描画する範囲の横幅
	 * @param height ボードの描画する範囲の縦幅
	 */
	public void draw(Graphics g, int drawX, int drawY, int boardX, int boardY, int width, int height) {
		int oriX = drawX + gridWidth / 2;
		int oriY = drawY + gridWidth / 2;
		int w = width  * cellSize + gridWidth * (width  + 1);
		int h = height * cellSize + gridWidth * (height + 1);

		// 枠を描画
		g.setColor(gridColor);
		g.fillRect(oriX, oriY, w, h);
		
		// 各セルを描画
		for (int ix = 0; ix < width; ++ix) {
			int x = oriX + ix * cellSize + (ix + 1) * gridWidth;
			for (int iy = 0; iy < height; ++iy) {
				int y = oriY + iy * cellSize + (iy + 1) * gridWidth;
				g.setColor(getColorMap(board.getData(boardX + ix, boardY + iy)));
				g.fillRect(x, y, cellSize, cellSize);
			}
		}
		
		// カーソルを描画
		if (0 <= curX && curX < width && 0 <= curY && curY < height) {
			int cx = curX - boardX;
			int cy = curY - boardY;
			int x = oriX + cx * cellSize + (cx + 1) * gridWidth;
			int y = oriY + cy * cellSize + (cy + 1) * gridWidth;
			g.setColor(curColor);
			g.fillRect(x, y, cellSize, cellSize);
		}
	}
	
	/*
	 * 
	 */
	private Color getColorMap(int id) {
		Color c = colorMap.get(id);
		return (c != null) ? c : defaultColor;
	}
	
	/**
	 * 指定した横幅に対して、ボードをセンタリングするためのX座標を取得します。
	 * 
	 * @param canvasWidth 描画する領域の横幅
	 */
	public int getCenteringX(int canvasWidth) {
		return (canvasWidth  - cellSize * width  - gridWidth * (width  + 1)) / 2;
	}

	/**
	 * 指定した縦幅に対して、ボードをセンタリングするためのY座標を取得します。
	 * 
	 * @param canvasHeight 描画する領域の縦幅
	 */
	public int getCenteringY(int canvasHeight) {
		return (canvasHeight - cellSize * height - gridWidth * (height + 1)) / 2;
	}

	/**
	 * 指定したボードの描画位置とマウスのX座標から、マウスが重なっているマスのX座標を取得します。
	 * 返されるマスのX座標は、ボードの左上のマスを (0,0) 右下のマスを (width-1, height-1) とした値です。
	 * 
	 * @param boardDrawX ボードを描画しているX座標
	 * @param mouseX マウスのキャンバス上でのX座標
	 * @return マウスがボード上にある場合は重なっているマスのX座標、そうでない場合は -1
	 */
	public int getBoardXFromMouseX(int boardDrawX, int mouseX) {
		int x = (mouseX - boardDrawX) / (cellSize + gridWidth);
		return (0 <= x && x < width) ? x : -1;
	}
	
	/**
	 * 指定したボードの描画位置とマウスのY座標から、マウスが重なっているマスのY座標を取得します。
	 * 返されるマスのY座標は、ボードの左上のマスを (0,0) 右下のマスを (width-1, height-1) とした値です。
	 * 
	 * @param boardDrawY ボードを描画しているY座標
	 * @param mouseY マウスのキャンバス上でのY座標
	 * @return マウスがボード上にある場合は重なっているマスのX座標、そうでない場合は -1
	 */
	public int getBoardYFromMouseY(int boardDrawY, int mouseY) {
		int y = (mouseY - boardDrawY) / (cellSize + gridWidth);
		return (0 <= y && y < height) ? y : -1;
	}

	/**
	 * 指定した座標に、指定した色でカーソルを描画します。
	 * カーソルはボードの値を変更することなくマスを塗るための機能です。
	 * カーソルは一時的なもので、別の座標にカーソルを描画すると古いカーソルは削除されます。
	 * また、{@link #removeCursor()} が呼び出すことで削除することもできます。
	 * 
	 * @param curX カーソルのX座標
	 * @param curY カーソルのY座標
	 * @param cursorColorId カーソルの色ID
	 */
	public void setCursor(int curX, int curY, int cursorColorId) {
		if (curX < 0 || curX >= width || curY < 0 || curY >= height)
			throw new IllegalArgumentException("カーソル座標が不正です。");
		this.curX = curX;
		this.curY = curY;
		this.curColor = getColorMap(cursorColorId);
	}

	/**
	 * カーソルを削除します。
	 */
	public void removeCursor() {
		this.curX = this.curY = -1;
	}

	/**
	 * 枠の太さを取得します。
	 * 
	 * @return 枠の太さ
	 */
	public int getGridWidth() {
		return gridWidth;
	}

	/**
	 * 枠の太さを設定します。
	 * 
	 * @param gridWidth 枠の太さ
	 */
	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	/**
	 * 枠の色を取得します。
	 * 
	 * @return 枠の色
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * 枠の色を設定します。
	 * 
	 * @param gridColor 枠の色
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	/**
	 * セルのサイズを取得します。
	 * 
	 * @return セルのサイズ
	 */
	public int getCellSize() {
		return cellSize;
	}

	/**
	 * セルのサイズを設定します。
	 * 
	 * @param cellSize セルのサイズ
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	
	/**
	 * マスの値に対応する色が{@link #bindColor(int, Color)}で
	 * 設定されてない場合に利用される色を取得します。
	 * 
	 * @return デフォルトの色
	 */
	public Color getDefaultColor() {
		return defaultColor;
	}

	/**
	 * マスの値に対応する色が{@link #bindColor(int, Color)}で
	 * 設定されてない場合に利用される色を設定します。
	 * 
	 * @param defaultColor デフォルトの色
	 */
	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}
}
