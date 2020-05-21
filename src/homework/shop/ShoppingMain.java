package homework.shop;

import java.awt.Color;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//하나의 화면은 앞으로 패널로 처리하자!!
public class ShoppingMain extends Page{
	
	public ShoppingMain(ShopApp shopApp, String title,Color color,int width, int height,boolean showFlag) {
		super(shopApp,title,color,width, height, showFlag);
		
		createProduct();
	}
	
	//데이터베이스에 있는 모든 상품 정보 가져오기!!
	public void selectAll() {
		String sql="select * from product order by product_id desc";
		
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		try {
			pstmt=shopApp.con.prepareStatement(sql);
			rs=pstmt.executeQuery(); //결과 담기
			
			while(rs.next()) {
				Product product = new Product();
				
				//rs에서 produc 옮겨심자!!
				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getInt("price"));
				product.setBrand(rs.getString("brand"));
				product.setImg(rs.getString("img"));
				
				ProductCard card = new ProductCard(product);
				//현재 내가 패널이므로, 카드패널을 내 패널에 붙이자!!
				this.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			shopApp.connectionManger.closeDB(pstmt,rs);
		}
	}
	
	//현재 페널에 자식컴포넌트들을 모두 지우기!!
	//js 에서의 removeChild()
	public void removeChild() {
		//자식 요소 구하기!!
		Component[] childList = this.getComponents();
		
		for(int i=0; i<childList.length; i++) {
			this.remove(childList[i]);
		}
	}
	
	//연습용메서드
	public void createProduct() {
		//임의로 5개만 생성해보기!!
		for(int i=0;i<3; i++) {
			Product product = new Product();
			product.setName("청바지");
			product.setPrice(50000);
			product.setBrand("agm");
			ProductCard card = new ProductCard(product);
			
			//현재 내가 패널이므로, 카드패널을 내 패널에 붙이자!!
			this.add(card);
		}
	}
}
