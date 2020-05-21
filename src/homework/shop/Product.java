package homework.shop;
/*
 * 데이터베이스의 테이블마다 1:1 대응하는 VO를 만드는 패턴을 사용해본다!
 * 데이터베이스 세계에서는 현실의 하나의 사물은 테이블로 디자인하고, 테이블에 들어간 각각의 레코드가 바로 현실에서의
 * 객체이다. 따라서 테이블은 실제 사물이 아니라, 거푸집이다!!
 * 반면 자바세상에서는 현실의 존재하는 하나의 사물을 클래스라는 단위로 표현하되 이 클래스는 실제 사물이 아니라 거푸집이고
 * ,이 거푸집으로 태어난 하나의 인스턴스가 실제 사물이다.
 * 
 *		 			데이터베이스		 자바분야
 * 	현실의사물			Table로 표현		 Class로 표현 
 * 	인스턴스			Table안의 레코드 	 힙에 올라온 객체
 * */
public class Product {
	private int product_id;
	private String name;
	private int price;
	private String brand;
	private String img;
	
	public int getProduct_id() {
		return product_id;
	}
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
