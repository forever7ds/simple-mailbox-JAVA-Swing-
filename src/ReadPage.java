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

    public String Address = "";		//�˽���ĵ�ַ
    public String Account = "";		//�˽�����˻�
    public String Password = "";	//�˽��������
    public String SendName = "";	//�˽�����ǳ�
    
    JFrame a = new JFrame("�������");
    
    msganduids m = null;		//�洢��ȡ������Ϣ
    String pop3add = null;		//pop3Э��ķ�������ַ
    
    public ReadPage(String address, String account, String password, String sendname) {
    	//��ʼ����������
    	Address = address;
    	Account = account;
    	Password = password;
    	SendName = sendname;
    	pop3add = "pop." + address.split("@")[1];
    	
    	//��ʾ��¼���
        try {
        	m = this.connectandload();		//����һ��pop3���������message
        	this.Loadmsgs(a, m);			//���س���õ�message
    		JOptionPane.showMessageDialog(a, "��¼�ɹ�!", "��ʾ",JOptionPane.WARNING_MESSAGE);
        }
        catch(Exception f) {
        	JOptionPane.showMessageDialog(a, "��¼ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
        }
        
    }
    
 
    public void delmsgs(String uid) throws Exception{
    	//�����ʼ��������Ĳ�������
    	//�������ñ���
        Properties props = new Properties();
        //���ô���Э��
        props.setProperty("mail.store.protocol", "pop3");
        //���ô���˿�,��ͬ�������Ͳ�ͬ
        String port="";
        //����qq����˿�
        if(pop3add.equals("pop.qq.com")) {
        	port = "995";
        	//����qq�������������
        	MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable",true);
            props.put("mail.pop3.ssl.socketFactory",sf);
            //props.setProperty("mail.debug", "true");
        }
        //��������163����˿�
        else if(pop3add.contentEquals("pop.163.com")) {
        	port = "110";
        }
        props.setProperty("mail.pop3.port", port);
        //�����ռ��˵�POP3������
        props.setProperty("mail.pop3.host", pop3add);
        //������������Ӧ�ó�������Ļ�����Ϣ�� Session ����
        Session session = Session.getInstance(props);
        //���õ�����Ϣ�ڿ���̨��ӡ����
        //session.setDebug(true);
        //��������
        Store store = session.getStore("pop3");
        //�����ռ���POP3������
        store.connect(pop3add, Account, Password);
        //����û����ʼ��˻���ע��ͨ��pop3Э���ȡĳ���ʼ��е�����ֻ��Ϊinbox
        Folder folder = store.getFolder("inbox");
        //���ö��ʼ��˻��ķ���Ȩ��
        folder.open(Folder.READ_WRITE);
        //�õ�UID��Ҫ�ȵõ�pop3��folder
        POP3Folder inbox = (POP3Folder) folder;
        //�õ��ʼ��˻��������ʼ���Ϣ
        Message [] messages_del = folder.getMessages();
        //�õ������ʼ���UID���Ƚ��ҵ���Ҫɾ����UID
        int i;
        for(i=0;i<messages_del.length;i++) {
        	if(inbox.getUID(messages_del[i]).equals(uid)) {
        		System.out.println("�ɹ��ҵ�");
        		break;
        	}
        }
        //ɾ����ӦUID���ʼ�
        messages_del[i].setFlag(Flags.Flag.DELETED, true);
        //�ر����ӱ�������
        folder.close();
        store.close();
        
    }
    
    public msganduids connectandload() throws Exception{
    	//�����ʼ��������Ĳ�������
    	//�������ñ���
        Properties props = new Properties();
        //���ô���Э��
        props.setProperty("mail.store.protocol", "pop3");
        //���ô���˿�,��ͬ�������Ͳ�ͬ
        String port="";
        //����qq����˿�
        if(pop3add.equals("pop.qq.com")) {
        	port = "995";
        	//����qq�������������
        	MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.enable",true);
            props.put("mail.pop3.ssl.socketFactory",sf);
            //props.setProperty("mail.debug", "true");
        }
        //��������163����˿�
        else if(pop3add.contentEquals("pop.163.com")) {
        	port = "110";
        }
        props.setProperty("mail.pop3.port", port);
        //�����ռ��˵�POP3������
        props.setProperty("mail.pop3.host", pop3add);
        //������������Ӧ�ó�������Ļ�����Ϣ�� Session ����
        Session session = Session.getInstance(props);
        //���õ�����Ϣ�ڿ���̨��ӡ����
        //session.setDebug(true);
        //��������
        Store store = session.getStore("pop3");
        //�����ռ���POP3������
        store.connect(pop3add, Account, Password);
        //����û����ʼ��˻���ע��ͨ��pop3Э���ȡĳ���ʼ��е�����ֻ��Ϊinbox
        Folder folder = store.getFolder("inbox");
        //���ö��ʼ��˻��ķ���Ȩ��
        folder.open(Folder.READ_WRITE);
        //�õ�UID��Ҫ�ȵõ�pop3��folder
        POP3Folder inbox = (POP3Folder) folder;
        //�õ������ʼ���Ϣ
        Message [] messages = folder.getMessages();
        //�����洢uid������
        String[] uids = new String[messages.length];
        //�õ������ʼ�UID
        for(int i=0;i<messages.length;i++) {
        	uids[i] = new String(inbox.getUID(messages[i]));
        }
        //���������ʼ���Ϣ��uid��info�ಢ����
        msganduids info = new msganduids(messages, uids);
        return info;
    }
    
    
    public void Loadmsgs(JFrame a, msganduids messages) throws Exception{
    	//��������
    	
    	//�����̶��ı�ǩ
    	JPanel f = new JPanel();
        JLabel Date_l = new JLabel("����");
        Date_l.setBounds(50, 100, 100, 30);
        f.add(Date_l);
        JLabel SendAddress_l = new JLabel("������");
        SendAddress_l.setBounds(270, 100, 100, 30);
        f.add(SendAddress_l);
        JLabel Subject_l = new JLabel("����");
        Subject_l.setBounds(490, 100, 100, 30);
        f.add(Subject_l);
        JLabel Content_l = new JLabel("����");
        Content_l.setBounds(710, 100, 100, 30);
        f.add(Content_l);
        f.setPreferredSize(new Dimension(1400, 700));
        f.setLayout(null);
        a.setSize(1500, 600);
        a.setLocationRelativeTo(null);
        a.setLayout(null);
        
        final JFrame a_f = a;
        final ReadPage this_f = this;
        
        //���Ͻǰ�ť
        JButton sendbtn = new JButton("����");
        sendbtn.setBounds(1250, 20, 100, 60);
        f.add(sendbtn);
        sendbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//�½�����ҳ��
        		new SendPage(Address, Account, Password, SendName);
        	}
        });
        
        JButton logout = new JButton("�˳�");
        logout.setBounds(1350, 20, 100, 60);
        f.add(logout);
        logout.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        
        JButton fresh = new JButton("ˢ��");
        fresh.setBounds(1150, 20, 100, 60);
        f.add(fresh);
        fresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame FreshNotice = new NoticeFrame("ˢ����");
        		a_f.dispose();
            	Thread th = new Thread(){
            		public void run() {
            			//ˢ�²���,����Ϊ���������Ӹ��´洢���ʼ���Ϣ��UID,�ټ���ҳ��
		            	try {
		            		this_f.m = this_f.connectandload();
		            		this_f.Loadmsgs(this_f.a, this_f.m);
		            		FreshNotice.dispose();
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "ˢ��ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		FreshNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        //����ť
        JButton sort_date_new = new JButton("��������");
        sort_date_new.setBounds(50, 20, 200, 30);
        f.add(sort_date_new);
        sort_date_new.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_date_old = new JButton("��������");
        sort_date_old.setBounds(50, 50, 200, 30);
        f.add(sort_date_old);
        sort_date_old.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_add_up = new JButton("�ռ�������");
        sort_add_up.setBounds(270, 20, 200, 30);
        f.add(sort_add_up);
        sort_add_up.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_add_down = new JButton("�ռ��˽���");
        sort_add_down.setBounds(270, 50, 200, 30);
        f.add(sort_add_down);
        sort_add_down.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_sbj_up = new JButton("��������");
        sort_sbj_up.setBounds(490, 20, 200, 30);
        f.add(sort_sbj_up);
        sort_sbj_up.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        JButton sort_sbj_down = new JButton("���⽵��");
        sort_sbj_down.setBounds(490, 50, 200, 30);
        f.add(sort_sbj_down);
        sort_sbj_down.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SortNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		//ð����������ʼ���Ϣ��UID��˳��
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
		            		JOptionPane.showMessageDialog(f, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
		            		SortNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        
        //չʾ���ʼ��б����ݹ���
        JLabel[] dates = new JLabel[messages.msg.length];
        JLabel[] addresses = new JLabel[messages.msg.length];
        JLabel[] subjects = new JLabel[messages.msg.length];
        JLabel[] abstracts = new JLabel[messages.msg.length];
        JButton[] details = new JButton[messages.msg.length];
        JButton[] deletes = new JButton[messages.msg.length];
        JButton[] trans = new JButton[messages.msg.length];
        //ѭ����ȡ�����ʼ�����
        int i;
        for(i = 0 ; i < messages.msg.length ; i++){
        	System.out.println("\n----------------------------------------------\nhere is message"+i+"\n");
            final int i_t = i;
            
            //�������鰴ť
        	details[i] = new JButton("����");
        	details[i].setBounds(1150, 130+i*40, 60, 30);
        	f.add(details[i]);
        	details[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			NoticeFrame LoginNotice = new NoticeFrame("��ȡ��");
                	Thread th = new Thread(){
                		public void run() {
                			try {
                				//��������ҳ��
                				new DetailPage(messages.msg[i_t]);
                				LoginNotice.dispose();
                			}
                			catch(Exception g) {
                				System.out.println("��������ʧ��!");
                			}
                			
                		}
                	};
                	th.setPriority(Thread.MIN_PRIORITY);
                	th.start();
        			
        		}
        	});
        	
        	//����ɾ����ť
        	deletes[i] = new JButton("ɾ��");
        	deletes[i].setBounds(1210, 130+i*40, 60, 30);
        	f.add(deletes[i]);
        	deletes[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
            		NoticeFrame DelNotice = new NoticeFrame("ɾ����");
                	Thread th = new Thread(){
                		public void run() {
    		            	try {
    		            		//ɾ����Ӧ�ʼ�
    		            		this_f.delmsgs(messages.uids[i_t]);
    		            		this_f.m = this_f.connectandload();
    		            		this_f.Loadmsgs(this_f.a, this_f.m);
    		            		JOptionPane.showMessageDialog(f, "ɾ���ɹ�!", "��ʾ",JOptionPane.WARNING_MESSAGE);
    		            		DelNotice.dispose();
    		            	}catch(Exception a) {
    		            		JOptionPane.showMessageDialog(f, "ɾ��ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
    		            		DelNotice.dispose();
    		            	}
                		}
                	};
                	th.setPriority(Thread.MIN_PRIORITY);
                	th.start();
            	}
            	
        	});
        	
        	//����ת����ť
        	trans[i] = new JButton("ת��");
        	trans[i].setBounds(1270, 130+i*40, 60, 30);
        	f.add(trans[i]);
        	trans[i].addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			//�½�ת��ҳ��
        			new TransPage(Address, Account, Password, SendName, messages.msg[i_t]);
        		}
        	});
        	

        	//����ʼ�����
            String subject = messages.msg[i].getSubject();
            System.out.println("����:" + subject);
            subjects[i] = new JLabel(subject);
            subjects[i].setBounds(490, 130+i*40 , 200 , 30);
            subjects[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(subjects[i]);
            
            
            //����ʼ�������
            Address[] from = messages.msg[i].getFrom();
            String fromstr = from[0].toString();
            if (fromstr.startsWith("=?")) {
				// ���ļ�������ɷ���RFC822�淶
				fromstr = MimeUtility.decodeText(fromstr);
			}
            System.out.println("������:" + fromstr);
            addresses[i] = new JLabel(fromstr);
            addresses[i].setBounds(270, 130+i*40 , 200 , 30);
            addresses[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(addresses[i]);
            
            //�������
            Date sentdate = messages.msg[i].getSentDate();
            System.out.println("����:" + sentdate);
            String datestr = Handledatestr(sentdate);
            dates[i] = new JLabel(datestr);
            dates[i].setBounds(50, 130+i*40 , 200 , 30);
            dates[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(dates[i]);
            
            //��ȡ�ʼ����ݣ������ʼ����ݵ�html����)
            String abstr = "";
            if(!(messages.msg[i].isMimeType("multipart/*"))){	//�������Ϊһ���
            	if(messages.msg[i].isMimeType("text/plain")) {
            		//��������ı�
	            	abstr = (String) messages.msg[i].getContent();
	            	System.out.println("���ݲ��0:" + abstr);
            	}
            	else if(messages.msg[i].isMimeType("text/html")) {
            		//���������html������ҳ�鿴
            		abstr = "����html��ʽ���ı�,��������鿴!";
	            	System.out.println("���ݲ��0:" + abstr);
            	}
            	
            }
            else {		//�������Ϊ����
            	Multipart layer1 = (Multipart) messages.msg[i].getContent();
            	if (layer1.getBodyPart(0).isMimeType("multipart/*")) {
            		Multipart layer2 = (Multipart) layer1.getBodyPart(0).getContent();
            		BodyPart layer2text = layer2.getBodyPart(0);
            		if(layer2text.isMimeType("text/plain")) {
            			//��������ı�
    	            	abstr = (String) layer2text.getContent();
    	            	System.out.println("���ݲ��2:" + abstr);
                	}
            		else if(layer2text.isMimeType("text/html")) {
            			//�����html����������ҳ�鿴
    	            	abstr = "����html��ʽ���ı�,��������鿴!";
    	            	System.out.println("���ݲ��2:" + abstr);
                	}
            	}
            	else {
            		BodyPart layer1text = layer1.getBodyPart(0);
            		if(layer1text.isMimeType("text/plain")) {
            			//��������ı�
            			abstr = (String) layer1text.getContent();
    	            	System.out.println("���ݲ��1:" + abstr);
            		}
            		else if(layer1text.isMimeType("text/html")) {
            			//�����html����������ҳ�鿴
            			abstr = "����html��ʽ���ı�,��������鿴!";
    	            	System.out.println("���ݲ��1:" + abstr);
            		}
            	}
            }
            //չʾ��ȡ����������ΪժҪ
            abstracts[i] = new JLabel(abstr);
            abstracts[i].setBounds(710, 130+i*40, 400, 30);
            abstracts[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            f.add(abstracts[i]);
        }
        
        //����������ҹؼ��ʵ��ı���
        JTextField indexstr = new JTextField();
        indexstr.setBounds(710, 50, 400, 30);
        f.add(indexstr);
        
        //�������ڲ���
        JButton IndexDate_btn = new JButton("���ڲ���");
        IndexDate_btn.setBounds(710, 20, 100, 30);
        f.add(IndexDate_btn);
        IndexDate_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//��������洢���ϲ���������message����Ϣ��UID
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
	                		//ʹ�ô洢�Ĳ�����Ϣ���³�ʼ��ҳ��
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        //���巢���˲���
        JButton IndexSender_btn = new JButton("�����˲���");
        IndexSender_btn.setBounds(810, 20, 100, 30);
        f.add(IndexSender_btn);
        IndexSender_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//��������洢���ϲ���������message����Ϣ��UID
	            			ArrayList<Message> signmsgs = new ArrayList<Message>();
	                		ArrayList<String> signuids = new ArrayList<String>();
	                		for(int i=0;i<this_f.m.msg.length;i++) {
	                			String fromadd = this_f.m.msg[i].getFrom()[0].toString();
	                			if (fromadd.startsWith("=?")) {
	                				// ���ļ�������ɷ���RFC822�淶
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
	                		//ʹ�ô洢�Ĳ��ҵ�����Ϣ��ʼ��ҳ��
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        	
        });
        
        //�����������
        JButton IndexSubject_btn = new JButton("�������");
        IndexSubject_btn.setBounds(910, 20, 100, 30);
        f.add(IndexSubject_btn);
        IndexSubject_btn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame IndexNotice = new NoticeFrame("������");
            	Thread th = new Thread(){
            		public void run() {
            			try {
            				//��������洢���ϲ���������message����Ϣ��UID
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
	                		//ʹ�ô洢�ı�������ҳ��
	                		msganduids indexresult = new msganduids(showns, shownuids);
        	        		this_f.Loadmsgs(this_f.a,indexresult);
        	        		IndexNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
                			IndexNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}

        });
        
        //������Һ�Ļ�ԭ����
        JButton recoverbtn = new JButton("��ԭ");
        recoverbtn.setBounds(1010, 20, 100, 30);
        f.add(recoverbtn);
        recoverbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame RecoverNotice = new NoticeFrame("��ԭ��");
            	Thread th = new Thread(){
            		public void run() {
            			//ʹ�ñ�Page�洢��ԭʼ��Ϣ�ٴμ���ҳ��
                		try {
        	        		this_f.Loadmsgs(this_f.a,this_f.m);
        	        		RecoverNotice.dispose();
                		}
                		catch(Exception r) {
                			JOptionPane.showMessageDialog(this_f.a, "��ԭʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
                			RecoverNotice.dispose();
                		}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
        
        
        //�����ʼ���Ŀ���ô��ڴ�С
        f.setPreferredSize(new Dimension(1400, 150+i*40));
        f.setVisible(true);
        //���ù�����
        JScrollPane sp = new JScrollPane(f);
        sp.setVisible(true);
        //�����ʼ�ҳ��Ϊframe����ҳ��
        a.setContentPane(sp);
        a.setDefaultCloseOperation(EXIT_ON_CLOSE);
        a.setVisible(true);
    }
    
    
    //�������ڶ����Ϊ�ַ���
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
        //����ʱ��
        return year+'.'+month+'.'+day+' '+time;
    }
}

//�洢��ȡ�����ʼ�����Ϣ����,����uid��message��������
class msganduids{
	Message[] msg = null;
	String[] uids = null;
	public msganduids (Message[] msg, String[] uids) {
		this.msg = msg;
		this.uids = uids;
	}
}
