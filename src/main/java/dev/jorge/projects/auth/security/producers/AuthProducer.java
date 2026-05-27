package dev.jorge.projects.auth.security.producers;

import dev.jorge.projects.auth.security.dtos.responses.EmailResponse;
import dev.jorge.projects.auth.user.entities.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProducer {

    final RabbitTemplate rabbitTemplate;

    public AuthProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(User user) {
        EmailResponse response = new EmailResponse();
        response.setUserId(user.getId());
        response.setEmailTo(user.getEmail());
        response.setSubject("Cadastro realizado com sucesso!");
        response.setText(user.getFirstName() + ", seja bem vindo(a)! \nAgradecemos o seu cadastro, aproveite agora todos os recursos da nossa plataforma!");

        rabbitTemplate.convertAndSend("", routingKey, response);
    }
}
