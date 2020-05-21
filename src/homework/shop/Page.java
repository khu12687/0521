package homework.shop;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

/*
 * 어플리케이션에서 사용 될 모든 페이지가 가질 공통 속성 및 기능을 정의한 최상위 객체!!
 * 왜만드나? 각 페이지가 보유한 속성과 메서드가 너무 비숫하고 중복되고 있음
 * */
public class Page extends JPanel{
	ShopApp shopApp; //내가 살아가는 윈도우 어버이
	String title;
	Color color;
	int width;
	int height;
	boolean showFlag;
	
	public Page(ShopApp shopApp, String title,Color color,int width, int height,boolean showFlag) {
		this.shopApp =shopApp;
		this.title =title;
		this.color = color;
		this.width = width;
		this.height = height;
		this.showFlag = showFlag;
		
		this.setVisible(this.showFlag);
		this.setBackground(color);
		this.setPreferredSize(new Dimension(width,height));
	}
	
}
