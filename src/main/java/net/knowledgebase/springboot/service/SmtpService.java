package net.knowledgebase.springboot.service;

import net.knowledgebase.springboot.model.Smtp;

import java.util.List;

public interface SmtpService {

    List<Smtp> getAllSmtp();

    Smtp saveSmtp(Smtp smtp);

    Smtp getSmtpById(Long id);

    Smtp updateSmtp(Smtp smtp);

    void deleteSmtpById(Long id);

    void sendEmail(String toAddress, String subject, String body);
}
