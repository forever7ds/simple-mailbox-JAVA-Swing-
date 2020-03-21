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

//�ʼ�ת������
public class TransPage {
	public String senderAccount = "";	//�������˻�
	public String senderAddress = "";	//�����˵�ַ
	public String senderPassword = "";	//����������
	public String sendname = "";		//�������ǳ�
	JFrame f=null;
	String smtpadd = null;				//��������ַ
	Message tobesent = null;			//Ҫת�����ʼ�
    
	//�ʼ�ת������
	public TransPage(String s1, String s2, String s3, String s4, Message msg) {
		senderAddress = s1;
		senderAccount = s2;
		senderPassword = s3;
		sendname = s4;
		smtpadd = "smtp." + s1.split("@")[1];
		tobesent = msg;
        f = new JFrame("ת������");
        
        //ת������gui
        JLabel sendadd_l = new JLabel("�ռ��˵�ַ:", JLabel.LEFT);
        sendadd_l.setBounds(20, 50, 80, 30);
        
        JButton sendbtn = new JButton("����");
        sendbtn.setBounds(250, 200, 100, 30);
        
        JTextField sendadd = new JTextField();
        sendadd.setBounds(100, 50, 400, 30);        
        
        //������Ͱ�ť����Ӧ
        final TransPage this_f = this;
        sendbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame TransNotice = new NoticeFrame("ת����");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		this_f.TransMsg(sendadd.getText());
		            		JOptionPane.showMessageDialog(f, "ת���ɹ�!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		TransNotice.dispose();
		            		sendadd.setText("");
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "ת��ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		TransNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        //��Ӹ�gui
        f.add(sendadd);
        f.add(sendbtn);
        f.add(sendadd_l);
        f.setSize(600, 400);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
	}
	
    public void TransMsg(String destadd) throws Exception {
    	//����ת������ĸ�Э��
    	Properties props = new Properties();
    	props.setProperty("mail.smtp.host",smtpadd);//�������������
        props.setProperty("mail.smtp.port","465");//���Ͷ˿�
        props.setProperty("mail.smtp.auth","true");//�Ƿ���Ȩ�޿���
        props.setProperty("mail.debug","true");//true ��ӡ��Ϣ������̨
        props.setProperty("mail.transport","smtp");//���͵�Э���Ǽ򵥵��ʼ�����Э��
        props.setProperty("mail.smtp.ssl.enable","true");
        //������������Ӧ�ó�������Ļ�����Ϣ�� Session ����
        Session session = Session.getInstance(props);
        //���õ�����Ϣ�ڿ���̨��ӡ����
        //session.setDebug(true);
        //�����ʼ���ʵ������
        Message msg = (Message) this.getMimeMessage_tran(session, this.tobesent, destadd);
        //����session�����ȡ�ʼ��������Transport
        Transport transport = session.getTransport();
        //���÷����˵��˻���������
        transport.connect(senderAccount, senderPassword);
        //�����ʼ��������͵������ռ��˵�ַ��message.getAllRecipients() ��ȡ�������ڴ����ʼ�����ʱ��ӵ������ռ���, ������, ������
        transport.sendMessage(msg,msg.getAllRecipients());
         
         
        //�ر��ʼ�����
        transport.close();
    }
    
    public MimeMessage getMimeMessage_tran(Session session, Message sentmsg, String recpaddress) throws Exception{
        //����һ���ʼ���ʵ������
        MimeMessage msg = new MimeMessage(session);
        //���÷����˵�ַ
        msg.setFrom(new InternetAddress(senderAddress, sendname, "UTF-8"));
        //�����ռ���
        if(recpaddress.indexOf(",") != -1) {
	    	//���ռ��˵����
        	InternetAddress[] recpaddresses = new InternetAddress().parse(recpaddress);
	    	msg.setRecipients(MimeMessage.RecipientType.TO,recpaddresses);   
	    }
	    else {
	    	//���ռ��˵����
	    	msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recpaddress));
	    }
        //�����ʼ�����
        msg.setSubject(sentmsg.getSubject(),"UTF-8");
        //�����ʼ�����
        msg.setContent(sentmsg.getContent(), sentmsg.getContentType());
        //�����ʼ��ķ���ʱ��,Ĭ����������
        msg.setSentDate(new Date());
        return msg;
    }
}

