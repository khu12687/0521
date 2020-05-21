package homework.shop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ShopApp extends JFrame implements ActionListener{
	JPanel p_north;
	JPanel p_content; //페이지가 붙게될 컨텐츠 영역!!
	JButton m_product; //상품관리 
	JButton m_main; //사용자가 보게될 쇼핑몰 메인화면
	JButton m_login; //관라자 인증 화면
	JButton m_chat; //관리자와 1:1 채팅화면
	
	Page[] pages = new Page[4]; //페이지수만큼 배열확보
	ConnectionManger connectionManger;
	Connection con; //패널들 즉 페이지들이 모두 접근할 수 있도록 부모 컨테이너인 윈도우에 보유해놓자!!
	boolean hasAuth=false; //로그인한 경우 true, 안한경우 false
	public ShopApp() {
		p_north = new JPanel();
		p_content = new JPanel();
		m_product = new JButton("상품관리");
		m_main = new JButton("쇼핑몰메인");
		m_login = new JButton("로그인");
		m_chat = new JButton("1:1채팅");
		
		//페이지 구성하기
		pages[0] = new ProductManager(this,"상품관리",Color.YELLOW,700,500,false);
		pages[1] = new ShoppingMain(this,"쇼핑몰메인",Color.BLUE,700,500,true);
		pages[2] = new Login(this,"로그인",Color.RED,700,500,false);
		pages[3] = new Chatting(this,"1:1채팅",Color.GREEN,700,500,false);
		
		//접속관리자 생성
		connectionManger = new ConnectionManger();
		
		//스타일부여
		p_north.setBackground(Color.BLACK);

		//조립
		p_north.add(m_product);
		p_north.add(m_main);
		p_north.add(m_login);
		p_north.add(m_chat);
		
		add(p_north, BorderLayout.NORTH);
		//모든 페이지가 프레임에 붙는게 아니라, 커넨츠영역에 붙어야하므로...
		add(p_content);
		p_content.add(pages[0]);
		p_content.add(pages[1]);
		p_content.add(pages[2]);
		p_content.add(pages[3]);
		
		pack(); //보유한 내용물까지 쪼그라든다!!
		//setSize(700,600);
		setVisible(true);
		
		//임시적으로 쓸거임
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//화면중앙으로
		setLocationRelativeTo(null);
		
		//버튼과 리스너 연결
		m_product.addActionListener(this);
		m_main.addActionListener(this);
		m_login.addActionListener(this);
		m_chat.addActionListener(this);
		
		//오라클 접속!!
		con = connectionManger.getConnection();
		if(con==null) {
			JOptionPane.showMessageDialog(this, "데이터베이스 접속실패");
		}else {
			//JOptionPane.showMessageDialog(this, "데이터베이스 접속성공");
		}
		
		//접속해제
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				connectionManger.closeDB(con); //DB접속해제
				System.exit(0); //프로세스 종료
			}
		});
		
		//유저가 보게되는 쇼핑화면!!
		showPage(1);
		ShoppingMain main = (ShoppingMain)pages[1];
		main.removeChild();
		main.selectAll();
		main.updateUI();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj==m_product) {
			//product O main X login X chat X
			showPage(0);
			
		}else if(obj==m_main) {
			//product X main O login X chat X
			showPage(1);
			//ShoppingMain 클래스의 selectAll() 메서드 호출하자!!
			//이 시점엔 이미 데이터베이스와의 접속이 완료된 이후라서 안전!!
			ShoppingMain main = (ShoppingMain)pages[1];
			main.removeChild();
			main.selectAll();
		}else if(obj==m_login) {
			//product X main X login O chat X
			showPage(2);
		}else if(obj==m_chat) {
			//product X main X login X chat O
			showPage(3);
		}
		
	}
	
	public void showPage(int page) {
		//로그인하지 않은 경우 처리!!
		if(!hasAuth &&page==0) {
			JOptionPane.showMessageDialog(this, "로그인이 필요한 서비스입니다");
			return;
		}
		
		//hasAuth가 현재 로그인한 상태이면서, 원하는 페이지가 2일경우
		//로그아웃버튼을 누른 사람이다!! 이경우 로그아웃 처리해줘야한다!!
		if(hasAuth && page ==2) {
			int ans = JOptionPane.showConfirmDialog(this, "로그아웃하시겠습니까?");
			if(ans==JOptionPane.OK_OPTION) { //로그아웃 원하는 사람..
				hasAuth=false;
				m_login.setText("Login");
				JOptionPane.showMessageDialog(this, "로그아웃 되었습니다");
			}
			return;
		}
		this.setTitle(pages[page].title); //윈도우의 제목을 페널의 제목으로 교체
		
		for(int i=0; i<pages.length; i++) {
			if(i==page) {
				pages[i].setVisible(true);
			}else {
				pages[i].setVisible(false);
			}
		}
	}
	
	public static void main(String[] args) {
		new ShopApp();
	}
}
