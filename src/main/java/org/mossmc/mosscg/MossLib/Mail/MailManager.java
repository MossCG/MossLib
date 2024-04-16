package org.mossmc.mosscg.MossLib.Mail;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;
import org.mossmc.mosscg.MossLib.Object.ObjectMail;
import org.mossmc.mosscg.MossLib.Object.ObjectMailInfo;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件管理类
 * 不用动脑子，直接发邮件
 */
public class MailManager {
    /**
     * 初始化方法
     * 传入mailInfo+logger即可
     */
    public static void initMail(ObjectMailInfo info, ObjectLogger loggerIn,boolean display) {
        mailInfo = info;
        logger = loggerIn;
        displayLog = display;
        loadSession();
        logger.sendInfo("MossLib "+ BasicInfo.versionFull+" Mail Module By "+ BasicInfo.author);
    }

    /**
     * 邮箱功能基础信息
     */
    private static ObjectMailInfo mailInfo;
    private static ObjectLogger logger;
    private static Session mailSession;
    private static boolean displayLog;

    /**
     * 初始化Session
     */
    private static void loadSession() {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", mailInfo.mailSMTPHost);
        props.setProperty("mail.smtp.auth", "true");
        mailSession = Session.getInstance(props);
        mailSession.setDebug(mailInfo.mailDebug);
    }

    /**
     * 发送邮件方法
     * 只需要填标题+内容
     */
    public static void sendMail(ObjectMail mail) throws Exception {
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(mail.fromMail, mail.personalName, mail.charset));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mail.targetMail, mail.targetMail, mail.charset));
        message.setSubject(mail.title, mail.charset);
        message.setContent(mail.content, "text/html;charset="+mail.charset);
        message.setSentDate(new Date());
        message.saveChanges();
        Transport mailTransport = mailSession.getTransport();
        mailTransport.connect(mailInfo.mailAccount, mailInfo.mailPassword);
        mailTransport.sendMessage(message, message.getAllRecipients());
        mailTransport.close();
        if (displayLog) {
            logger.sendInfo("发送了一封邮件：");
            logger.sendInfo(mail.fromMail+"["+mail.personalName+"] -> "+mail.targetMail);
            logger.sendInfo(mail.title+"["+mail.charset+"]");
            logger.sendInfo(mail.content);
        }
    }
}
