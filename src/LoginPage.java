import javax.mail.Message;
import javax.swing.*;
import java.awt.event.*;


public class LoginPage extends JFrame{
	
	JFrame f = null;
	String address="";
	String account="";
	String password="";
	String sendname="";

	String[] addresses = null;
	String[] passwords = null;
	String[] sendnames = null;
	public LoginPage(){
		//�ı�����frame
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			;
		}*/
		
		//��ȡ�����ļ��洢���˺���Ϣ������,����ʼ��addresses������
		QuickLogin LoginData = new QuickLogin();
		String[] Datas = LoginData.getAllData().split("\n");
		addresses = new String[Datas.length];
		passwords = new String[Datas.length];
		sendnames = new String[Datas.length];
		for(int i=0;i<Datas.length;i++) {
			addresses[i] = new String(Datas[i].split(" ")[0]);
			System.out.println(addresses[i]);
			passwords[i] = new String(Datas[i].split(" ")[1]);
			System.out.println(passwords[i]);
			sendnames[i] = new String(Datas[i].split(" ")[2]);
			System.out.println(sendnames[i]);
		}
		
        f = new JFrame("MailManager��¼");
        
        //��ʶ
        JLabel Address_l = new JLabel("�˺�:");
        Address_l.setBounds(20, 20, 80, 30);
        JLabel Password_l = new JLabel("����:");
        Password_l.setBounds(20, 70, 80, 30);
        JLabel SendName_l = new JLabel("�û��ǳ�:");
        SendName_l.setBounds(20, 120, 80, 30);

        //���������
        JPasswordField Password = new JPasswordField();
        Password.setBounds(100, 70, 100, 30);
        Password.setText(passwords[0]);
        //��ѡ��ĵ�ַ�����
        final JComboBox<String> Address = new JComboBox<String>(this.addresses);
        Address.setBounds(100, 20, 100, 30);
        Address.setEditable(true);
        Address.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // ֻ����ѡ�е�״̬
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("ѡ��: " + Address.getSelectedIndex() + " = " + Address.getSelectedItem());
                    Password.setText(passwords[Address.getSelectedIndex()]);
                }
            }
        });
        Address.setSelectedIndex(0);		//Ĭ��ѡ��
        //��ѡ����ǳ������
        final JComboBox<String> SendName = new JComboBox<String>(this.sendnames);
        SendName.setBounds(100, 120, 100, 30);
        SendName.setEditable(true);
        SendName.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // ֻ����ѡ�е�״̬
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("ѡ��: " + SendName.getSelectedIndex() + " = " + SendName.getSelectedItem());
                }
            }
        });
        SendName.setSelectedIndex(0);		//Ĭ��ѡ��
                
        //��¼��ť
        JButton Login_btn = new JButton("��¼");
        Login_btn.setBounds(100, 200, 100, 30);
        Login_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	//��ʼ�����Ӳ����������������Ҫ�ı���
            	address = Address.getSelectedItem().toString();
            	account = Address.getSelectedItem().toString().split("@")[0];
            	password = new String(Password.getPassword());
            	sendname = SendName.getSelectedItem().toString();
            	//���ӷ������������������
            	NoticeFrame LoginNotice = new NoticeFrame("��¼��");
            	Thread th = new Thread(){
            		public void run() {
            			new ReadPage(address, account, password, sendname);
            			LoginNotice.dispose();
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
            	f.dispose();		
            }
        });
        
        f.add(Password);
        f.add(SendName);
        f.add(Address);
        f.add(Password_l);
        f.add(Address_l);
        f.add(SendName_l);
        f.add(Login_btn);
        f.setSize(300, 300);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }
}

//һ���ڵ�¼���͵Ƚ��浯����Loading���ؽ�����
class NoticeFrame extends JFrame {
	private JLabel ImgIcon = new JLabel();
	private Thread th;
	private ImageIcon LoadingImage = null;
	private String Category="";
	public NoticeFrame(String category) {
		this.Category = category;
		this.init();
		final NoticeFrame NF = this;
		//�ҵ���Դ���·��
		String path = System.getProperty("user.dir") + "\\Images\\LoadingImage.gif";
		System.out.println(path);
		this.LoadingImage =  new ImageIcon(path);
		th = new Thread() {
			public void run() {
				//�趨��ʾ��ͼƬ
				NF.ImgIcon.setIcon(LoadingImage);
				NF.ImgIcon.setBounds(0, 0, 300, 200);
				NF.ImgIcon.setHorizontalAlignment(JLabel.CENTER);
				NF.ImgIcon.setVerticalAlignment(JLabel.CENTER);
				NF.add(NF.ImgIcon);
				NF.setVisible(true);
			}
		};
		try {
			th.setPriority(Thread.MAX_PRIORITY);
			th.start();
		}catch(Exception e) {
			;
		}
	}
	private void init() {
		//��ʼ��frame�ĸ������
		this.setTitle(this.Category);
		this.setSize(300, 250);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}


