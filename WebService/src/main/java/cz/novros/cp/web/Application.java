package cz.novros.cp.web;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@ComponentScan
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(JmsTemplate jmsTemplate) {
		return (args) -> {
//			// Send a message with a POJO - the template reuse the message converter
//			System.out.println("Sending an email message.");
//			final MessageCreator messageCreator = new MessageCreator() {
//				@Override
//				public Message createMessage(final Session session) throws JMSException {
//					return session.createObjectMessage(new UserMessage("info@example.com", "password"));
//				}
//			};
//			final Message message = jmsTemplate.sendAndReceive("database-user", messageCreator);
//			System.out.println("Message recieved: " + message);

			final String email = "test@test.cz";
			final Map<String, String> message = new HashMap<>();
			message.put("action", "register");
			message.put("username", email);
			message.put("password", "password");
			message.put("sender", "web-service");

			System.out.println("Sending an user message.");
			jmsTemplate.convertAndSend("database-user", message);
			final Boolean response = (Boolean) jmsTemplate.receiveSelectedAndConvert("web-service", "JMSCorrelationID='" + email + "'");
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
