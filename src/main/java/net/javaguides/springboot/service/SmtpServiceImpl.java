package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Smtp;
import net.javaguides.springboot.repository.SmtpRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
