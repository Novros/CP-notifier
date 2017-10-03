package cz.novros.cp.web.jms;

import javax.annotation.Nonnull;

import org.springframework.jms.core.JmsTemplate;

import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.user.UserMessage;

/**
 * Abstract class for all jms services in this project.
 */
public abstract class AbstractJmsService extends cz.novros.cp.jms.service.AbstractJmsService {

	protected AbstractJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate, QueueNames.WEB_QUEUE);
	}

	protected void fillBasicInfo(@Nonnull final UserMessage message, @Nonnull final String username) {
		message.setUsername(username);
		fillBasicInfo(message);
	}
}
