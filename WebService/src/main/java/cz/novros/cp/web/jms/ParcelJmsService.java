package cz.novros.cp.web.jms;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;
import cz.novros.cp.jms.message.parcel.ReadParcelsMessage;

@Service
@Profile("jms")
@Slf4j
public class ParcelJmsService extends AbstractJmsService implements ParcelService {

	protected ParcelJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
		log.debug("Reading parcels for tracking numbers({}).", trackingNumbers);

		final ReadParcelsMessage readParcelsMessage = new ReadParcelsMessage();
		fillBasicInfo(readParcelsMessage, "tracking");
		readParcelsMessage.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.DATABASE_PARCEL_QUEUE, readParcelsMessage);
		final ParcelsMessage parcelsMessage = (ParcelsMessage) jmsTemplate.receiveSelectedAndConvert(readParcelsMessage.getSenderQueue(), readParcelsMessage.getMessageId());

		log.debug("Parcels(size={}) for tracking numbers({}) were read.", parcelsMessage.getParcels().size(), trackingNumbers);

		return parcelsMessage.getParcels();
	}
}
