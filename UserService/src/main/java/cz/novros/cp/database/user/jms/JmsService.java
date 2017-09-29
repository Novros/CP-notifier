package cz.novros.cp.database.user.jms;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.user.entity.User;
import cz.novros.cp.database.user.service.UserService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.message.reponse.BooleanResponseMessage;
import cz.novros.cp.jms.message.user.AddTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.LoginUserMessage;
import cz.novros.cp.jms.message.user.ReadTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.RegisterUserMessage;
import cz.novros.cp.jms.service.AbstractJmsService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JmsService extends AbstractJmsService {

	private static final Logger log = LoggerFactory.getLogger(JmsService.class);

	UserService userService;

	@Autowired
	public JmsService(@Nonnull final UserService userService, final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
		this.userService = userService;
	}

	@JmsListener(destination = QueueNames.DATABASE_USER_QUEUE, containerFactory = "myFactory")
	public void receiveMessage(@Nonnull final AbstractJmsMessage message) {
		if (message instanceof RegisterUserMessage) {
			registerUser((RegisterUserMessage) message);
		} else if (message instanceof LoginUserMessage) {
			loginUser((LoginUserMessage) message);
		} else if (message instanceof AddTrackingNumbersMessage) {
			addTrackingNumbers((AddTrackingNumbersMessage) message);
		} else if (message instanceof ReadTrackingNumbersMessage) {
			readTrackingNumbers((ReadTrackingNumbersMessage) message);
		} else {
			log.warn("Did not found any appropriate method for message: {}", message);
		}
	}

	private void registerUser(@Nonnull final RegisterUserMessage message) {
		final String email = message.getUsername();
		log.debug("Creating user with email: {}", email);

		final User user = userService.createUser(email, message.getPassword());

		final BooleanResponseMessage responseMessage = new BooleanResponseMessage();
		if (user == null) {
			log.error("User({}) was not created!", email);
			responseMessage.setOk(false);
			responseMessage.setError(false);
		} else {
			log.info("User with email[{}] was created.", email);
			responseMessage.setOk(true);
		}

		sendResponse(message, responseMessage);
	}

	private void loginUser(@Nonnull final LoginUserMessage message) {
		final String email = message.getUsername();
		log.debug("Trying to login user({}).", email);

		final User user = userService.loginUser(email, message.getPassword());

		final BooleanResponseMessage responseMessage = new BooleanResponseMessage();
		if (user == null) {
			log.error("User({}) was not logged in!", email);
			responseMessage.setOk(false);
			responseMessage.setError(false);
		} else {
			log.debug("User({}) was successfully logged.", email);
			responseMessage.setOk(true);
		}

		sendResponse(message, responseMessage);
	}

	private void addTrackingNumbers(@Nonnull final AddTrackingNumbersMessage message) {
		final String email = message.getUsername();
		log.debug("Adding tracking numbers to user({}).", email);

		final Set<String> trackingNumbers = new HashSet<>(message.getTrackingNumbers().size());
		trackingNumbers.addAll(message.getTrackingNumbers());

		final User user = userService.addTrackingNumbers(email, trackingNumbers);

		final BooleanResponseMessage responseMessage = new BooleanResponseMessage();
		if (user == null) {
			log.error("Could not add tracking numbers to user({})!", email);
			responseMessage.setOk(false);
			responseMessage.setError(false);
		} else {
			log.info("Tracking numbers (count={}) to user({}) where added!", trackingNumbers.size(), email);
			responseMessage.setOk(true);
		}

		sendResponse(message, responseMessage);
	}

	private void readTrackingNumbers(@Nonnull final ReadTrackingNumbersMessage message) {
		final String email = message.getUsername();
		log.debug("Reading tracking numbers for user({}).", email);

		final Set<String> numbers = userService.readTrackingNumbers(email);
		final TrackingNumbersMessage trackingNumbersMessage = new TrackingNumbersMessage();
		trackingNumbersMessage.setTrackingNumbers(numbers);

		log.info("Tracking numbers (count={}) for user({}) where read!", numbers.size(), email);

		sendResponse(message, trackingNumbersMessage);
	}
}
