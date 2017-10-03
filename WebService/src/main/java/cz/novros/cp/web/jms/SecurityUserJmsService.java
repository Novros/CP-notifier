package cz.novros.cp.web.jms;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.User;
import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.reponse.BooleanResponseMessage;
import cz.novros.cp.jms.message.user.LoginUserMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;

@Service
@Profile("jms")
@Slf4j
public class SecurityUserJmsService extends AbstractJmsService implements SecurityUserService {

	@Autowired
	protected SecurityUserJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Override
	public boolean registerUser(@Nonnull final User user) {
		log.debug("Trying to register user({}).", user.getUsername());

		final RegisterUserMessage message = new RegisterUserMessage();
		fillBasicInfo(message, user.getUsername());
		message.setPassword(user.getPassword());

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final BooleanResponseMessage booleanResponseMessage = recieveResponse(message);

		log.debug("User({}) was " + (booleanResponseMessage.isOk() ? "not " : "") + "registered.");

		return booleanResponseMessage.isOk();
	}

	@Override
	public boolean loginUser(@Nonnull final User user) {
		log.debug("Trying to login user({}).", user.getUsername());

		final LoginUserMessage message = new LoginUserMessage();
		fillBasicInfo(message, user.getUsername());
		message.setPassword(user.getPassword());

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final BooleanResponseMessage booleanResponseMessage = recieveResponse(message);

		log.debug("User({}) was " + (booleanResponseMessage.isOk() ? "not " : "") + "logged.");

		return booleanResponseMessage.isOk();
	}
}
