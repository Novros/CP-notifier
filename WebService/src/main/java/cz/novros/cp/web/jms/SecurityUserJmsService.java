package cz.novros.cp.web.jms;

import javax.annotation.Nonnull;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.jms.JmsConstants;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.reponse.BooleanResponseMessage;
import cz.novros.cp.jms.message.user.LoginUserMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;

@Service
@Profile("jms")
@Slf4j
public class SecurityUserJmsService extends AbstractJmsService implements SecurityUserService {

	protected SecurityUserJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Override
	public boolean registerUser(@Nonnull final String username, @Nonnull final String password) {
		log.debug("Trying to register user({}).", username);

		final RegisterUserMessage message = new RegisterUserMessage();
		fillBasicInfo(message, username);
		message.setPassword(password);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final BooleanResponseMessage booleanResponseMessage = (BooleanResponseMessage) jmsTemplate.receiveSelectedAndConvert(message.getSenderQueue(), JmsConstants.getResponseSelector(message.getMessageId()));

		log.debug("User({}) was " + (booleanResponseMessage.isOk() ? "not " : "") + "registered.");

		return booleanResponseMessage.isOk();
	}

	@Override
	public boolean loginUser(@Nonnull final String username, @Nonnull final String password) {
		log.debug("Trying to login user({}).", username);

		final LoginUserMessage message = new LoginUserMessage();
		fillBasicInfo(message, username);
		message.setPassword(password);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final BooleanResponseMessage booleanResponseMessage = (BooleanResponseMessage) jmsTemplate.receiveSelectedAndConvert(message.getSenderQueue(), JmsConstants.getResponseSelector(message.getMessageId()));

		log.debug("User({}) was " + (booleanResponseMessage.isOk() ? "not " : "") + "logged.");

		return booleanResponseMessage.isOk();
	}
}
