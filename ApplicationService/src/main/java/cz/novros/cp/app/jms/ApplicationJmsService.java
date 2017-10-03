package cz.novros.cp.app.jms;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.application.RefreshParcelsMessage;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Profile("jms")
@Slf4j
public class ApplicationJmsService extends AbstractJmsService implements cz.novros.cp.jms.service.ApplicationJmsService {

	@Autowired
	public ApplicationJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate, QueueNames.APPLICATION_QUEUE);
	}

	@JmsListener(destination = QueueNames.APPLICATION_QUEUE, containerFactory = "myFactory")
	public void listenMessageFromJms(@Nonnull final AbstractJmsMessage message) throws IOException {
		if (message instanceof RefreshParcelsMessage) {
			refreshParcels((RefreshParcelsMessage) message);
		} else {
			log.warn("Did not found any appropriate method for message: {}", message);
		}
	}

	@Override
	public void refreshParcels(@Nonnull final RefreshParcelsMessage message) {
		log.info("Reading parcels with tracking numbers({}) from czech post service.", message.getTrackingNumbers());

		final TrackingNumbersMessage trackingNumbersMessage = new TrackingNumbersMessage();
		fillBasicInfo(trackingNumbersMessage);
		trackingNumbersMessage.setTrackingNumbers(message.getTrackingNumbers());

		jmsTemplate.convertAndSend(QueueNames.REST_CP_QUEUE, trackingNumbersMessage);
		ParcelsMessage parcelsMessage = recieveResponse(trackingNumbersMessage);

		log.info("Parcels(count={}) with tracking numbers({}) were read from czech post service.", parcelsMessage.getParcels().size(), message.getTrackingNumbers());
		log.info("Saving updated parcels(count={}) to parcel service.", parcelsMessage.getParcels().size());

		final SaveParcelsMessage saveParcelsMessage = new SaveParcelsMessage();
		fillBasicInfo(saveParcelsMessage);
		saveParcelsMessage.setParcels(parcelsMessage.getParcels());

		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, parcelsMessage);
		parcelsMessage = recieveResponse(parcelsMessage);

		fillBasicInfo(parcelsMessage);

		log.info("Updated parcels(count={}) in parcel service and application.", parcelsMessage.getParcels().size());

		sendResponse(message, parcelsMessage);
	}
}
