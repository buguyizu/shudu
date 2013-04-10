package com.appspot.shudu99.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		try {
			setSendEmailInfo();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//https://developers.google.com/appengine/docs/java/mail/overview#Quotas_and_Limits
	private void setSendEmailInfo() throws AddressException, MessagingException, UnsupportedEncodingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);

        //发件地址，姓名
        msg.setFrom(new InternetAddress("yahveh.chen@gmail.com","y"));
        //收件地址
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("yahveh.chen@gmail.com","y"));
        //主题
        msg.setSubject("Your Example.com account has been activated");
        //正文
        msg.setText("haha");

        // メールの形式を指定
        //msg.setHeader("Content-Type", "text/html");
        // 送信日付を指定
        //msg.setSentDate(new Date());
        // 送信します
        Transport.send(msg);
	}
}
