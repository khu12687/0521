package homework.shop;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import homework.lib.Formatter;

/*
 * �����ο� �Ұ��� JTable�� ���⿡ ������ �����ʹ� �и����Ѿ�, ���� JTable�� �ٸ� �����͸� ��ü�Ҷ� ���ϴ�..
 * �� ������������ ���̱� ���� JTable�� MVC������ �ݿ��� TableModel�̶�� ��Ʈ�ѷ��� �����Ѵ�!!
 * 
 * */
public class ProductModel extends AbstractTableModel{
	
	//�������迭�� ����Ҽ��� ������, �迭�� ������ ���� ������ �̸� �غ��� �÷��� �����ӟp�� 3���� ���� �� ������ �ִ� List�� �̿��غ���!!
	//ArrayList<Product> ��ü�� �ڷ����̴�...
	//�ѱ����� Ǯ� ���ϸ� �׳� ����Ʈ�� �ƴ϶� Product���� ����Ʈ��
	ArrayList<Product> list = new ArrayList<Product>();
	String[] columnNames = {
		"��ǰ�ڵ�","��ǰ��","����","�귣��","��ǰ�̹���"
	};
	@Override
	public int getRowCount() {
		
		return list.size(); //���� ���� �� ���ڵ� ����
	}
	@Override
	public int getColumnCount() {
		
		return 5; //�÷��� �� �츮�� ��� 5��
	}
	
	//�� �޼������ ��� JTable�� �ڽ��� ������ ä���ֱ� ���� ȣ���Ѵ�
	//�Ʒ��� �޼���� ��5ȸ ȣ���ϸ�, �̶� �Ű����� col�� 0,1,2,3,4....
	@Override
	public String getColumnName(int col) {
		
		return columnNames[col];
	}
	@Override
	public Object getValueAt(int row, int col) {
		//������ ������ �迭 ��� Product
		Product product = list.get(row);
		String data=null;
		if(col==0) {
			data=Integer.toString(product.getProduct_id());
		}else if(col==1) {
			data=product.getName();
		}else if(col==2) {
			//String���� data�� ��ȭǥ�÷� ��ȯ�غ���!! 3�ڸ����� ��ǥ�α���..����Ҷ��� ó���ϸ�ȴ�..
			//data=Integer.toString(product.getPrice());
			//��Ģ���� ������ �̿��ϸ� ���ϴ� ������ ���ڿ��� ����� ��ü
			data = Formatter.getCurrency(product.getPrice());
			
		}else if(col==3) {
			data=product.getBrand();
		}else if(col==4) {
			data=product.getImg();
		}
		return data;
			
	}
	//������ ������ �� �ְ� ���� ���θ� ��ȯ�ϴ� �޼���
	public boolean isCellEditable(int row, int col){
		boolean flag=true;
		if(col==0 || col==4) { //0��° ���� primary key�����̹Ƿ� �����Ұ����ϰ�
			flag =false;
		}
		return flag;
	}
	
	//Jtable�� ������ ���� list���� �������� �޼��尡 getValueAt()�̶��
	//����ڰ� ������ �����͸� list �ٽ� �ٳ��ϴ� �޼���� setValueAt()
	//value ��ü? ����ڰ� JTable�� ���� �����ϰ� ����ġ�� ����
	//�ش� �������� ��ǥ�� row,col, ���� value�� ���޵Ǿ� ��!!
	public void setValueAt(Object value, int row, int col){
		//���� �迭�� �ƴ� List�� �̿��ϰ� �ִ�
		//�׸��� List�ȿ� ����ִ� ���ڵ带 ǥ���� ��ü�� ������ �迭��� VO�� �̿��ϰ��ִ�
		//0��° - VO�� product_id, 1���� = VO�� name...
		Product product = list.get(row);
		if(col==1) { //name
			product.setName(value.toString());
		}else if(col==2) { //price
			product.setPrice(Integer.parseInt(value.toString()));
			
		}else if(col==3) { //brand
			product.setBrand(value.toString());
		}
		//db�� �ֱ� ����!
	}
}
