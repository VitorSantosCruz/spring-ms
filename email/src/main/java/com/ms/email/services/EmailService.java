package com.ms.email.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ms.email.enums.StatusEmail;
import com.ms.email.models.EmailModel;
import com.ms.email.repository.EmailRepository;

import jakarta.transaction.Transactional;

@Service
public class EmailService {
  private final EmailRepository emailRepository;
  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String emailFrom;

  public EmailService(EmailRepository emailRepository, JavaMailSender mailSender) {
    this.emailRepository = emailRepository;
    this.mailSender = mailSender;
  }

  @Transactional
  public EmailModel sendEmail(EmailModel emailModel) {
    try {
      emailModel.setSendDateEmail(LocalDateTime.now());
      emailModel.setEmailFrom(emailFrom);

      var message = new SimpleMailMessage();
      message.setTo(emailModel.getEmailTo());
      message.setSubject(emailModel.getSubject());
      message.setText(emailModel.getText());

      mailSender.send(message);

      emailModel.setStatusEmail(StatusEmail.SENT);
    } catch (Exception e) {
      emailModel.setStatusEmail(StatusEmail.ERROR);
    }

    return emailRepository.save(emailModel);
  }
}
