package homework.shop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//하나의 화면은 앞으로 패널로 처리하자!!
public class Login extends Page{
	JPanel loginBox;
	JPanel form; //로그인 폼
	JLabel la_id;
	JLabel la_pw;
	JTextField t_id;
	JPasswordField t_pw;
	JButton bt_regist;
	
	public Login(ShopApp shopApp, String title,Color color,int width, int height,boolean showFlag) {
		super(shopApp,title,color,width, height,showFlag);
		
		loginBox = new JPanel();
		form = new JPanel();
		la_id = new JLabel("ID");
		la_pw = new JLabel("Password");
		t_id = new JTextField();
		t_pw = new JPasswordField();
		bt_regist = new JButton("Login");
		
		//스타일 적용
		loginBox.setBackground(Color.WHITE);
		loginBox.setPreferredSize(new Dimension(250,170));
		loginBox.setPreferredSize(new Dimension(250,100));
		
		//레이아웃변경
		form.setLayout(new GridLayout(2,2));
		
		//조립
		form.add(la_id);
		form.add(t_id);
		form.add(la_pw);
		form.add(t_pw);
		
		loginBox.add(form);
		loginBox.add(bt_regist);
		
		add(loginBox);
		
		//버튼에 리스너 연결
		bt_regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginCheck();
			}
		});
	}
	public void loginCheck() {
		//?물음표 표기법의 역할 : 바인드 변수라한다..
		//데이터베이스 성능향상을 위해 사용..
		String sql ="select * from admin where id=? and pass=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = shopApp.con.prepareStatement(sql);
			pstmt.setString(1, t_id.getText()); //id
			pstmt.setString(2, new String(t_pw.getPassword()));
			
			//System.out.println(t_pw.getText());
			rs=pstmt.executeQuery(); //쿼리수행!!
			if(rs.next()) { //일치하는 데이터 존재함
				shopApp.hasAuth=true;
				JOptionPane.showMessageDialog(this, "인증되었습니다");
				shopApp.m_login.setText("Logout");
			}else { //일치하는 데이터없음
				shopApp.hasAuth=false;
				JOptionPane.showMessageDialog(this, "인증실패");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			shopApp.connectionManger.closeDB(pstmt,rs);
		}
	}
}
