import java.awt.event.*;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//邮件转发界面
public class TransPage {
	public String senderAccount = "";	//发送人账户
	public String senderAddress = "";	//发送人地址
	public String senderPassword = "";	//发送人密码
	public String sendname = "";		//发送人昵称
	JFrame f=null;
	String smtpadd = null;				//服务器地址
	Message tobesent = null;			//要转发的邮件
    
	//邮件转发函数
	public TransPage(String s1, String s2, String s3, String s4, Message msg) {
		senderAddress = s1;
		senderAccount = s2;
		senderPassword = s3;
		sendname = s4;
		smtpadd = "smtp." + s1.split("@")[1];
		tobesent = msg;
        f = new JFrame("转发界面");
        
        //转发界面gui
        JLabel sendadd_l = new JLabel("收件人地址:", JLabel.LEFT);
        sendadd_l.setBounds(20, 50, 80, 30);
        
        JButton sendbtn = new JButton("发送");
        sendbtn.setBounds(250, 200, 100, 30);
        
        JTextField sendadd = new JTextField();
        sendadd.setBounds(100, 50, 400, 30);        
        
        //点击发送按钮的响应
        final TransPage this_f = this;
        sendbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame TransNotice = new NoticeFrame("转发中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		this_f.TransMsg(sendadd.getText());
		            		JOptionPane.showMessageDialog(f, "转发成功!", "提示",JOptionPane.WARNING_MESSAGE);
		            		TransNotice.dispose();
		            		sendadd.setText("");
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "转发失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		TransNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        //添加各gui
        f.add(sendadd);
        f.add(sendbtn);
        f.add(sendadd_l);
        f.setSize(600, 400);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
	}
	
    public void TransMsg(String destadd) throws Exception {
    	//设置转发所需的各协议
    	Properties props = new Properties();
    	props.setProperty("mail.smtp.host",smtpadd);//发送邮箱服务器
        props.setProperty("mail.smtp.port","465");//发送端口
        props.setProperty("mail.smtp.auth","true");//是否开启权限控制
        props.setProperty("mail.debug","true");//true 打印信息到控制台
        props.setProperty("mail.transport","smtp");//发送的协议是简单的邮件传输协议
        props.setProperty("mail.smtp.ssl.enable","true");
        //创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props);
        //设置调试信息在控制台打印出来
        //session.setDebug(true);
        //创建邮件的实例对象
        Message msg = (Message) this.getMimeMessage_tran(session, this.tobesent, destadd);
        //根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());
         
         
        //关闭邮件连接
        transport.close();
    }
    
    public MimeMessage getMimeMessage_tran(Session session, Message sentmsg, String recpaddress) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress, sendname, "UTF-8"));
        //设置收件人
        if(recpaddress.indexOf(",") != -1) {
	    	//多收件人的情况
        	InternetAddress[] recpaddresses = new InternetAddress().parse(recpaddress);
	    	msg.setRecipients(MimeMessage.RecipientType.TO,recpaddresses);   
	    }
	    else {
	    	//单收件人的情况
	    	msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recpaddress));
	    }
        //设置邮件主题
        msg.setSubject(sentmsg.getSubject(),"UTF-8");
        //设置邮件正文
        msg.setContent(sentmsg.getContent(), sentmsg.getContentType());
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }
}

