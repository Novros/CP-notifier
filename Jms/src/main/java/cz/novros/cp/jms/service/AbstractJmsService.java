package cz.novros.cp.jms.service;

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

	protected AbstractJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
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
