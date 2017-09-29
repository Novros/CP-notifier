package cz.novros.cp.database.user.jms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.user.entity.User;
import cz.novros.cp.database.user.service.UserService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JmsService {

	private static final String DATABASE_USER_QUEUE = "database-user";
	private static final String SENDER_QUEUE = "sender";

	private static final String ACTION_IDENTIFIER = "action";
	private static final String REGISTER_USER_ACTION = "register";
	private static final String LOGIN_USER_ACTION = "login";
	private static final String ADD_TRACKING_ACTION = "tracking";

	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	private static final String TRACKING_KEY = "tracking";

	private static final String TRACKING_NUMBER_DELIMITER = ";";

	private static final Logger log = LoggerFactory.getLogger(JmsService.class);

	UserService userService;
	JmsTemplate jmsTemplate;

	public JmsService(@NonNull final UserService userService, final JmsTemplate jmsTemplate) {
		this.userService = userService;
		this.jmsTemplate = jmsTemplate;
	}

	@JmsListener(destination = DATABASE_USER_QUEUE, containerFactory = "myFactory")
	public void receiveMessage(@NonNull final Map<String, String> message) {
		switch (message.get(ACTION_IDENTIFIER)) {
			case REGISTER_USER_ACTION:
				registerUser(message);
				break;
			case LOGIN_USER_ACTION:
				loginUser(message);
				break;
			case ADD_TRACKING_ACTION:
				addTrackingNumbers(message);
				break;
			default:
				log.warn("No action was defined!");
				break;
		}
	}

	private void registerUser(@NonNull final Map<String, String> message) {
		final String sender = message.get(SENDER_QUEUE);
		final String email = message.get(USERNAME_KEY);
		log.debug("Creating user with email: {}", email);

		final User user = userService.createUser(email, message.get(PASSWORD_KEY));
		if (user == null) {
			log.error("User({}) was not created!", email);
			sendResponse(message, false);
		} else {
			log.info("User with email[{}] was created.", email);
			sendResponse(message, true);
		}
	}

	private void loginUser(@NonNull final Map<String, String> message) {
		final String email = message.get(USERNAME_KEY);
		log.debug("Trying to login user({}).", email);

		final User user = userService.loginUser(email, message.get(PASSWORD_KEY));

		if (user == null) {
			log.error("User({}) was not logged in!", email);
			sendResponse(message, false);
		} else {
			log.debug("User({}) was successfully logged.", email);
			sendResponse(message, true);
		}
	}

	private void addTrackingNumbers(@NonNull final Map<String, String> message) {
		final String email = message.get(USERNAME_KEY);
		log.debug("Adding tracking numbers to user({}).", email);

		final Set<String> trackingNumbers = new HashSet<>();
		trackingNumbers.addAll(Arrays.asList(message.get(TRACKING_KEY).split(TRACKING_NUMBER_DELIMITER)));

		final User user = userService.addTrackingNumbers(email, trackingNumbers);

		if (user == null) {
			log.error("Could not add tracking numbers to user({})!", email);
			sendResponse(message, false);
		} else {
			log.info("Tracking numbers (count={}) to user({}) where added!", trackingNumbers.size(), email);
			sendResponse(message, true);
		}
	}

	private void sendResponse(@NonNull final Map<String, String> requestMessage, @NonNull final Object response) {
		jmsTemplate.convertAndSend(requestMessage.get(SENDER_QUEUE),
				response,
				message -> {
					message.setJMSCorrelationID(requestMessage.get(USERNAME_KEY));
					return message;
				});
	}
}
