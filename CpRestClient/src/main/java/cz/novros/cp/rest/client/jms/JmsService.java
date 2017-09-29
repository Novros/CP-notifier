package cz.novros.cp.rest.client.jms;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.notification.UpdateParcelsMessage;
import cz.novros.cp.jms.message.parcel.ReadAllTrackingNumbersMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;
import cz.novros.cp.rest.client.service.RestClientService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JmsService extends AbstractJmsService {

	private static final String SAVE_PARCEL_ID = "saveParcels";
	private static final String READ_ALL_TRACKING_ID = "readAllTracking";

	RestClientService restClientService;

	@Autowired
	public JmsService(@Nonnull final JmsTemplate jmsTemplate, final RestClientService restClientService) {
		super(jmsTemplate);
		this.restClientService = restClientService;
	}

	@JmsListener(destination = QueueNames.REST_CP_QUEUE, containerFactory = "myFactory")
	public void listenMessageFromJms(@Nonnull final AbstractJmsMessage message) throws IOException {
		if (message instanceof UpdateParcelsMessage) {
			sendRequestForTrackingNumbers();
		} else if (message instanceof TrackingNumbersMessage) {
			updateParcels((TrackingNumbersMessage) message);
		}
	}

	private void updateParcels(@Nonnull final TrackingNumbersMessage message) throws IOException {
		log.debug("Updating parcels from cp rest api.");
		final Collection<cz.novros.cp.rest.client.entity.Parcel> restParcels = restClientService.getParcelHistory(message.getTrackingNumbers());

		final SaveParcelsMessage saveParcelsMessage = new SaveParcelsMessage();
		saveParcelsMessage.setSenderQueue(QueueNames.REST_CP_QUEUE);
		saveParcelsMessage.setMessageId(SAVE_PARCEL_ID + new Date().getTime());
		saveParcelsMessage.setParcels(EntityConverter.convertParcels(restParcels));

		log.debug("Sending parcels to save them into database..");
		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, saveParcelsMessage);

		log.info("Parcels where updated.");
	}

	private void sendRequestForTrackingNumbers() {
		log.debug("Sending request for all tracking numbers.");
		final String messageId = READ_ALL_TRACKING_ID + new Date().getTime();

		final ReadAllTrackingNumbersMessage message = new ReadAllTrackingNumbersMessage();
		message.setSenderQueue(QueueNames.REST_CP_QUEUE);
		message.setMessageId(messageId);

		log.trace("Sending message to read all tracking numbers.");
		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, message);

		log.debug("Request for all tracking numbers was sent.");
	}
}
