import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.*;

//sendmail page
public class SendPage extends JFrame{
	public String senderAccount = "";   //sender account
	public String senderAddress = "";	//sender address
	public String senderPassword = "";	//sender password
	public String sendname = "";		//sender ID
	JFrame f=null;
	String smtpadd = null;				//smtp address
	
	
	public SendPage(String s1, String s2, String s3, String s4) {
		senderAddress = s1;
		senderAccount = s2;
		senderPassword = s3;
		sendname = s4;
		smtpadd = "smtp." + s1.split("@")[1];
        f = new JFrame("发送界面");
             
        //labels
        JLabel sendadd_l = new JLabel("收件人地址:", JLabel.LEFT);
        sendadd_l.setBounds(20, 50, 80, 30);
        
        JLabel sendtopic_l = new JLabel("主题:", JLabel.LEFT);
        sendtopic_l.setBounds(20, 100, 80, 30);
        
        JLabel sendbody_l = new JLabel("内容:", JLabel.LEFT);
        sendbody_l.setBounds(20, 150, 80, 30);
        
        JLabel filelist_l = new JLabel("附件:", JLabel.LEFT);
        filelist_l.setBounds(20, 520, 80, 30);
        
        //buttons
        JButton sendbtn = new JButton("发送");
        sendbtn.setBounds(250, 700, 100, 30);
        
        JButton fileadd = new JButton("添加附件");
        fileadd.setBounds(150, 480, 100, 20);
        
        JButton filedel = new JButton("删除附件");
        filedel.setBounds(350, 480, 100, 20);
        
        //text input spaces
        JTextField sendadd = new JTextField();
        sendadd.setBounds(100, 50, 450, 30);
        
        
        JTextField sendtopic = new JTextField();
        sendtopic.setBounds(100, 100, 450, 30);
           
        JTextArea sendbody = new JTextArea();
        sendbody.setLineWrap(true);
        sendbody.setPreferredSize(new Dimension(400, 300));
        JScrollPane sendbody_sp = new JScrollPane(sendbody);
        sendbody_sp.setBounds(100, 150, 450, 300);

        JTextArea filelist = new JTextArea();
        filelist.setLineWrap(true);
        filelist.setPreferredSize(new Dimension(400, 150));
        JScrollPane filelist_sp = new JScrollPane(filelist);
        filelist_sp.setBounds(100, 520, 450, 150);
        
        //add those items
        f.add(sendbtn);
        f.add(sendtopic);
        f.add(sendtopic_l);
        f.add(sendadd);
        f.add(sendadd_l);
        f.add(sendbody_sp);
        f.add(sendbody_l);
        f.add(fileadd);
        f.add(filedel);
        f.add(filelist_l);
        f.add(filelist_sp);
        f.setSize(600, 800);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setVisible(true);
        
        //buttons' action
        //select attachment
        class FileSelect extends JFrame{
    		JButton open=null;
    		JTextArea filelt = null;
    		JButton confirm=null;
    		public FileSelect(){
    			confirm = new JButton("确认");
    			confirm.setBounds(200, 420, 100, 30);
    			this.add(confirm);
    			open=new JButton("打开");
    			open.setBounds(200, 50, 100, 30);
    			this.add(open);
    			filelt = new JTextArea();
                filelt.setLineWrap(true);
                filelt.setBounds(100, 100, 300, 300);
                this.add(filelt);
                this.setLayout(null);
                this.setLocationRelativeTo(null);
    			this.setSize(500, 500);
    			this.setVisible(true);
    			//use system to open the index
    			open.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    	    			JFileChooser jfc=new JFileChooser();
    	    			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
    	    			jfc.showDialog(new JLabel(), "选择");
    	    			File file=jfc.getSelectedFile();
    	    			if(file.isDirectory()){
    	    				System.out.println("文件夹:"+file.getAbsolutePath());		//选择文件夹
    	    			}else if(file.isFile()){
    	    				System.out.println("文件:"+file.getAbsolutePath());		//选择文件
    	    			}
    	    			System.out.println(jfc.getSelectedFile().getName());
    	    			filelt.setText(filelt.getText()  + jfc.getSelectedFile().getAbsolutePath() + "\n");
    	    		}
    			});
    			confirm.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    	    			filelist.setText(filelt.getText());
    	    			dispose();
    	    		}
    			});
    			//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    		}
    	}
        
        //check, cancel and confirm the attachment
        class FileOut extends JFrame{
    		JButton confirm=null;
    		String filetext = filelist.getText();
    		String[] filetextsub = filetext.split("\n");
    		JButton[] buttons = new JButton[filetextsub.length];
    		JTextArea[] filepathes = new JTextArea[filetextsub.length];
    		public FileOut(){	
    			int i;
    			//cancel
    			for(i=0;i<filetextsub.length;i++) {
    				buttons[i] = new JButton();
    				buttons[i].setText("删除");
    				buttons[i].setBounds(450, 20+i*80, 80, 30);
    				this.add(buttons[i]);
    				filepathes[i] = new JTextArea();
    				filepathes[i].setText(filetextsub[i]);
    				filepathes[i].setBounds(50, 20+i*80, 380, 60);
    				filepathes[i].setLineWrap(true);
    				filepathes[i].setEditable(false);
    				this.add(filepathes[i]);
    				final int i_f = i;
    				buttons[i].addActionListener(new ActionListener(){
    					public void actionPerformed(ActionEvent e) {
    						filetextsub[i_f] = "";
    						filepathes[i_f].setText("");
    					}
    				});
    			}
    			//confirm
    			confirm = new JButton("确认");
    			confirm.setBounds(250, 20+i*80, 100, 30);
    			i++;
    			this.add(confirm);
                this.setLayout(null);
                this.setLocationRelativeTo(null);
    			this.setSize(600, 20+i*80);
    			this.setVisible(true);
    			confirm.addActionListener(new ActionListener(){
    				public void actionPerformed(ActionEvent e) {
    					String filetext_new = "";
    					for(int i=0;i<filetextsub.length;i++) {
    						if(! filetextsub[i].equals("")) {
    							filetext_new = filetext_new + filetextsub[i] + "\n";
    						}
    					}
    					filelist.setText(filetext_new);
    					dispose();
    				}
    				
    			});
    			//this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    		}
    	}
        
        //add attachment button
        fileadd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new FileSelect();
        	}
        });
        
        //cancel attachment button
        filedel.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(filelist.getText().equals("")){
    				JOptionPane.showMessageDialog(f, "已选择附件为空!", "Error",JOptionPane.WARNING_MESSAGE);
    			}
        		else{
        			new FileOut();
        		}
        	}
        });
        
        //send button
        final SendPage this_f = this;
        sendbtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		NoticeFrame SendNotice = new NoticeFrame("发送中");
            	Thread th = new Thread(){
            		public void run() {
		            	try {
		            		this_f.SendMsg(sendadd.getText(),sendtopic.getText(), sendbody.getText(), filelist.getText());
		            		JOptionPane.showMessageDialog(f, "发送成功!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SendNotice.dispose();
		            		sendbody.setText("");
		            		sendtopic.setText("");
		            		filelist.setText("");
		            		sendadd.setText("");
		            	}catch(Exception a) {
		            		JOptionPane.showMessageDialog(f, "发送失败!", "提示",JOptionPane.WARNING_MESSAGE);
		            		SendNotice.dispose();
		            	}
            		}
            	};
            	th.setPriority(Thread.MIN_PRIORITY);
            	th.start();
        	}
        });
    }

    public void SendMsg(String address, String topic, String body, String files) throws Exception{
    	Properties props = new Properties();
        //创建定义整个应用程序所需的环境信息的 Session 对象
        props.setProperty("mail.smtp.host",smtpadd);//发送邮箱服务器
        props.setProperty("mail.smtp.port","465");//发送端口
        props.setProperty("mail.smtp.auth","true");//是否开启权限控制
        props.setProperty("mail.debug","true");//true 打印信息到控制台
        props.setProperty("mail.transport","smtp");//发送的协议是简单的邮件传输协议
        props.setProperty("mail.smtp.ssl.enable","true");

        Session session = Session.getInstance(props);
        //创建邮件的实例对象
        Message msg = getMimeMessage(session, address, topic, body, files);
        //根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址
        transport.sendMessage(msg,msg.getAllRecipients());
        //关闭邮件连接
        transport.close();
    }
    
    
	public MimeMessage getMimeMessage(Session session, String recpaddress,String subject, String body, String files) throws Exception{
	    //创建一封邮件的实例对象
	    MimeMessage msg = new MimeMessage(session);
	    //设置发件人地址
	    msg.setFrom(new InternetAddress(senderAddress, sendname, "UTF-8"));
	    //设置收件人
	    if(recpaddress.indexOf(",") != -1) {
	    	//多收件人情况
	    	InternetAddress[] recpaddresses = new InternetAddress().parse(recpaddress);
	    	msg.setRecipients(MimeMessage.RecipientType.TO,recpaddresses);   
	    }
	    else {
	    	//单收件人情况
	    	msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recpaddress));
	    }
	    //设置邮件主题
	    msg.setSubject(subject,"UTF-8");
	    //设置邮件正文
	    //创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(body, "text/plain;charset=UTF-8");
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        //创建附件"节点"
        if(!files.equals("")) {
	        String[] filessub = files.split("\n");
	        MimeBodyPart[] attachments = new MimeBodyPart[filessub.length];
	        //多附件发送
	        for(int i=0;i<filessub.length;i++) {
	        	attachments[i] = new MimeBodyPart();
	        	DataHandler dh = new DataHandler(new FileDataSource(filessub[i]));
	        	attachments[i].setDataHandler(dh);
	        	attachments[i].setFileName(MimeUtility.encodeText(dh.getName()));
	        	mm.addBodyPart(attachments[i]);
	        }
        }
        mm.setSubType("mixed");         // 混合关系
        //设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        msg.setContent(mm);
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
	    return msg;
	}
	
}
