package cz.novros.cp.web.jms;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.UserService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.message.user.AddTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.ReadTrackingNumbersMessage;
import cz.novros.cp.jms.message.user.RemoveTrackingNumbersMessage;

@Service
@Profile("jms")
@Slf4j
public class UserJmsService extends AbstractJmsService implements UserService {

	@Autowired
	protected UserJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		log.debug("Adding tracking numbers to user({}).", username);

		final AddTrackingNumbersMessage message = new AddTrackingNumbersMessage();
		fillBasicInfo(message, username);
		message.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final TrackingNumbersMessage trackingNumbersMessage = recieveResponse(message);

		log.info("Tracking numbers to user({}) were " + (trackingNumbersMessage.isError() ? "not " : "") + "added.", username);

		return Arrays.asList(trackingNumbersMessage.getTrackingNumbers());
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final String[] trackingNumbers) {
		log.debug("Removing tracking numbers from user({}).", username);

		final RemoveTrackingNumbersMessage message = new RemoveTrackingNumbersMessage();
		fillBasicInfo(message, username);
		message.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final TrackingNumbersMessage trackingNumbersMessage = recieveResponse(message);

		log.info("Tracking numbers from user({}) were " + (trackingNumbersMessage.isError() ? "not " : "") + "removed.", username);

		return Arrays.asList(trackingNumbersMessage.getTrackingNumbers());
	}

	@Override
	public Collection<String> readTrackingNumbers(@Nonnull final String username) {
		log.debug("Reading all tracking numbers for user({}).", username);

		final ReadTrackingNumbersMessage message = new ReadTrackingNumbersMessage();
		fillBasicInfo(message, username);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_USER_QUEUE, message);
		final TrackingNumbersMessage trackingNumbersMessage = recieveResponse(message);

		log.debug("Read tracking numbers (size={}) for user({}).", trackingNumbersMessage.getTrackingNumbers().length, username);

		return Arrays.asList(trackingNumbersMessage.getTrackingNumbers());
	}
}
