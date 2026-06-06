package dev.jorge.projects.auth.producer;

import dev.jorge.projects.auth.dto.response.EmailResponse;
import dev.jorge.projects.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProducer {

    private final RabbitTemplate rabbitTemplate;

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
