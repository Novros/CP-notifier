package cz.novros.cp.database.parcel.jms;

import static cz.novros.cp.jms.QueueNames.DATABASE_PARCEL_QUEUE;

import java.io.IOException;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.database.parcel.entity.Parcel;
import cz.novros.cp.database.parcel.service.ParcelService;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.ReadAllTrackingNumbersMessage;
import cz.novros.cp.jms.message.parcel.ReadParcelsMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JmsService extends AbstractJmsService {

	ParcelService parcelService;

	@Autowired
	public JmsService(@Nonnull final ParcelService parcelService, final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
		this.parcelService = parcelService;
	}

	@JmsListener(destination = DATABASE_PARCEL_QUEUE, containerFactory = "myFactory")
	private void listenMessageFromJms(@Nonnull final AbstractJmsMessage message) throws IOException {
		if (message instanceof SaveParcelsMessage) {
			saveParcels((SaveParcelsMessage) message);
		} else if (message instanceof ReadParcelsMessage) {
			readParcels((ReadParcelsMessage) message);
		} else if (message instanceof ReadAllTrackingNumbersMessage) {
			readAllTracking((ReadAllTrackingNumbersMessage) message);
		}
	}

	private void saveParcels(@Nonnull final SaveParcelsMessage message) throws IOException {
		log.debug("Saving parcels to database (count={})", message.getParcels().size());

		final Iterable<Parcel> parcelIterable = parcelService.saveParcels(EntityConverter.convertParcelFromJms(message.getParcels()));
		final ParcelsMessage parcelsMessage = new ParcelsMessage();

		if (parcelIterable == null || Iterables.isEmpty(parcelIterable)) {
			log.error("There was problem with saving parcels!!");
			parcelsMessage.setError(true);
			parcelsMessage.setParcels(ImmutableList.of());
		} else {
			log.info("Parcels where successfully written.");
			parcelsMessage.setParcels(EntityConverter.convertParcelToJms(Lists.newArrayList(parcelIterable)));
		}

		sendResponse(message, parcelsMessage);
	}

	private void readParcels(@Nonnull final ReadParcelsMessage message) throws JsonProcessingException {
		log.debug("Reading parcels by tracking number.");

		final Collection<Parcel> parcels = parcelService.findByTrackingNumber(message.getTrackingNumbers());
		final ParcelsMessage parcelsMessage = new ParcelsMessage();

		if (parcels.isEmpty()) {
			log.error("There was problem with reading parcels!");
			parcelsMessage.setError(true);
			parcelsMessage.setParcels(ImmutableList.of());
		} else {
			log.debug("Parcels were successfully read.");
			parcelsMessage.setParcels(EntityConverter.convertParcelToJms(parcels));
		}

		sendResponse(message, parcelsMessage);
	}

	private void readAllTracking(@Nonnull final ReadAllTrackingNumbersMessage message) {
		log.debug("Reading all tracking numbers.");

		final Collection<String> trackingNumbers = parcelService.readAllTrackingNumbers();
		final TrackingNumbersMessage parcelsMessage = new TrackingNumbersMessage();

		if (trackingNumbers.isEmpty()) {
			log.warn("Database is empty!");
			parcelsMessage.setError(true);
			parcelsMessage.setTrackingNumbers(ImmutableList.of());
		} else {
			log.debug("All tracking numbers were successfully read.");
			parcelsMessage.setTrackingNumbers(trackingNumbers);
		}

		sendResponse(message, parcelsMessage);
	}
}
