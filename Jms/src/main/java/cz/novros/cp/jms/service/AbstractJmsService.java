package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import org.springframework.jms.core.JmsTemplate;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.jms.CommonConstants;
import cz.novros.cp.jms.message.AbstractJmsMessage;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractJmsService {

	JmsTemplate jmsTemplate;

	protected AbstractJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	protected void sendResponse(@Nonnull final AbstractJmsMessage requestMessage, @Nonnull final Object response) {
		jmsTemplate.convertAndSend(requestMessage.getSenderQueue(),
				response,
				message -> {
					message.setJMSCorrelationID(CommonConstants.RESPONSE_PREFIX + requestMessage.getMessageId());
					return message;
				});
	}
}
