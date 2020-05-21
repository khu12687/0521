package homework.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import homework.lib.FileManager;
import homework.lib.Formatter;

//하나의 화면은 앞으로 패널로 처리하자!!
public class ProductManager extends Page{
	//-------------서쪽관련------------------
	JPanel p_west; //서쪽에 붙여질 컨테이너
	JTextField t_name;
	JTextField t_price;
	JTextField t_brand;
	JButton bt_find; //이미지 찾기 버튼
	JPanel p_thumb; //등록시 보여질 미리보기 썸네일
	JButton bt_regist; //등록버튼
	JButton bt_list; //목록버튼
	JButton bt_del; //삭제버튼
	
	//--------------샌터영역------------------
	JPanel p_content; //샌터영역에 들어갈 모든 컴포넌트의 어버이
	JTable table;
	JScrollPane scroll;
	JPanel p_detailBox; //상세보기 컨테이너
	JPanel p_detail; //등록후 보여질 상세보기
	JPanel p_info; //이름, 가격, 브랜드 나올 영역
	JLabel la_name;
	JLabel la_price;
	JLabel la_brand;
	
	JFileChooser chooser;
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image thumbImg; //썸네일 이미지
	Image detailImg; //상세 이미지
	
	FileInputStream fis; //파일을 대상으로 한 입력스트림
	FileOutputStream fos; //파일을 대상으로 한 출력스트림
	File thumbFile; //썸네일 처리를 위한 파일객체
	String copyName; //새롭게 부여된 파일명!!
	ProductModel productModel;
	int product_id; //현재 사용자가 보고있는 레코드의 식별값
					//이값이 0이면, 유저는 제품을 선택한적이 없는거다!!
	String delFile;
	public ProductManager(ShopApp shopApp, String title,Color color,int width, int height,boolean showFlag) {
		super(shopApp,title,color,width, height, showFlag);
		
		p_west = new JPanel();
		t_name = new JTextField(10);
		t_price = new JTextField(10);
		t_brand = new JTextField(10);
		
		bt_find = new JButton("이미지");
		p_thumb = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(thumbImg, 0, 0, 120,120,ProductManager.this);
			}
		};
		bt_regist = new JButton("등록");
		bt_list =new JButton("목록");
		bt_del =new JButton("삭제");
		p_content = new JPanel();
		table = new JTable();
		scroll = new JScrollPane(table);
		
		p_detailBox = new JPanel();
		p_detail = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(detailImg, 0, 0,ProductManager.this);
			}
		};
		p_info = new JPanel();
		la_name = new JLabel("제품명 : 티셔츠");
		la_price = new JLabel("가격 : 80000");
		la_brand = new JLabel("브랜드 : ㅁㄴㅇ");
		
		chooser = new JFileChooser("C:/images");
		table.setModel(productModel=new ProductModel());
		
		//스타일 적용
		p_content.setBackground(Color.PINK);
		p_west.setPreferredSize(new Dimension(120,height));
		
		p_thumb.setBackground(Color.WHITE);
		p_thumb.setPreferredSize(new Dimension(120,120));
		p_detail.setBackground(Color.BLACK);
		p_detail.setPreferredSize(new Dimension(180,180));
		
		la_name.setPreferredSize(new Dimension(350,50));
		la_price.setPreferredSize(new Dimension(350,50));
		la_brand.setPreferredSize(new Dimension(350,50));
		
		la_name.setFont(new Font("돋음",Font.BOLD,14));
		la_price.setFont(new Font("돋음",Font.BOLD,14));
		la_brand.setFont(new Font("돋음",Font.BOLD,14));
		
		//보더 레이아웃 적용
		this.setLayout(new BorderLayout());
		p_content.setLayout(new BorderLayout());
		p_detailBox.setLayout(new BorderLayout());
		
		//조립
		add(p_content,BorderLayout.CENTER);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(t_brand);
		p_west.add(bt_find);
		p_west.add(p_thumb);
		p_west.add(bt_regist);
		p_west.add(bt_list);
		p_west.add(bt_del);
		
		
		add(p_west, BorderLayout.WEST);
		p_content.add(scroll);
		
		//상세보기 영역 조립
		p_info.add(la_name);
		p_info.add(la_price);
		p_info.add(la_brand);
		
		p_detailBox.add(p_detail,BorderLayout.WEST);
		p_detailBox.add(p_info);
		
		p_content.add(p_detailBox,BorderLayout.SOUTH);
		
		//버튼과 리스너 연결
		bt_find.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findImg();
			}
		});
		
		bt_regist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regist();
			}
		});
		
		bt_list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getList();
			}
		});
		
		bt_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int result = JOptionPane.showConfirmDialog(ProductManager.this,"삭제하시겠습니까?");
				if(result ==JOptionPane.OK_OPTION) {					
					if(deleteFile()) { //파일이 삭제가되면..
						delete(); //db삭제
					}
				}
			}
		});
		
		//테이블과 리스너 연결
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//상세보기!!
				select();
			}
		});
	}
	
	//이미지 탐색기 열고, 선택한 이미지 그리기!!
	public void findImg() {
		int result = chooser.showOpenDialog(shopApp);
		
		//유저가 파일선택 후 ok를 누르면..
		if(result ==JFileChooser.APPROVE_OPTION) {
			thumbFile = chooser.getSelectedFile();
			//System.out.println(file.getAbsolutePath());
			
			//선택한 이미지 경로를 이용한 그림 그리기!!
			thumbImg = kit.getImage(thumbFile.getAbsolutePath());
			p_thumb.repaint();
		}
	}
	
	//데이터베이스에 넣기 및 이미지 복사!!
	public void regist() {
		//복사먼저 시도하고, 성공하면 db에 넣자!!
		if(copy()) {//복사가 성공하면 db에 넣자!!
			
			String name = t_name.getText();
			int price = Integer.parseInt(t_price.getText());
			String brand = t_brand.getText();
			
			String sql ="insert into product(product_id, name, price, brand, img)";
				   sql+=" values(seq_product.nextval,'"+name+"',"+price+",'"+brand+"','"+copyName+"')";
			//쿼리문 수행객체인 PreparedStatement 생성!!
			//인터페이스라서 new하여 생성 불가!! 해결책??
			//접속이 되어야 퀴리도 날리므로, Connection으로 부터 간접적으로 인스턴스를 얻어오면 된다!!
		    PreparedStatement pstmt =null;
			
			try {
				//쿼리수행 메서드 호출!! DML excutueUpdate()호출
				pstmt = shopApp.con.prepareStatement(sql);
				int result = pstmt.executeUpdate(); //실행결과 이 쿼리문에 의해 영향을 받은 레코드 갯수를 반환해줌
				if(result ==0) { //에러가 아니라 단지 insert가 안된거임
					JOptionPane.showMessageDialog(this, "등록실패");
				}else {
					JOptionPane.showMessageDialog(this, "등록성공");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				shopApp.connectionManger.closeDB(pstmt);
			}
		}
	}
	
	//복사 성공여부를 반환하는 메서드!!
	public boolean copy() {
		boolean result =false;
		try {
			fis = new FileInputStream(thumbFile);
			//복사될 이미지는 사용자가 올린 이미지명을 사용하지 않고 개발자의
			//규칙을 적용하여 새롭게 생성해야 함!!
			long time=System.currentTimeMillis();
			//System.out.println(time);

			//확장자 구하기 
			String ext=FileManager.getExt(thumbFile.getAbsolutePath());
			System.out.println(time+"."+ext);
			copyName=time+"."+ext;
			fos = new FileOutputStream("C:/web_appDB/oracle_workspace/project0518/data/"+copyName);//복사시작!!
			byte[] buff=new byte[1024];//퍼마실 그릇용량 1kbyte
			int data=-1;//데이터의 끝을 알려주는 변수 

			while(true) {
				try {
					data=fis.read(buff);//데이터입력!! read()할때마다 1024개
					if(data==-1)break;//파일의 끝을 만나면 반복문 탈출!!
					fos.write(buff);//출력, write()할때마다 1024개씩!!
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(this, "복사완료");
			result=true; //복사성공
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result=false; //복사실패
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	//목록가져오기
	public void getList() {
		String sql="select * from product order by product_id desc";
		
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		
		try {
			pstmt=shopApp.con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			//커서를 내려가면서 VO에 레코드를 담자!
			//List를 생성하여 아래의 VO들을 차곡차곡 넣어두자!!
			//그래야 TableModel 에서 배열처럼 사용하니깐!!
			ArrayList productList = new ArrayList();
			
			while(rs.next()){
				Product product = new Product();
				//배열의 인덱스가 아니라, 변수명을 이용하므로 훨씬 직관적!!
				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getInt("price"));
				product.setBrand(rs.getString("brand"));
				product.setImg(rs.getString("img"));
				
				productList.add(product); //완성된 VO를 리스트에 누적! as 자바스크립트에 push
			}
			System.out.println("담겨진 총 상품의 수는 "+productList.size());
			
			//생성된 리스트를 TableModel에게 전달하자!!
			productModel.list=productList;
			//JTable 새로고침
			table.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			shopApp.connectionManger.closeDB(pstmt,rs);
		}
	}
	
	//레코드 한건 가져오기!!
	public void select() {
		int row = table.getSelectedRow();
		
		//자바에서는 기본자료형과 이 기본자료형의 객체인 Wrapper클래스간 자동형변환을 지원해주는데,
		//이러한 현상을 가리켜 
		//Boxing : 기본자료형 감싸서 객체화시킨다
		//UnBoxing : 객체자료형을 기본자료형으로 변환
		//Integer x = new Integer(3);
		//int y=x;
		product_id=Integer.parseInt((String)table.getValueAt(row,0));//(x,0)번째에 있다...
		String sql="select * from product where product_id="+product_id;
		System.out.println(sql);
		
		PreparedStatement pstmt = null; //쿼리수행 객체
		ResultSet rs = null; //레코드를 담을 객체
		
		try {
			pstmt = shopApp.con.prepareStatement(sql);
			rs=pstmt.executeQuery(); //select문인 경우 excuteQuery()
			
			//레코드 한건은, 자바분야에서는 하나의 인스턴스로 받으면 된다!!
			Product product = new Product();
			
			//rs의 데이터를 자바인스턴스에 넣기!!
			if(rs.next()) { //레코드가있을떄만
				product.setProduct_id(rs.getInt("product_id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getInt("price"));
				product.setBrand(rs.getString("brand"));
				product.setImg(rs.getString("img"));
			}
			//모두 담았으므로, Product Vo 이용하여 화면에 출력!!
			//상세이미지 repaint(), 제품명,가격,브랜드라벨에 값 채우기!!
			detailImg=kit.getImage("C:/web_appDB/oracle_workspace/project0518/data/"+product.getImg());
			detailImg=detailImg.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH);
			p_detail.repaint();
			
			//제품명,가격,브랜드 라벨
			la_name.setText("제품명 : "+product.getName());
			la_price.setText("가격 : "+Formatter.getCurrency(product.getPrice()));
			la_brand.setText("브랜드 : "+product.getBrand());
			
			//선택시 현재 보고있는 이미지정보를 맴버변수로 저장!!
			delFile = product.getImg();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			shopApp.connectionManger.closeDB(pstmt,rs);
		}
		
	}
	
	public void delete() {
		//선택한 상품이 있는지 여부 체크(유효성 체크)
		if(product_id==0) {
			JOptionPane.showMessageDialog(this,"삭제하실 제품을 선택해주세요");
			return; //실행부의 진행을 막자!!, 아래코드로 못내려가게..
		}
		int result = JOptionPane.showConfirmDialog(this,"삭제하시겠습니까?");
		if(result ==JOptionPane.OK_OPTION) {
			
			String sql = "delete from product where product_id="+product_id;
			System.out.println(sql);
			
			PreparedStatement pstmt = null;
			try {
				pstmt = shopApp.con.prepareStatement(sql);
				
				//row반환값은 이 쿼리에 의해 영향을 받은 레코드 수를 반환
				int row = pstmt.executeUpdate();
				if(row==0) {
					JOptionPane.showMessageDialog(this, "삭제실패");
				}else {
					JOptionPane.showMessageDialog(this, "삭제성공");
					getList(); //다시 테이블 갱신!!
				}
				product_id=0;
				//이미지도 삭제!!
				
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				shopApp.connectionManger.closeDB(pstmt);
			}
		}
	}
	
	//이미지 삭제
	public boolean deleteFile() {
		//삭제하고싶은 파일에 대한 파일객체를 생성해야 한다!!
		File file = new File("C:/web_appDB/oracle_workspace/project0518/data/"+delFile);
		return file.delete();
	}
}
