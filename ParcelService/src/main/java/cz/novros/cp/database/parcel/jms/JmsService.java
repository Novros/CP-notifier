package cz.novros.cp.database.parcel.jms;

import static cz.novros.cp.jms.QueueNames.DATABASE_PARCEL_QUEUE;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.database.parcel.service.ParcelService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.AbstractJmsMessage;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.ReadAllTrackingNumbersMessage;
import cz.novros.cp.jms.message.parcel.ReadParcelsMessage;
import cz.novros.cp.jms.message.parcel.RemoveParcelsMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;
import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;
import cz.novros.cp.jms.service.AbstractJmsService;
import cz.novros.cp.jms.service.ParcelJmsService;

@Service
@Profile("jms")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JmsService extends AbstractJmsService implements ParcelJmsService {

	ParcelService parcelService;

	@Autowired
	public JmsService(@Nonnull final ParcelService parcelService, final JmsTemplate jmsTemplate) {
		super(jmsTemplate, QueueNames.DATABASE_PARCEL_QUEUE);
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
		} else if (message instanceof RemoveParcelsMessage) {
			removeParcels((RemoveParcelsMessage) message);
		} else {
			log.warn("Did not found any appropriate method for message: {}", message);
		}
	}

	public void readParcels(@Nonnull final ReadParcelsMessage message) {
		log.debug("Reading parcels by tracking number.");

		final Collection<Parcel> parcels = parcelService.readParcels(message.getTrackingNumbers());
		final ParcelsMessage parcelsMessage = new ParcelsMessage();

		if (parcels.isEmpty()) {
			log.error("There was problem with reading parcels!");
			parcelsMessage.setError(true);
			parcelsMessage.setParcels(ImmutableList.of());
		} else {
			log.debug("Parcels were successfully read.");
			parcelsMessage.setParcels(parcels);
		}

		sendResponse(message, parcelsMessage);
	}

	public void saveParcels(@Nonnull final SaveParcelsMessage message) {
		log.debug("Saving parcels to database (count={})", message.getParcels().size());

		final Collection<Parcel> parcels = parcelService.saveParcels(message.getParcels());
		final ParcelsMessage parcelsMessage = new ParcelsMessage();

		if (parcels.isEmpty()) {
			log.error("There was problem with saving parcels!!");
			parcelsMessage.setError(true);
			parcelsMessage.setParcels(ImmutableList.of());
		} else {
			log.info("Parcels where successfully written.");
			parcelsMessage.setParcels(parcels);
		}

		sendResponse(message, parcelsMessage);
	}

	@Override
	public void removeParcels(@Nonnull final RemoveParcelsMessage message) {
		log.debug("Remove parcels({}) from database.", Arrays.toString(message.getTrackingNumbers()));

		parcelService.removeParcels(message.getTrackingNumbers());
		// FIXME
//		final ParcelsMessage parcelsMessage = new ParcelsMessage();
//
//		if (parcels.isEmpty()) {
//			log.error("There was problem with saving parcels!!");
//			parcelsMessage.setError(true);
//			parcelsMessage.setParcels(ImmutableList.of());
//		} else {
		log.info("Parcels where successfully written.");
//		}
//
//		sendResponse(message, parcelsMessage);
	}

	// TODO Remove?
	public void readAllTracking(@Nonnull final ReadAllTrackingNumbersMessage message) {
		log.debug("Reading all tracking numbers.");

		final Collection<String> trackingNumbers = parcelService.readAllTrackingNumbers();
		final TrackingNumbersMessage parcelsMessage = new TrackingNumbersMessage();

		if (trackingNumbers.isEmpty()) {
			log.warn("Database is empty!");
			parcelsMessage.setError(true);
			parcelsMessage.setTrackingNumbers(new String[]{});
		} else {
			log.debug("All tracking numbers were successfully read.");
			parcelsMessage.setTrackingNumbers(trackingNumbers.toArray(new String[trackingNumbers.size()]));
		}

		sendResponse(message, parcelsMessage);
	}
}
