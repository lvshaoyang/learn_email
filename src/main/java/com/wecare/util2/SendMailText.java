package com.wecare.util2;

import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/***
 * @Author lvsy
 * @Date 2018/7/27 18:04
 ***/
public class SendMailText {
    //发件人地址
    public static String senderAddress = "3055203368@qq.com";
    //收件人地址
    public static String recipientAddress = "lvsy@wecaresoft.net";
    //发件人账户名
    public static String senderAccount = "3055203368@qq.com";
    //发件人账户密码
    public static String senderPassword = "esloqyhljjmndgib";

    public static void main(String[] args) throws Exception {
        //1、连接邮件服务器的参数配置
        Properties props = new Properties();
        //设置用户的认证方式
        props.setProperty("mail.smtp.auth", "true");
        //设置传输协议
        props.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
//                // 用户名、密码
//                String userName = props.getProperty("mail.user");
//                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(senderAccount, senderPassword);
            }
        };

        //2、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getInstance(props,authenticator);
        //设置调试信息在控制台打印出来
        session.setDebug(true);
        //3、创建邮件的实例对象
        Message msg = getMimeMessage(session);
        //4、根据session对象获取邮件传输对象Transport
        Transport transport = session.getTransport();
        //设置发件人的账户名和密码
        transport.connect(senderAccount, senderPassword);
        //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(msg,msg.getAllRecipients());

        //如果只想发送给指定的人，可以如下写法
        //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});

        //5、关闭邮件连接
        transport.close();
    }

    /**
     * 获得创建一封邮件的实例对象
     * @param session
     * @return
     * @throws MessagingException
     * @throws AddressException
     */
    public static MimeMessage getMimeMessage(Session session) throws Exception{
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(recipientAddress));
        //设置邮件主题
        msg.setSubject("邮件主题","UTF-8");
        //设置邮件正文
        msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }

    @Test
    public void test() throws MessagingException {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");//遇到最多的坑就是下面这行，不加要报“A secure connection is requiered”错。
        props.put("mail.smtp.starttls.enable", "true");
        // 发件人的账号
        props.put("mail.user", senderAccount);
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", senderPassword);

        //props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
//                String userName = props.getProperty("mail.user");
//                String password = props.getProperty("mail.password");
                return new PasswordAuthentication("3055203368@qq.com", "iktdtwmvovnbddcj");
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress("lvsy@wecaresoft.net");
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置抄送，抄送和密送如果不写正确的地址也要报错。最好注释不用。
//        InternetAddress cc = new InternetAddress("");
//        message.setRecipient(RecipientType.CC, cc);
//
//        // 设置密送，其他的收件人不能看到密送的邮件地址
//        InternetAddress bcc = new InternetAddress("");
//        message.setRecipient(RecipientType.CC, bcc);

        // 设置邮件标题
        message.setSubject("JAVA测试邮件");

        // 设置邮件的内容体
        message.setContent("<a href='http://www.XXX.org'>测试的邮件</a>", "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);

    }


    ////////////////////////////////////////////////////////////////////////////



    @Test
    public void qqSender() {
        MailSSLSocketFactory msf = null;
        try {
            msf = new MailSSLSocketFactory();
            msf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }


        Properties props = new Properties();
// 开启调试
        props.setProperty("mail.debug", "true");
// 是否需要验证
        props.setProperty("mail.smtp.auth", "true");
// 发送邮件服务器
        props.setProperty("mail.smtp.host", "smtp.qq.com");
// 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", msf);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.socketFactory.port", "465");
//        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");


// 使用匿名内部类，用邮箱进行验证
        Session session = Session.getInstance(props, new Authenticator() {


            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
// 通过用户名和密码进行验证
                return new PasswordAuthentication("3055203368@qq.com",
                        "iktdtwmvovnbddcj");
            }


        });
        Message message = new MimeMessage(session);
        try {
// 设置邮件发送方
            message.setFrom(new InternetAddress("3055203368@qq.com"));
// 设置邮件标题
            message.setSubject("测试");
// 设置邮件内容
            message.setContent("测试", "text/html;charset=utf-8");
// 设置邮件接收方
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    "lvsy@wecaresoft.net"));


// 发送邮件
            Transport.send(message);


        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
















}
