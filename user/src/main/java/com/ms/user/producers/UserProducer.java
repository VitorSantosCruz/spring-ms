package com.ms.user.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ms.user.dtos.EmailDto;
import com.ms.user.models.UserModel;

@Component
public class UserProducer {
  private final RabbitTemplate rabbitTemplate;

  @Value("${broker.queue.email.name}")
  private String routingKey;

  public UserProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishMessageEmail(UserModel userModel) {
    var emailDto = new EmailDto();
    emailDto.setUserId(userModel.getUserId());
    emailDto.setEmailTo(userModel.getEmail());
    emailDto.setSubject("Cadastro realizado com sucesso");
    emailDto.setText(userModel.getName() + ", seja bem vindo(a)! \nagradecemos o seu cadastro aproveite agora todos os recursos da nossa plataforma!");

    rabbitTemplate.convertAndSend("", routingKey, emailDto);
  }
}
