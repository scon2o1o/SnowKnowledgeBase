package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Smtp;
import net.javaguides.springboot.repository.SmtpRepository;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

@Service
public class SmtpServiceImpl implements SmtpService {

    private SmtpRepository smtpRepository;

    public SmtpServiceImpl(SmtpRepository smtpRepository) {
        super();
        this.smtpRepository = smtpRepository;
    }

    @Override
    public List<Smtp> getAllSmtp() {
        return smtpRepository.findAll();
    }

    @Override
    public Smtp saveSmtp(Smtp smtp) {
        return smtpRepository.save(smtp);
    }

    @Override
    public Smtp getSmtpById(Long id) {
        return smtpRepository.findById(id).get();
    }

    @Override
    public Smtp updateSmtp(Smtp smtp) {
        return smtpRepository.save(smtp);
    }

    @Override
    public void deleteSmtpById(Long id) {
        smtpRepository.deleteById(id);
    }

    @Override
    public void sendEmail(String toAddress, String subject, String body) {
        List<Smtp> smtp = smtpRepository.findAll();
        Properties props = new Properties();
        if (smtp.get(0).getAuth().equals("Y")) {
            props.put("mail.smtp.auth", true);
        }
        if (smtp.get(0).getAuth().equals("N")) {
            props.put("mail.smtp.auth", false);
        }
        if (smtp.get(0).getStarttls().equals("Y")) {
            props.put("mail.smtp.starttls.enable", true);
        }
        if (smtp.get(0).getStarttls().equals("N")) {
            props.put("mail.smtp.starttls.enable", false);
        }
        props.put("mail.smtp.host", smtp.get(0).getServer());
        props.put("mail.smtp.port", smtp.get(0).getPort());
        props.put("mail.smtp.ssl.trust", smtp.get(0).getServer());

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtp.get(0).getUsername(), smtp.get(0).getPassword());
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtp.get(0).getFromAddress()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(subject);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e1) {
            throw new RuntimeException(e1);
        }

    }
}
