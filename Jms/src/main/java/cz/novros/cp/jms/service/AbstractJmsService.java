package cz.novros.cp.jms.service;

import java.util.Date;

import javax.annotation.Nonnull;

import org.springframework.jms.core.JmsTemplate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.JmsConstants;
import cz.novros.cp.jms.message.AbstractJmsMessage;

/**
 * This class should extends all jms services.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractJmsService {

	JmsTemplate jmsTemplate;
	String jmsQueueName;

	protected AbstractJmsService(@Nonnull final JmsTemplate jmsTemplate, final String jmsQueueName) {
		this.jmsTemplate = jmsTemplate;
		this.jmsQueueName = jmsQueueName;
	}

	/**
	 * Fill basic info into message.
	 *
	 * @param message Message which will be filled.
	 */
	protected void fillBasicInfo(@Nonnull final AbstractJmsMessage message) {
		message.setSenderQueue(jmsQueueName);
		message.setMessageId(Long.toString(new Date().getTime()));
	}

	/**
	 * Wait for response in jms queue.
	 *
	 * @param requestMessage Message which was send to queue.
	 * @param <T>            Expected type of response.
	 *
	 * @return Received response message.
	 */
	protected <T> T recieveResponse(@Nonnull final AbstractJmsMessage requestMessage) {
		return (T) jmsTemplate.receiveSelectedAndConvert(requestMessage.getSenderQueue(), JmsConstants.getResponseSelector(requestMessage.getMessageId()));
	}

	/**
	 * Create response by given request message and add response to it.
	 *
	 * @param requestMessage Message which will service recieved.
	 * @param response       Response on request message.
	 */
	protected void sendResponse(@Nonnull final AbstractJmsMessage requestMessage, @Nonnull final Object response) {
		jmsTemplate.convertAndSend(requestMessage.getSenderQueue(),
				response,
				message -> {
					message.setJMSCorrelationID(JmsConstants.RESPONSE_PREFIX + requestMessage.getMessageId());
					return message;
				});
	}
}
