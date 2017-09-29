package cz.novros.cp.database.parcel.jms;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.database.parcel.entity.Parcel;
import cz.novros.cp.database.parcel.service.ParcelService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JmsService {

	private static final String DATABASE_PARCEL_QUEUE = "database-parcel";
	private static final String SENDER_QUEUE = "sender";
	private static final String SENDER_MESSAGE_IDENTIFIER = "msgId";

	private static final String ACTION_IDENTIFIER = "action";
	private static final String SAVE_PARCELS_ACTION = "save";
	private static final String READ_PARCELS_ACTION = "read";

	private static final String PARCELS_TRACKING_KEY = "trackingNumbers";
	private static final String PARCELS_KEY = "parcels";

	private static final String TRACKING_NUMBER_DELIMITER = ";";

	private static final ObjectMapper mapper = new ObjectMapper();

	ParcelService parcelService;
	JmsTemplate jmsTemplate;

	public JmsService(@NonNull final ParcelService parcelService, final JmsTemplate jmsTemplate) {
		this.parcelService = parcelService;
		this.jmsTemplate = jmsTemplate;
	}

	@JmsListener(destination = DATABASE_PARCEL_QUEUE, containerFactory = "myFactory")
	public void receiveMessage(@NonNull final Map<String, String> message) throws IOException {
		switch (message.get(ACTION_IDENTIFIER)) {
			case SAVE_PARCELS_ACTION:
				saveParcels(message);
				break;
			case READ_PARCELS_ACTION:
				readParcels(message);
				break;
			default:
				log.warn("No action was defined!");
				break;
		}
	}

	private void saveParcels(@NonNull final Map<String, String> message) throws IOException {
		Collection<Parcel> parcels = Arrays.asList(mapper.readValue(message.get(PARCELS_KEY), Parcel[].class));
		log.debug("Saving parcels to database (count={})", parcels.size());

		final Iterable<Parcel> parcelIterable = parcelService.saveParcels(parcels);
		if (parcelIterable == null || Iterables.isEmpty(parcelIterable)) {
			log.error("There was problem with saving parcels!!");
			sendResponse(message, "null");
		} else {
			log.info("Parcels where successfully written.");

			parcels = Lists.newArrayList(parcelIterable);
			sendResponse(message, mapper.writeValueAsString(parcels));
		}
	}

	private void readParcels(@NonNull final Map<String, String> message) throws JsonProcessingException {
		log.debug("Reading tracking numbers.");
		final Collection<String> trackingNumbers = Arrays.asList(message.get(PARCELS_TRACKING_KEY).split(TRACKING_NUMBER_DELIMITER));

		final Collection<Parcel> parcels = parcelService.findByTrackingNumber(trackingNumbers);
		if (parcels == null || parcels.isEmpty()) {
			log.error("There was problem with reading parcels!");
			sendResponse(message, mapper.writeValueAsString(parcels));
		} else {
			log.debug("Parcels were successfully read.");
			sendResponse(message, mapper.writeValueAsString(parcels));
		}
	}

	private void sendResponse(@NonNull final Map<String, String> requestMessage, @NonNull final Object response) {
		jmsTemplate.convertAndSend(requestMessage.get(SENDER_QUEUE),
				response,
				message -> {
					message.setJMSCorrelationID(requestMessage.get(SENDER_MESSAGE_IDENTIFIER));
					return message;
				});
	}
}
