package cz.novros.cp.web;

import javax.jms.ConnectionFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import cz.novros.cp.jms.CommonConstants;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.reponse.BooleanResponseMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;

@ComponentScan
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(JmsTemplate jmsTemplate) {
		return (args) -> {
			final String email = "test@test.cz";

			final RegisterUserMessage message = new RegisterUserMessage();
			message.setUsername(email);
			message.setPassword("password");
			message.setSenderQueue(QueueNames.WEB_QUEUE);
			message.setMessageId(email);

			System.out.println("Sending an user message.");
			jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
			final BooleanResponseMessage response = (BooleanResponseMessage) jmsTemplate.receiveSelectedAndConvert(QueueNames.WEB_QUEUE, CommonConstants.getResponseSelector(email));
			System.out.println(response);
		};
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}
