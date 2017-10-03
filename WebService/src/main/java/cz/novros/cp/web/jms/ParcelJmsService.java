package cz.novros.cp.web.jms;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.ReadParcelsMessage;
import cz.novros.cp.jms.message.parcel.RemoveParcelsMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;

@Service
@Profile("jms")
@Slf4j
public class ParcelJmsService extends AbstractJmsService implements ParcelService {

	@Autowired
	protected ParcelJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final String[] trackingNumbers) {
		log.debug("Reading parcels for tracking numbers({}).", Arrays.toString(trackingNumbers));

		final ReadParcelsMessage readParcelsMessage = new ReadParcelsMessage();
		fillBasicInfo(readParcelsMessage);
		readParcelsMessage.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, readParcelsMessage);
		final ParcelsMessage parcelsMessage = recieveResponse(readParcelsMessage);

		log.debug("Parcels(size={}) for tracking numbers({}) were read.", parcelsMessage.getParcels().size(), trackingNumbers);

		return parcelsMessage.getParcels();
	}

	@Nonnull
	@Override
	public Collection<Parcel> saveParcels(@Nonnull final Collection<Parcel> parcels) {
		log.debug("Saving parcels(count={}) to database.", parcels.size());

		final SaveParcelsMessage saveParcelsMessage = new SaveParcelsMessage();
		fillBasicInfo(saveParcelsMessage);
		saveParcelsMessage.setParcels(parcels);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, saveParcelsMessage);
		final ParcelsMessage parcelsMessage = recieveResponse(saveParcelsMessage);

		log.debug("Parcels(size={}) were saved.", parcelsMessage.getParcels().size());

		return parcelsMessage.getParcels();
	}

	@Override
	public void removeParcels(@Nonnull final String[] trackingNumbers) {
		log.debug("Removing parcels by tracking numbers({}).", Arrays.toString(trackingNumbers));

		final RemoveParcelsMessage removeParcelsMessage = new RemoveParcelsMessage();
		fillBasicInfo(removeParcelsMessage);
		removeParcelsMessage.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, removeParcelsMessage);

		log.debug("Parcels for tracking numbers({}) should be removed.", Arrays.toString(trackingNumbers));
	}
}
