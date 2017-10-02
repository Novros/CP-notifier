package cz.novros.cp.rest.client.jms;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;
import cz.novros.cp.jms.service.CzechPostJmsService;
import cz.novros.cp.rest.client.service.CzechPostRestClientService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Profile("jms")
@Slf4j
public class JmsService extends AbstractJmsService implements CzechPostJmsService {

	CzechPostRestClientService restClientService;

	@Autowired
	public JmsService(@Nonnull final JmsTemplate jmsTemplate, final CzechPostRestClientService restClientService) {
		super(jmsTemplate);
		this.restClientService = restClientService;
	}

	@JmsListener(destination = QueueNames.REST_CP_QUEUE, containerFactory = "myFactory")
	public void listenMessageFromJms(@Nonnull final AbstractJmsMessage message) throws IOException {
		if (message instanceof TrackingNumbersMessage) {
			readParcels((TrackingNumbersMessage) message);
		} else {
			log.warn("Did not found any appropriate method for message: {}", message);
		}
	}

	@Override
	public void readParcels(@Nonnull final TrackingNumbersMessage message) {
		log.debug("Reading parcels with tracking number({}) from czech post rest api.", message);
		final Collection<Parcel> restParcels = restClientService.readParcels(message.getTrackingNumbers());

		final ParcelsMessage parcelsMessage = new ParcelsMessage();
		parcelsMessage.setParcels(restParcels);
		parcelsMessage.setError(false);
		sendResponse(message, parcelsMessage);

		log.info("Parcels from czech post rest api were read.");
	}
}
