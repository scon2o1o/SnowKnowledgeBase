package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Smtp;

import java.util.List;

public interface SmtpService {

    List<Smtp> getAllSmtp();

    Smtp saveSmtp(Smtp smtp);

    Smtp getSmtpById(Long id);

    Smtp updateSmtp(Smtp smtp);

    void deleteSmtpById(Long id);
}
