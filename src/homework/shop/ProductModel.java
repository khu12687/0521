package homework.shop;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import homework.lib.Formatter;

/*
 * 디자인에 불과한 JTable과 여기에 보여질 데이터는 분리시켜야, 추후 JTable에 다른 데이터를 교체할때 편하다..
 * 즉 유지보수성을 높이기 위해 JTable은 MVC패턴을 반영한 TableModel이라는 컨트롤러를 제공한다!!
 * 
 * */
public class ProductModel extends AbstractTableModel{
	
	//이차원배열을 사용할수도 있으나, 배열의 단점이 많기 때문에 이를 극복한 컬렉션 프레임웤의 3가지 유형 중 순서가 있는 List를 이용해본다!!
	//ArrayList<Product> 자체가 자료형이다...
	//한국말로 풀어서 말하면 그냥 리스트가 아니라 Product전용 리스트형
	ArrayList<Product> list = new ArrayList<Product>();
	String[] columnNames = {
		"상품코드","상품명","가격","브랜드","상품이미지"
	};
	@Override
	public int getRowCount() {
		
		return list.size(); //행의 갯수 즉 레코드 갯수
	}
	@Override
	public int getColumnCount() {
		
		return 5; //컬럼의 수 우리의 경우 5개
	}
	
	//이 메서드들은 모두 JTable이 자신의 내용을 채워넣기 위해 호출한다
	//아래의 메서드는 총5회 호출하며, 이때 매개변수 col은 0,1,2,3,4....
	@Override
	public String getColumnName(int col) {
		
		return columnNames[col];
	}
	@Override
	public Object getValueAt(int row, int col) {
		//기존의 일차원 배열 대신 Product
		Product product = list.get(row);
		String data=null;
		if(col==0) {
			data=Integer.toString(product.getProduct_id());
		}else if(col==1) {
			data=product.getName();
		}else if(col==2) {
			//String형인 data를 통화표시로 변환해보자!! 3자리마다 쉼표로구분..출력할때만 처리하면된다..
			//data=Integer.toString(product.getPrice());
			//규칙적인 패턴을 이용하면 원하는 형식의 문자열을 만드는 객체
			data = Formatter.getCurrency(product.getPrice());
			
		}else if(col==3) {
			data=product.getBrand();
		}else if(col==4) {
			data=product.getImg();
		}
		return data;
			
	}
	//각셀을 수정할 수 있게 할지 여부를 반환하는 메서드
	public boolean isCellEditable(int row, int col){
		boolean flag=true;
		if(col==0 || col==4) { //0번째 열은 primary key영역이므로 수정불가능하게
			flag =false;
		}
		return flag;
	}
	
	//Jtable의 각셀에 값을 list에서 가져오는 메서드가 getValueAt()이라면
	//사용자가 수정한 데이터를 list 다시 바녕하는 메서드는 setValueAt()
	//value 정체? 사용자가 JTable의 셀을 편집하고 엔터치는 순간
	//해당 데이터의 좌표는 row,col, 값은 value로 전달되어 옴!!
	public void setValueAt(Object value, int row, int col){
		//주의 배열이 아닌 List를 이용하고 있다
		//그리고 List안에 들어있는 레코드를 표현한 객체는 일차원 배열대신 VO를 이용하고있다
		//0번째 - VO의 product_id, 1번쨰 = VO의 name...
		Product product = list.get(row);
		if(col==1) { //name
			product.setName(value.toString());
		}else if(col==2) { //price
			product.setPrice(Integer.parseInt(value.toString()));
			
		}else if(col==3) { //brand
			product.setBrand(value.toString());
		}
		//db에 넣기 숙제!
	}
}
