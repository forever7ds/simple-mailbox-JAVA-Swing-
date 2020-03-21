import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.math.*;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import javax.swing.border.*;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.util.MailSSLSocketFactory;

import java.awt.*;
import java.awt.event.*;

 
public class ReadPage extends JFrame{

    public String Address = "";		//此界面的地址
    public String Account = "";		//此界面的账户
    public String Password = "";	//此界面的密码
    public String SendName = "";	//此界面的昵称
    
    JFrame a = new JFrame("浏览界面");
    
    msganduids m = null;		//存储读取到的信息
    String pop3add = null;		//pop3协议的服务器地址
    
    public ReadPage(String address, String account, String password, String sendname) {
    	//初始化各个变量
    	Address = address;
    	Account = account;
    	Password = password;
    	SendName = sendname;
    	pop3add = "pop." + address.split("@")[1];
    	
    	//显示登录结果
        try {
        	m = this.connectandload();		//连接一次pop3服务器获得message
        	this.Loadmsgs(a, m);			//加载出获得的message
    		JOptionPane.showMessageDialog(a, "登录成功!", "提示",JOptionPane.WARNING_MESSAGE);
        }
        catch(Exception f) {
        	JOptionPane.showMessageDialog(a, "登录失败!", "提示",JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
 
    public void delmsgs(String uid) throws Exception{
    	//连接邮件服务器的参数配置
    	//创建配置变量
        Properties props = new Properties();
        //设置传输协议
        props.setProperty("mail.store.protocol", "pop3");
        //设置传输端口,不同邮箱类型不同
        String port="";
        //设置qq邮箱端口
        if(pop3add.equals("pop.qq.com")) {
        	port = "995";
        	//配置qq邮箱的连接条件
        	MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable",true);
            props.put("mail.pop3.ssl.socketFactory",sf);
            //props.setProperty("mail.debug", "true");
        }
        //设置网易163邮箱端口
        else if(pop3add.contentEquals("pop.163.com")) {
        	port = "110";
        }
        props.setProperty("mail.pop3.port", port);
        //设置收件人的POP3服务器
        props.setProperty("mail.pop3.host", pop3add);
        //创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        //session.setDebug(true);
        //创建连接
        Store store = session.getStore("pop3");
        //连接收件人POP3服务器
        store.connect(pop3add, Account, Password);
        //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
        Folder folder = store.getFolder("inbox");
        //设置对邮件账户的访问权限
        folder.open(Folder.READ_WRITE);
        //得到UID需要先得到pop3的folder
        POP3Folder inbox = (POP3Folder) folder;
        //得到邮件账户的所有邮件信息
        Message [] messages_del = folder.getMessages();
        //得到所有邮件的UID并比较找到需要删除的UID
        int i;
        for(i=0;i<messages_del.length;i++) {
        	if(inbox.getUID(messages_del[i]).equals(uid)) {
        		System.out.println("成功找到");
        		break;
        	}
        }
        //删除对应UID的邮件
        messages_del[i].setFlag(Flags.Flag.DELETED, true);
        //关闭连接保留更改
        folder.close();
        store.close();
        
    }
    
    public msganduids connectandload() throws Exception{
    	//连接邮件服务器的参数配置
    	//创建配置变量
        Properties props = new Properties();
        //设置传输协议
        props.setProperty("mail.store.protocol", "pop3");
        //设置传输端口,不同邮箱类型不同
        String port="";
        //设置qq邮箱端口
        if(pop3add.equals("pop.qq.com")) {
        	port = "995";
        	//配置qq邮箱的连接条件
        	MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable",true);
            props.put("mail.pop3.ssl.socketFactory",sf);
            //props.setProperty("mail.debug", "true");
        }
        //设置网易163邮箱端口
        else if(pop3add.contentEquals("pop.163.com")) {
        	port = "110";
        }
        props.setProperty("mail.pop3.port", port);
        //设置收件人的POP3服务器
        props.setProperty("mail.pop3.host", pop3add);
        //创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        //session.setDebug(true);
        //创建连接
        Store store = session.getStore("pop3");
        //连接收件人POP3服务器
        store.connect(pop3add, Account, Password);
        //获得用户的邮件账户，注意通过pop3协议获取某个邮件夹的名称只能为inbox
        Folder folder = store.getFolder("inbox");
        //设置对邮件账户的访问权限
        folder.open(Folder.READ_WRITE);
        //得到UID需要先得到pop3的folder
        POP3Folder inbox = (POP3Folder) folder;
        //得到所有邮件信息
        Message [] messages = folder.getMessages();
        //创建存储uid的数组
        String[] uids = new String[messages.length];
        //得到所有邮件UID
        for(int i=0;i<messages.length;i++) {
        	uids[i] = new String(inbox.getUID(messages[i]));
        }
        //创建包含邮件信息和uid的info类并返回
        msganduids info = new msganduids(messages, uids);
        return info;
    }
    
    
    public void Loadmsgs(JFrame a, msganduids messages) throws Exception{
    	//构建主题
    	
    	//创建固定的标签
    	JPanel f = new JPanel();
        JLabel Date_l = new JLabel("日期");
        Date_l.setBounds(50, 100, 100, 30);
        f.add(Date_l);
        JLabel SendAddress_l = new JLabel("发送人");
        SendAddress_l.setBounds(270, 100, 100, 30);
        f.add(SendAddress_l);
        JLabel Subject_l = new JLabel("主题");
        Subject_l.setBounds(490, 100, 100, 30);
        f.add(Subject_l);
        JLabel Content_l = new JLabel("内容");
        Content_l.setBounds(710, 100, 100, 30);
        f.add(Content_l);
        f.setPreferredSize(new Dimension(1400, 700));
        f.setLayout(null);
        a.setSize(1500, 600);
        a.setLocationRelativeTo(null);
        a.setLayout(null);
        
        final JFrame a_f = a;
        final ReadPage this_f = this;
        
        //右上角按钮
        JButton sendbtn = new JButton("发送");
        sendbtn.setBounds(1250, 20, 100, 60);
        f.add(sendbtn);
        sendbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//新建发送页面
        		new SendPage(Address, Account, Password, SendName);
        	}
        });
        
        JButton logout = new JButton("退出");
        logout.setBounds(1350, 20, 100, 60);
        f.add(logout);
        logout.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        JButton fresh = new JButton("刷新");
        fresh.setBounds(1150, 20, 100, 60);
        f.add(fresh);
        fresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame FreshNotice = new NoticeFrame("刷新中");
        		a_f.dispose();
            	Thread th = new Thread(){
            		public void run() {
            			//刷新操作,具体为先重新连接更新存储的邮件信息和UID,再加载页面
		            	try {
		            		this_f.m = this_f.connectandload();
		            		this_f.Loadmsgs(this_f.a, this_f.m);
		            		FreshNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "刷新失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		FreshNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        //排序按钮
        JButton sort_date_new = new JButton("最新优先");
        sort_date_new.setBounds(50, 20, 200, 30);
        f.add(sort_date_new);
        sort_date_new.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getSentDate().before(messages.msg[j].getSentDate())) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_date_old = new JButton("最早优先");
        sort_date_old.setBounds(50, 50, 200, 30);
        f.add(sort_date_old);
        sort_date_old.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getSentDate().after(messages.msg[j].getSentDate())) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_add_up = new JButton("收件人升序");
        sort_add_up.setBounds(270, 20, 200, 30);
        f.add(sort_add_up);
        sort_add_up.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getFrom()[0].toString().compareTo(messages.msg[j].getFrom()[0].toString()) > 0) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_add_down = new JButton("收件人降序");
        sort_add_down.setBounds(270, 50, 200, 30);
        f.add(sort_add_down);
        sort_add_down.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getFrom()[0].toString().compareTo(messages.msg[j].getFrom()[0].toString()) < 0) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_sbj_up = new JButton("主题升序");
        sort_sbj_up.setBounds(490, 20, 200, 30);
        f.add(sort_sbj_up);
        sort_sbj_up.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getSubject().compareTo(messages.msg[j].getSubject()) > 0) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_sbj_down = new JButton("主题降序");
        sort_sbj_down.setBounds(490, 50, 200, 30);
        f.add(sort_sbj_down);
        sort_sbj_down.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("排序中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//冒泡排序更改邮件信息和UID的顺序
		            		for(int i=0;i<messages.msg.length-1;i++) {
			        			for(int j=i+1;j<messages.msg.length;j++) {
			        					if(messages.msg[i].getSubject().compareTo(messages.msg[j].getSubject()) < 0) {
			        						Message temp = messages.msg[i];
			        						String tempuid = messages.uids[i];
			        						messages.msg[i] = messages.msg[j];
			        						messages.uids[i] = messages.uids[j];
			        						messages.msg[j] = temp;
			        						messages.uids[i] = tempuid;
			        					}
			        			}
			        		}
		            		a_f.dispose();
		            		this_f.Loadmsgs(this_f.a, messages);
		            		SortNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "排序失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        
        //展示的邮件列表内容构建
        JLabel[] dates = new JLabel[messages.msg.length];
        JLabel[] addresses = new JLabel[messages.msg.length];
        JLabel[] subjects = new JLabel[messages.msg.length];
        JLabel[] abstracts = new JLabel[messages.msg.length];
        JButton[] details = new JButton[messages.msg.length];
        JButton[] deletes = new JButton[messages.msg.length];
        JButton[] trans = new JButton[messages.msg.length];
        //循环提取所有邮件详情
        int i;
        for(i = 0 ; i < messages.msg.length ; i++){
        	System.out.println("\n----------------------------------------------\nhere is message"+i+"\n");
            final int i_t = i;
            
            //定义详情按钮
        	details[i] = new JButton("详情");
        	details[i].setBounds(1150, 130+i*40, 60, 30);
        	f.add(details[i]);
        	details[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			NoticeFrame LoginNotice = new NoticeFrame("读取中");
                	Thread th = new Thread(){
                		public void run() {
                			try {
                				//创建详情页面
                				new DetailPage(messages.msg[i_t]);
                				LoginNotice.dispose();
                			}
                			catch(Exception g) {
                				System.out.println("创建详情失败!");
                			}
                			
                		}
                	};
                	th.setPriority(Thread.MIN_PRIORITY);
                	th.start();
        			
        		}
        	});
        	
        	//定义删除按钮
        	deletes[i] = new JButton("删除");
        	deletes[i].setBounds(1210, 130+i*40, 60, 30);
        	f.add(deletes[i]);
        	deletes[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
            		NoticeFrame DelNotice = new NoticeFrame("删除中");
                	Thread th = new Thread(){
                		public void run() {
    		            	try {
    		            		//删除对应邮件
    		            		this_f.delmsgs(messages.uids[i_t]);
    		            		this_f.m = this_f.connectandload();
    		            		this_f.Loadmsgs(this_f.a, this_f.m);
    		            		JOptionPane.showMessageDialog(f, "删除成功!", "提示",JOptionPane.WARNING_MESSAGE);
    		            		DelNotice.dispose();
    		            	}catch(Exception a) {
    		            		JOptionPane.showMessageDialog(f, "删除失败!", "提示",JOptionPane.WARNING_MESSAGE);
    		            		DelNotice.dispose();
    		            	}
                		}
                	};
                	th.setPriority(Thread.MIN_PRIORITY);
                	th.start();
            	}
            	
        	});
        	
        	//定义转发按钮
        	trans[i] = new JButton("转发");
        	trans[i].setBounds(1270, 130+i*40, 60, 30);
        	f.add(trans[i]);
        	trans[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			//新建转发页面
        			new TransPage(Address, Account, Password, SendName, messages.msg[i_t]);
        		}
        	});
        	

        	//获得邮件主题
            String subject = messages.msg[i].getSubject();
            System.out.println("主题:" + subject);
            subjects[i] = new JLabel(subject);
            subjects[i].setBounds(490, 130+i*40 , 200 , 30);
            subjects[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(subjects[i]);
            
            
            //获得邮件发件人
            Address[] from = messages.msg[i].getFrom();
            String fromstr = from[0].toString();
            if (fromstr.startsWith("=?")) {
				// 把文件名编码成符合RFC822规范
				fromstr = MimeUtility.decodeText(fromstr);
			}
            System.out.println("发件人:" + fromstr);
            addresses[i] = new JLabel(fromstr);
            addresses[i].setBounds(270, 130+i*40 , 200 , 30);
            addresses[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(addresses[i]);
            
            //获得日期
            Date sentdate = messages.msg[i].getSentDate();
            System.out.println("日期:" + sentdate);
            String datestr = Handledatestr(sentdate);
            dates[i] = new JLabel(datestr);
            dates[i].setBounds(50, 130+i*40 , 200 , 30);
            dates[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(dates[i]);
            
            //获取邮件内容（包含邮件内容的html代码)
            String abstr = "";
            if(!(messages.msg[i].isMimeType("multipart/*"))){	//解析层次为一层的
            	if(messages.msg[i].isMimeType("text/plain")) {
            		//获得内容文本
	            	abstr = (String) messages.msg[i].getContent();
	            	System.out.println("内容层次0:" + abstr);
            	}
            	else if(messages.msg[i].isMimeType("text/html")) {
            		//如果内容是html在详情页查看
            		abstr = "这是html格式的文本,请点击详情查看!";
	            	System.out.println("内容层次0:" + abstr);
            	}
            	
            }
            else {		//解析层次为多层的
            	Multipart layer1 = (Multipart) messages.msg[i].getContent();
            	if (layer1.getBodyPart(0).isMimeType("multipart/*")) {
            		Multipart layer2 = (Multipart) layer1.getBodyPart(0).getContent();
            		BodyPart layer2text = layer2.getBodyPart(0);
            		if(layer2text.isMimeType("text/plain")) {
            			//获得内容文本
    	            	abstr = (String) layer2text.getContent();
    	            	System.out.println("内容层次2:" + abstr);
                	}
            		else if(layer2text.isMimeType("text/html")) {
            			//如果是html内容在详情页查看
    	            	abstr = "这是html格式的文本,请点击详情查看!";
    	            	System.out.println("内容层次2:" + abstr);
                	}
            	}
            	else {
            		BodyPart layer1text = layer1.getBodyPart(0);
            		if(layer1text.isMimeType("text/plain")) {
            			//获得内容文本
            			abstr = (String) layer1text.getContent();
    	            	System.out.println("内容层次1:" + abstr);
            		}
            		else if(layer1text.isMimeType("text/html")) {
            			//如果是html内容在详情页查看
            			abstr = "这是html格式的文本,请点击详情查看!";
    	            	System.out.println("内容层次1:" + abstr);
            		}
            	}
            }
            //展示提取到的内容作为摘要
            abstracts[i] = new JLabel(abstr);
            abstracts[i].setBounds(710, 130+i*40, 400, 30);
            abstracts[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(abstracts[i]);
        }
        
        //定义输入查找关键词的文本框
        JTextField indexstr = new JTextField();
        indexstr.setBounds(710, 50, 400, 30);
        f.add(indexstr);
        
        //定义日期查找
        JButton IndexDate_btn = new JButton("日期查找");
        IndexDate_btn.setBounds(710, 20, 100, 30);
        f.add(IndexDate_btn);
        IndexDate_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("查找中");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//定义变量存储符合查找条件的message的信息和UID
	            			ArrayList<Message> signmsgs = new ArrayList<Message>();
	                		ArrayList<String> signuids = new ArrayList<String>();
	                		for(int i=0;i<this_f.m.msg.length;i++) {
	                			if(Handledatestr(this_f.m.msg[i].getSentDate()).indexOf(indexstr.getText())!=-1) {
	                				signmsgs.add(this_f.m.msg[i]);
	                				signuids.add(this_f.m.uids[i]);
	                			}
	                		}
	                		Message[] showns = new Message[signmsgs.size()];
	                		String[] shownuids = new String[signuids.size()];
	                		for(int i=0;i<showns.length;i++) {
	                			showns[i] = signmsgs.get(i);
	                			shownuids[i] = signuids.get(i);
	                		}
	                		//使用存储的查找信息重新初始化页面
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "查找失败!", "提示",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        //定义发件人查找
        JButton IndexSender_btn = new JButton("发件人查找");
        IndexSender_btn.setBounds(810, 20, 100, 30);
        f.add(IndexSender_btn);
        IndexSender_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("查找中");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//定义变量存储符合查找条件的message的信息和UID
	            			ArrayList<Message> signmsgs = new ArrayList<Message>();
	                		ArrayList<String> signuids = new ArrayList<String>();
	                		for(int i=0;i<this_f.m.msg.length;i++) {
	                			String fromadd = this_f.m.msg[i].getFrom()[0].toString();
	                			if (fromadd.startsWith("=?")) {
	                				// 把文件名编码成符合RFC822规范
	                				fromadd = MimeUtility.decodeText(fromadd);
	                			}
	                			if(fromadd.indexOf(indexstr.getText())!=-1) {
	                				signmsgs.add(this_f.m.msg[i]);
	                				signuids.add(this_f.m.uids[i]);
	                			}
	                		}
	                		Message[] showns = new Message[signmsgs.size()];
	                		String[] shownuids = new String[signuids.size()];
	                		for(int i=0;i<showns.length;i++) {
	                			showns[i] = signmsgs.get(i);
	                			shownuids[i] = signuids.get(i);
	                		}
	                		//使用存储的查找到的信息初始化页面
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "查找失败!", "提示",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        //定义主题查找
        JButton IndexSubject_btn = new JButton("主题查找");
        IndexSubject_btn.setBounds(910, 20, 100, 30);
        f.add(IndexSubject_btn);
        IndexSubject_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("查找中");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//定义变量存储符合查找条件的message的信息和UID
	            			ArrayList<Message> signmsgs = new ArrayList<Message>();
	                		ArrayList<String> signuids = new ArrayList<String>();
	                		for(int i=0;i<this_f.m.msg.length;i++) {
	                			if(this_f.m.msg[i].getSubject().indexOf(indexstr.getText())!=-1) {
	                				signmsgs.add(this_f.m.msg[i]);
	                				signuids.add(this_f.m.uids[i]);
	                			}
	                		}
	                		Message[] showns = new Message[signmsgs.size()];
	                		String[] shownuids = new String[signuids.size()];
	                		for(int i=0;i<showns.length;i++) {
	                			showns[i] = signmsgs.get(i);
	                			shownuids[i] = signuids.get(i);
	                		}
	                		//使用存储的变量加载页面
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "查找失败!", "提示",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}

        });
        
        //定义查找后的还原功能
        JButton recoverbtn = new JButton("还原");
        recoverbtn.setBounds(1010, 20, 100, 30);
        f.add(recoverbtn);
        recoverbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame RecoverNotice = new NoticeFrame("还原中");
            	Thread th = new Thread(){
            		public void run() {
            			//使用本Page存储的原始信息再次加载页面
                		try {
        	        		this_f.Loadmsgs(this_f.a,this_f.m);
        	        		RecoverNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "还原失败!", "提示",JOptionPane.WARNING_MESSAGE);
                			RecoverNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        
        //根据邮件数目设置窗口大小
        f.setPreferredSize(new Dimension(1400, 150+i*40));
        f.setVisible(true);
        //设置滚动条
        JScrollPane sp = new JScrollPane(f);
        sp.setVisible(true);
        //设置邮件页面为frame的主页面
        a.setContentPane(sp);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        a.setVisible(true);
    }
    
    
    //处理日期对象成为字符串
    public static String Handledatestr(Date date){
        String m = date.toString().split(" ")[1];
        String month = "";
    	if (m.equals("Jan")){
    		month= "1";
    	}else if (m.equals("Feb")) {
    		month= "2";
    	}else if (m.equals("Mar")) {
    		month= "3";
    	}else if (m.equals("Apr")) {
    		month= "4";
    	}else if (m.equals("May")) {
    		month= "5";
    	}else if (m.equals("Jun")) {
    		month= "6";
    	}else if (m.equals("Jul")) {
    		month= "7";
    	}else if (m.equals("Aug")) {
    		month= "8";
    	}else if (m.equals("Sep")) {
    		month= "9";
    	}else if (m.equals("Oct")) {
    		month= "10";
    	}else if (m.equals("Nov")) {
    		month= "11";
    	}else if (m.equals("Dec")) {
    		month= "12";
    	}
        String day = date.toString().split(" ")[2];
        String time = date.toString().split(" ")[3];
        String year = date.toString().split(" ")[5];
        //处理时间
        return year+'.'+month+'.'+day+' '+time;
    }
}

//存储读取到的邮件的信息的类,包括uid和message两个数组
class msganduids{
	Message[] msg = null;
	String[] uids = null;
	public msganduids (Message[] msg, String[] uids) {
		this.msg = msg;
		this.uids = uids;
	}
}
