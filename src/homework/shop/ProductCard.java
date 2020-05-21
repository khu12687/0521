package homework.shop;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

//상품카드 하나를 표현하는 클래스!!
public class ProductCard extends JPanel{
	JPanel p_canvas;
	JLabel la_name;
	JLabel la_price;
	JLabel la_brand;
	
	Product product; //한건의 레코드를 담을수 있는 로직없는 객체를 가리켜 VO ,js에서는 JSON이 VO역할...
	
	public ProductCard(Product product) {
		this.product = product;
		
		//디자인시작
		p_canvas = new JPanel() {
			
		};
		
		la_name = new JLabel("상품명 : "+product.getName());
		la_price = new JLabel("가격 : "+product.getPrice());
		la_brand = new JLabel("브랜드 : "+product.getBrand());
		
		//스타일적용
		//현재 클래스인 카드의 크기!!
		this.setPreferredSize(new Dimension(130,175));
		this.setBackground(Color.YELLOW);
		
		p_canvas.setPreferredSize(new Dimension(110,70));
		p_canvas.setBackground(Color.CYAN);
		
		la_name.setPreferredSize(new Dimension(120,30));
		la_price.setPreferredSize(new Dimension(120,30));
		la_brand.setPreferredSize(new Dimension(120,30));
		
		//조립
		this.add(p_canvas);
		this.add(la_name);
		this.add(la_price);
		this.add(la_brand);
	}
}
