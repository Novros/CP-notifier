package cz.novros.cp.web.jms;

import java.util.Date;

import javax.annotation.Nonnull;

import org.springframework.jms.core.JmsTemplate;

import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.user.UserMessage;

/**
 * Abstract class for all jms services in this project.
 */
public abstract class AbstractJmsService extends cz.novros.cp.jms.service.AbstractJmsService {

	protected AbstractJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	protected static void fillBasicInfo(@Nonnull final UserMessage message, @Nonnull final String username) {
		message.setUsername(username);
		fillBasicInfo((AbstractJmsMessage) message, username);
	}

	protected static void fillBasicInfo(@Nonnull final AbstractJmsMessage message, @Nonnull final String username) {
		message.setSenderQueue(QueueNames.WEB_QUEUE);
		message.setMessageId(username + new Date().getTime());
	}

}
