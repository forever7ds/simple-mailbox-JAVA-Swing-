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
	
	ArrayList<BodyPart> bodylist = new ArrayList<BodyPart>();	//附件解析成bodypart的链表
	String content = "";
	
	public DetailPage(Message msg) throws Exception{
		this.objmsg = msg;
		dtlpage = new JFrame("邮件详情");
		//提取message的各个内容
		//主题
		this.subject = msg.getSubject();
		//日期
		this.date = msg.getSentDate();
		//发件人
		this.sendadd = msg.getFrom()[0].toString();
		if (this.sendadd.startsWith("=?")) {
			// 把文件名编码成符合RFC822规范
			this.sendadd = MimeUtility.decodeText(this.sendadd);
		}
		System.out.println(this.sendadd);

    	try {
    		//提取内容
    		//纯文本邮件的情况
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
    		//复合类型邮件的情况
    		else {
        		Multipart b = (Multipart) objmsg.getContent();
        		this.getallcontent(b);		//调用一个递归方法解析所有部分进一个链表,链表存储着内容和附件
        		
        		
        		/*for(int j=0;j < this.bodylist.size();j++) {
        			System.out.println(this.bodylist.get(j).getContentType());
        		}
        		System.out.println(objmsg.getSubject());
        		System.out.println(objmsg.getContentType());
        		System.out.println(((Multipart) objmsg.getContent()).getBodyPart(0).getContentType());
        		System.out.println("打印结束");*/
        		
        		//在存储着内容和附件的链表中找到文本内容
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
    		System.out.println("有异常");
    		JOptionPane.showMessageDialog(this.dtlpage, "打开详情失败!", "Error",JOptionPane.WARNING_MESSAGE);
    		this.dtlpage.dispose();
    	}
    	//把提取到的内容显示出来
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
    	
    	
    	//在链表中找到附件部分并显示
    	JPanel showfiles = new JPanel();
    	showfiles.setLayout(null);
    	int i,sum;
    	for(i=0, sum=0;i<this.bodylist.size();i++) {
    		if(bodylist.get(i).getDisposition() != null) {
    			String filename = this.bodylist.get(i).getFileName();
    			if (filename.startsWith("=?")) {
    				// 把文件名编码成符合RFC822规范
    				filename = MimeUtility.decodeText(filename);
    			}
    			System.out.println(filename);
    			JLabel a = new JLabel(filename);
    			a.setBounds(100, 20+sum*40, 300, 30);
    			showfiles.add(a);
    			JButton b = new JButton("下载");
    			b.setBounds(20, 20+sum*40, 60, 30);
    			final DetailPage temp = this;
    			final int i_t = i;
    			b.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					try {
    						InputStream is = temp.bodylist.get(i_t).getInputStream();
    						JFileChooser jfc=new JFileChooser();
        	    			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        	    			jfc.showDialog(new JLabel(), "选择");
        	    			File file=jfc.getSelectedFile();
        	    			if(file.isDirectory()){
        	    				System.out.println("文件夹:"+file.getAbsolutePath());
        	    			}else if(file.isFile()){
        	    				System.out.println("文件:"+file.getAbsolutePath());
        	    			}
        	    			String abspath = jfc.getSelectedFile().getAbsolutePath();
        	    			System.out.println(abspath);
        	    			saveFile(is, abspath +"\\" + temp.subject +"_", decodeText(temp.bodylist.get(i_t).getFileName()));
    					}
    					catch(Exception s) {
    						JOptionPane.showMessageDialog(a, "下载失败!", "提示",JOptionPane.WARNING_MESSAGE);
    					}
    				}
    			});
    			showfiles.add(b);
    			sum+=1;
    		}
    	}
    	//设置滚动条
    	showfiles.setPreferredSize(new Dimension(400, 80+sum*40));
    	JScrollPane showfiles_sp = new JScrollPane(showfiles);
    	showfiles_sp.setBounds(150, 420, 450, 200);
    	this.dtlpage.add(showfiles_sp);
    	
    	//显示主题
    	JTextField showsubject = new JTextField();
    	showsubject.setText(this.subject);
    	showsubject.setEditable(false);
    	showsubject.setBounds(150, 150, 400, 30);
    	this.dtlpage.add(showsubject);
    	//显示日期
    	JTextField showdate = new JTextField();
    	showdate.setText(Handledatestr(this.date));
    	showdate.setEditable(false);
    	showdate.setBounds(150, 100, 400, 30);
    	this.dtlpage.add(showdate);
    	//显示发件人
    	JTextField showsendadd = new JTextField();
    	showsendadd.setText(this.sendadd);
    	showsendadd.setEditable(false);
    	showsendadd.setBounds(150, 50, 400, 30);
    	this.dtlpage.add(showsendadd);
    	
    	//显示标识标签
    	JLabel files_l= new JLabel("附件");
    	files_l.setBounds(50, 420, 80, 30);
    	this.dtlpage.add(files_l);
    	
    	JLabel content_l = new JLabel("内容");
    	content_l.setBounds(50, 200, 80, 30);
    	this.dtlpage.add(content_l);
    	
    	JLabel subject_l = new JLabel("主题");
    	subject_l.setBounds(50, 150, 80, 30);
    	this.dtlpage.add(subject_l);
    	
    	JLabel date_l = new JLabel("时间");
    	date_l.setBounds(50, 100, 80, 30);
    	this.dtlpage.add(date_l);
    	
    	JLabel sendadd_l = new JLabel("发件人");
    	sendadd_l.setBounds(50, 50, 80, 30);
    	this.dtlpage.add(sendadd_l);
    	
    	//设置详情页面参数
    	this.dtlpage.setLayout(null);
    	this.dtlpage.setSize(800,700);
    	this.dtlpage.setLocationRelativeTo(null);
    	this.dtlpage.setVisible(true);
    	
	}
	
	
	//一个递归方法解析出所有部分返回到bodylist中
	public void getallcontent(Multipart msg) throws Exception{
		for(int i=0;i<msg.getCount();i++) {
			if(msg.getBodyPart(i).isMimeType("multipart/*")) {
				System.out.println("遭遇第二层次");
				Multipart submsg = (Multipart) msg.getBodyPart(i).getContent();
				getallcontent(submsg);
			}
			else {
				bodylist.add(msg.getBodyPart(i));
			}
		}
	}
	

	//存储文件
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
	
	//文本解码
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
        //处理时间
        return year+'.'+month+'.'+day+' '+time;
    }
	
}
