import java.util.Date;
import java.util.Properties;
import java.util.ArrayList;
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
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import javax.swing.border.*;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.util.MailSSLSocketFactory;

import java.awt.*;
import java.awt.event.*;


public class DetailPage extends JFrame{
	Message objmsg = null;
	JFrame dtlpage = null;
	Date date = null;
	String sendadd = null;
	String subject = null;
	
	ArrayList<BodyPart> bodylist = new ArrayList<BodyPart>();	//����������bodypart������
	String content = "";
	
	public DetailPage(Message msg) throws Exception{
		this.objmsg = msg;
		dtlpage = new JFrame("�ʼ�����");
		//��ȡmessage�ĸ�������
		//����
		this.subject = msg.getSubject();
		//����
		this.date = msg.getSentDate();
		//������
		this.sendadd = msg.getFrom()[0].toString();
		if (this.sendadd.startsWith("=?")) {
			// ���ļ�������ɷ���RFC822�淶
			this.sendadd = MimeUtility.decodeText(this.sendadd);
		}
		System.out.println(this.sendadd);

    	try {
    		//��ȡ����
    		//���ı��ʼ������
    		if(objmsg.getContentType().indexOf("text/plain") != -1) {
    			System.out.println(objmsg.getContentType());
    			this.content = (String) objmsg.getContent();
    		}
    		else if(objmsg.getContentType().indexOf("text/html") != -1) {
    			System.out.println(objmsg.getContentType());
    			this.content = (String) objmsg.getContent();
    			if(this.content.indexOf("<html>") == -1) {
	    			this.content = "<html><body>" + this.content + "</body></html>";
	    		}
    		}
    		//���������ʼ������
    		else {
        		Multipart b = (Multipart) objmsg.getContent();
        		this.getallcontent(b);		//����һ���ݹ鷽���������в��ֽ�һ������,����洢�����ݺ͸���
        		
        		
        		/*for(int j=0;j < this.bodylist.size();j++) {
        			System.out.println(this.bodylist.get(j).getContentType());
        		}
        		System.out.println(objmsg.getSubject());
        		System.out.println(objmsg.getContentType());
        		System.out.println(((Multipart) objmsg.getContent()).getBodyPart(0).getContentType());
        		System.out.println("��ӡ����");*/
        		
        		//�ڴ洢�����ݺ͸������������ҵ��ı�����
        		int j=0;
		    	for(;j < this.bodylist.size();j++) {
		    		if (this.bodylist.get(j).getContentType().indexOf("text/plain")!=-1) {
		    			this.content = (String) this.bodylist.get(j).getContent();
		    			break;
		    		}
				}
		    	if(j == this.bodylist.size()) {
		    		int l=0;
			    	for(;l < this.bodylist.size();l++) {
			    		if (this.bodylist.get(l).getContentType().indexOf("text/html")!=-1) {
			    			this.content = (String) this.bodylist.get(l).getContent();
			    			if(this.content.indexOf("<html>") == -1) {
			    			this.content = "<html><body>" + this.content + "</body></html>";
			    			}
			    		}
			    		break;
					}
		    	}
    		}
    	}
    	catch(Exception q) {
    		System.out.println("���쳣");
    		JOptionPane.showMessageDialog(this.dtlpage, "������ʧ��!", "Error",JOptionPane.WARNING_MESSAGE);
    		this.dtlpage.dispose();
    	}
    	//����ȡ����������ʾ����
    	if(this.content.indexOf("<html>") != -1) {
    		JLabel showcontent = new JLabel(this.content, JLabel.LEFT);
	    	showcontent.setVerticalAlignment(JLabel.TOP);
	    	showcontent.setPreferredSize(new Dimension(3000, 3000));
	    	JScrollPane showcontent_sp = new JScrollPane(showcontent);
	    	showcontent_sp.setBounds(150, 200, 450, 200);
	    	this.dtlpage.add(showcontent_sp);
    	}
    	else {
	    	JTextArea showcontent = new JTextArea();
	    	showcontent.setText(this.content);
	    	showcontent.setEditable(false);
	    	showcontent.setLineWrap(true);
	    	showcontent.setPreferredSize(new Dimension(400, 400));
	    	JScrollPane showcontent_sp = new JScrollPane(showcontent);
	    	showcontent_sp.setBounds(150, 200, 450, 200);
	    	this.dtlpage.add(showcontent_sp);
    	}
    	
    	
    	//���������ҵ��������ֲ���ʾ
    	JPanel showfiles = new JPanel();
    	showfiles.setLayout(null);
    	int i,sum;
    	for(i=0, sum=0;i<this.bodylist.size();i++) {
    		if(bodylist.get(i).getDisposition() != null) {
    			String filename = this.bodylist.get(i).getFileName();
    			if (filename.startsWith("=?")) {
    				// ���ļ�������ɷ���RFC822�淶
    				filename = MimeUtility.decodeText(filename);
    			}
    			System.out.println(filename);
    			JLabel a = new JLabel(filename);
    			a.setBounds(100, 20+sum*40, 300, 30);
    			showfiles.add(a);
    			JButton b = new JButton("����");
    			b.setBounds(20, 20+sum*40, 60, 30);
    			final DetailPage temp = this;
    			final int i_t = i;
    			b.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					try {
    						InputStream is = temp.bodylist.get(i_t).getInputStream();
    						JFileChooser jfc=new JFileChooser();
        	    			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        	    			jfc.showDialog(new JLabel(), "ѡ��");
        	    			File file=jfc.getSelectedFile();
        	    			if(file.isDirectory()){
        	    				System.out.println("�ļ���:"+file.getAbsolutePath());
        	    			}else if(file.isFile()){
        	    				System.out.println("�ļ�:"+file.getAbsolutePath());
        	    			}
        	    			String abspath = jfc.getSelectedFile().getAbsolutePath();
        	    			System.out.println(abspath);
        	    			saveFile(is, abspath +"\\" + temp.subject +"_", decodeText(temp.bodylist.get(i_t).getFileName()));
    					}
    					catch(Exception s) {
    						JOptionPane.showMessageDialog(a, "����ʧ��!", "��ʾ",JOptionPane.WARNING_MESSAGE);
    					}
    				}
    			});
    			showfiles.add(b);
    			sum+=1;
    		}
    	}
    	//���ù�����
    	showfiles.setPreferredSize(new Dimension(400, 80+sum*40));
    	JScrollPane showfiles_sp = new JScrollPane(showfiles);
    	showfiles_sp.setBounds(150, 420, 450, 200);
    	this.dtlpage.add(showfiles_sp);
    	
    	//��ʾ����
    	JTextField showsubject = new JTextField();
    	showsubject.setText(this.subject);
    	showsubject.setEditable(false);
    	showsubject.setBounds(150, 150, 400, 30);
    	this.dtlpage.add(showsubject);
    	//��ʾ����
    	JTextField showdate = new JTextField();
    	showdate.setText(Handledatestr(this.date));
    	showdate.setEditable(false);
    	showdate.setBounds(150, 100, 400, 30);
    	this.dtlpage.add(showdate);
    	//��ʾ������
    	JTextField showsendadd = new JTextField();
    	showsendadd.setText(this.sendadd);
    	showsendadd.setEditable(false);
    	showsendadd.setBounds(150, 50, 400, 30);
    	this.dtlpage.add(showsendadd);
    	
    	//��ʾ��ʶ��ǩ
    	JLabel files_l= new JLabel("����");
    	files_l.setBounds(50, 420, 80, 30);
    	this.dtlpage.add(files_l);
    	
    	JLabel content_l = new JLabel("����");
    	content_l.setBounds(50, 200, 80, 30);
    	this.dtlpage.add(content_l);
    	
    	JLabel subject_l = new JLabel("����");
    	subject_l.setBounds(50, 150, 80, 30);
    	this.dtlpage.add(subject_l);
    	
    	JLabel date_l = new JLabel("ʱ��");
    	date_l.setBounds(50, 100, 80, 30);
    	this.dtlpage.add(date_l);
    	
    	JLabel sendadd_l = new JLabel("������");
    	sendadd_l.setBounds(50, 50, 80, 30);
    	this.dtlpage.add(sendadd_l);
    	
    	//��������ҳ�����
    	this.dtlpage.setLayout(null);
    	this.dtlpage.setSize(800,700);
    	this.dtlpage.setLocationRelativeTo(null);
    	this.dtlpage.setVisible(true);
    	
	}
	
	
	//һ���ݹ鷽�����������в��ַ��ص�bodylist��
	public void getallcontent(Multipart msg) throws Exception{
		for(int i=0;i<msg.getCount();i++) {
			if(msg.getBodyPart(i).isMimeType("multipart/*")) {
				System.out.println("�����ڶ����");
				Multipart submsg = (Multipart) msg.getBodyPart(i).getContent();
				getallcontent(submsg);
			}
			else {
				bodylist.add(msg.getBodyPart(i));
			}
		}
	}
	

	//�洢�ļ�
	private static void saveFile(InputStream is, String destDir, String fileName) throws FileNotFoundException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(destDir + fileName)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
	}
	
	//�ı�����
	public static String decodeText(String encodeText) throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	
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
