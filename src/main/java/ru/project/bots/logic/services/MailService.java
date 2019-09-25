package ru.project.bots.logic.services;

import ru.project.bots.model.dao.StatisticsDAO;
import ru.project.bots.model.hibernate.GenericDAO;
import ru.project.bots.model.hibernate.HibernateStatisticsDAO;
import ru.project.bots.model.logs.SimpleLogger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService implements Runnable{

    private final String to;
    private final String username;
    private final String password;

    private final String mailHost = "smtp.gmail.com";
    private final String mailPort = "587";
    private final boolean sending = true;

    private final SimpleLogger logger;

    public MailService(String to, String username, String password, SimpleLogger logger) {
        this.to = to;
        this.username = username;
        this.password = password;
        this.logger = logger;

        new Thread(this).start();
    }

    @Override
    public void run() {

        while (sending){

            GenericDAO genericDAO = null;
            try {

                final StatisticsDAO dao = new HibernateStatisticsDAO();
                genericDAO = (GenericDAO) dao;
                genericDAO.beginTx();

                final long dialogs = dao.countDialogs();
                final long orders = dao.countOrders();

                genericDAO.commitTx();

                final Properties properties = new Properties();
                properties.put("mail.smtp.auth", true);
                properties.put("mail.smtp.starttls.enable", true);
                properties.put("mail.smtp.host", mailHost);
                properties.put("mail.smtp.port", mailPort);
                properties.put("mail.smtp.socketFactory.fallback", "true");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                final Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(username));
                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject("Bot statistics");

                final MimeBodyPart mimeBodyPart = new MimeBodyPart();
                String text = "<h2>Dialogs: " + orders + "</h2>" +
                        "<h2>Orders: " + dialogs +" </h2>";
                mimeBodyPart.setContent(text, "text/html");

                final Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);

            }catch (Exception e){
                logger.log(e);
                if(genericDAO != null)
                    genericDAO.rollback();
            }

            try {
                Thread.sleep(3_600_000);
            }catch (Exception exc){
                new Thread(this).run();
                Thread.currentThread().interrupt();
            }
        }

    }
}
