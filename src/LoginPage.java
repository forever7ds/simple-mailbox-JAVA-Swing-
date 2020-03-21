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
		//改变主题frame
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {
			;
		}*/
		
		//读取本地文件存储的账号信息并解析,来初始化addresses等属性
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
		
        f = new JFrame("MailManager登录");
        
        //标识
        JLabel Address_l = new JLabel("账号:");
        Address_l.setBounds(20, 20, 80, 30);
        JLabel Password_l = new JLabel("密码:");
        Password_l.setBounds(20, 70, 80, 30);
        JLabel SendName_l = new JLabel("用户昵称:");
        SendName_l.setBounds(20, 120, 80, 30);

        //密码输入框
        JPasswordField Password = new JPasswordField();
        Password.setBounds(100, 70, 100, 30);
        Password.setText(passwords[0]);
        //带选择的地址输入框
        final JComboBox<String> Address = new JComboBox<String>(this.addresses);
        Address.setBounds(100, 20, 100, 30);
        Address.setEditable(true);
        Address.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("选中: " + Address.getSelectedIndex() + " = " + Address.getSelectedItem());
                    Password.setText(passwords[Address.getSelectedIndex()]);
                }
            }
        });
        Address.setSelectedIndex(0);		//默认选择
        //带选择的昵称输入框
        final JComboBox<String> SendName = new JComboBox<String>(this.sendnames);
        SendName.setBounds(100, 120, 100, 30);
        SendName.setEditable(true);
        SendName.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("选中: " + SendName.getSelectedIndex() + " = " + SendName.getSelectedItem());
                }
            }
        });
        SendName.setSelectedIndex(0);		//默认选择
                
        //登录按钮
        JButton Login_btn = new JButton("登录");
        Login_btn.setBounds(100, 200, 100, 30);
        Login_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	//初始化连接并创建邮箱界面所需要的变量
            	address = Address.getSelectedItem().toString();
            	account = Address.getSelectedItem().toString().split("@")[0];
            	password = new String(Password.getPassword());
            	sendname = SendName.getSelectedItem().toString();
            	//连接服务器并创建邮箱界面
            	NoticeFrame LoginNotice = new NoticeFrame("登录中");
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

//一个在登录或发送等界面弹出的Loading加载界面类
class NoticeFrame extends JFrame {
	private JLabel ImgIcon = new JLabel();
	private Thread th;
	private ImageIcon LoadingImage = null;
	private String Category="";
	public NoticeFrame(String category) {
		this.Category = category;
		this.init();
		final NoticeFrame NF = this;
		//找到资源相对路径
		String path = System.getProperty("user.dir") + "\\Images\\LoadingImage.gif";
		System.out.println(path);
		this.LoadingImage =  new ImageIcon(path);
		th = new Thread() {
			public void run() {
				//设定显示的图片
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
		//初始化frame的各项参数
		this.setTitle(this.Category);
		this.setSize(300, 250);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}


