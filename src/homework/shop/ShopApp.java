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
	JPanel p_content; //�������� �ٰԵ� ������ ����!!
	JButton m_product; //��ǰ���� 
	JButton m_main; //����ڰ� ���Ե� ���θ� ����ȭ��
	JButton m_login; //������ ���� ȭ��
	JButton m_chat; //�����ڿ� 1:1 ä��ȭ��
	
	Page[] pages = new Page[4]; //����������ŭ �迭Ȯ��
	ConnectionManger connectionManger;
	Connection con; //�гε� �� ���������� ��� ������ �� �ֵ��� �θ� �����̳��� �����쿡 �����س���!!
	boolean hasAuth=false; //�α����� ��� true, ���Ѱ�� false
	public ShopApp() {
		p_north = new JPanel();
		p_content = new JPanel();
		m_product = new JButton("��ǰ����");
		m_main = new JButton("���θ�����");
		m_login = new JButton("�α���");
		m_chat = new JButton("1:1ä��");
		
		//������ �����ϱ�
		pages[0] = new ProductManager(this,"��ǰ����",Color.YELLOW,700,500,false);
		pages[1] = new ShoppingMain(this,"���θ�����",Color.BLUE,700,500,true);
		pages[2] = new Login(this,"�α���",Color.RED,700,500,false);
		pages[3] = new Chatting(this,"1:1ä��",Color.GREEN,700,500,false);
		
		//���Ӱ����� ����
		connectionManger = new ConnectionManger();
		
		//��Ÿ�Ϻο�
		p_north.setBackground(Color.BLACK);

		//����
		p_north.add(m_product);
		p_north.add(m_main);
		p_north.add(m_login);
		p_north.add(m_chat);
		
		add(p_north, BorderLayout.NORTH);
		//��� �������� �����ӿ� �ٴ°� �ƴ϶�, Ŀ���������� �پ���ϹǷ�...
		add(p_content);
		p_content.add(pages[0]);
		p_content.add(pages[1]);
		p_content.add(pages[2]);
		p_content.add(pages[3]);
		
		pack(); //������ ���빰���� �ɱ׶���!!
		//setSize(700,600);
		setVisible(true);
		
		//�ӽ������� ������
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//ȭ���߾�����
		setLocationRelativeTo(null);
		
		//��ư�� ������ ����
		m_product.addActionListener(this);
		m_main.addActionListener(this);
		m_login.addActionListener(this);
		m_chat.addActionListener(this);
		
		//����Ŭ ����!!
		con = connectionManger.getConnection();
		if(con==null) {
			JOptionPane.showMessageDialog(this, "�����ͺ��̽� ���ӽ���");
		}else {
			//JOptionPane.showMessageDialog(this, "�����ͺ��̽� ���Ӽ���");
		}
		
		//��������
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				connectionManger.closeDB(con); //DB��������
				System.exit(0); //���μ��� ����
			}
		});
		
		//������ ���ԵǴ� ����ȭ��!!
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
			//ShoppingMain Ŭ������ selectAll() �޼��� ȣ������!!
			//�� ������ �̹� �����ͺ��̽����� ������ �Ϸ�� ���Ķ� ����!!
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
		//�α������� ���� ��� ó��!!
		if(!hasAuth &&page==0) {
			JOptionPane.showMessageDialog(this, "�α����� �ʿ��� �����Դϴ�");
			return;
		}
		
		//hasAuth�� ���� �α����� �����̸鼭, ���ϴ� �������� 2�ϰ��
		//�α׾ƿ���ư�� ���� ����̴�!! �̰�� �α׾ƿ� ó��������Ѵ�!!
		if(hasAuth && page ==2) {
			int ans = JOptionPane.showConfirmDialog(this, "�α׾ƿ��Ͻðڽ��ϱ�?");
			if(ans==JOptionPane.OK_OPTION) { //�α׾ƿ� ���ϴ� ���..
				hasAuth=false;
				m_login.setText("Login");
				JOptionPane.showMessageDialog(this, "�α׾ƿ� �Ǿ����ϴ�");
			}
			return;
		}
		this.setTitle(pages[page].title); //�������� ������ ����� �������� ��ü
		
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
