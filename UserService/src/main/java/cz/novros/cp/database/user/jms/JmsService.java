package cz.novros.cp.database.user.jms;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.User;
import cz.novros.cp.database.user.service.UserSecurityService;
import cz.novros.cp.database.user.service.UserService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.message.reponse.BooleanResponseMessage;
import cz.novros.cp.jms.message.user.AddTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.LoginUserMessage;
import cz.novros.cp.jms.message.user.ReadTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;
import cz.novros.cp.jms.message.user.RemoveTrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;
import cz.novros.cp.jms.service.SecurityUserJmsService;
import cz.novros.cp.jms.service.UserJmsService;

@Service
@Profile("jms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JmsService extends AbstractJmsService implements UserJmsService, SecurityUserJmsService {

	UserService userService;
	UserSecurityService userSecurityService;

	@Autowired
	protected JmsService(@Nonnull final JmsTemplate jmsTemplate, final UserService userService, final UserSecurityService userSecurityService) {
		super(jmsTemplate, QueueNames.DATABASE_USER_QUEUE);

		this.userService = userService;
		this.userSecurityService = userSecurityService;
	}

	@JmsListener(destination = QueueNames.DATABASE_USER_QUEUE, containerFactory = "myFactory")
	public void receiveMessage(@Nonnull final AbstractJmsMessage message) {
		if (message instanceof RegisterUserMessage) {
			registerUser((RegisterUserMessage) message);
		} else if (message instanceof LoginUserMessage) {
			loginUser((LoginUserMessage) message);
		} else if (message instanceof AddTrackingNumbersMessage) {
			addTrackingNumbers((AddTrackingNumbersMessage) message);
		} else if (message instanceof RemoveTrackingNumbersMessage) {
			removeTrackingNumbers((RemoveTrackingNumbersMessage) message);
		} else if (message instanceof ReadTrackingNumbersMessage) {
			readTrackingNumbers((ReadTrackingNumbersMessage) message);
		} else {
			log.warn("Did not found any appropriate method for message: {}", message);
		}
	}

	@Override
	public void registerUser(@Nonnull final RegisterUserMessage message) {
		final String username = message.getUsername();
		log.debug("Creating user with username: {}", username);

		final boolean registered = userSecurityService.registerUser(new User(username, message.getPassword()));

		final BooleanResponseMessage responseMessage = new BooleanResponseMessage();
		responseMessage.setOk(registered);
		responseMessage.setError(!registered);

		log.error("User({}) was {} created!", username, registered ? "" : "not");

		sendResponse(message, responseMessage);
	}

	@Override
	public void loginUser(@Nonnull final LoginUserMessage message) {
		final String username = message.getUsername();
		log.debug("Trying to login user({}).", username);

		final boolean logged = userSecurityService.loginUser(new User(username, message.getPassword()));

		final BooleanResponseMessage responseMessage = new BooleanResponseMessage();
		responseMessage.setOk(logged);
		responseMessage.setError(!logged);

		log.error("User({}) was {} logged in!", username, logged ? "" : "not");

		sendResponse(message, responseMessage);
	}

	@Override
	public void addTrackingNumbers(@Nonnull final AddTrackingNumbersMessage message) {
		final String username = message.getUsername();
		log.debug("Adding tracking numbers to user({}).", username);

		final Collection<String> updatedTrackingNumbers = userService.addTrackingNumbers(username, message.getTrackingNumbers());

		final TrackingNumbersMessage trackingNumbersMessage = new TrackingNumbersMessage();
		trackingNumbersMessage.setTrackingNumbers(updatedTrackingNumbers.toArray(new String[updatedTrackingNumbers.size()]));

		log.info("Tracking numbers of user({}) where updated(operation add)!", username);

		sendResponse(message, trackingNumbersMessage);
	}

	@Override
	public void removeTrackingNumbers(@Nonnull final RemoveTrackingNumbersMessage message) {
		final String username = message.getUsername();
		log.debug("Removing tracking numbers to user({}).", username);

		final Collection<String> updatedTrackingNumbers = userService.removeTrackingNumbers(username, message.getTrackingNumbers());

		final TrackingNumbersMessage trackingNumbersMessage = new TrackingNumbersMessage();
		trackingNumbersMessage.setTrackingNumbers(updatedTrackingNumbers.toArray(new String[updatedTrackingNumbers.size()]));

		log.info("Tracking numbers of user({}) where updated(operation remove)!", username);

		sendResponse(message, trackingNumbersMessage);
	}

	@Override
	public void readTrackingNumbers(@Nonnull final ReadTrackingNumbersMessage message) {
		final String username = message.getUsername();
		log.debug("Reading tracking numbers for user({}).", username);

		final Set<String> numbers = userService.readTrackingNumbers(username);
		final TrackingNumbersMessage trackingNumbersMessage = new TrackingNumbersMessage();
		trackingNumbersMessage.setTrackingNumbers(numbers.toArray(new String[numbers.size()]));

		log.info("Tracking numbers (count={}) for user({}) where read!", numbers.size(), username);

		sendResponse(message, trackingNumbersMessage);
	}
}
