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
import cz.novros.cp.common.service.ApplicationService;
import cz.novros.cp.jms.QueueNames;
import cz.novros.cp.jms.message.application.RefreshParcelsMessage;
import cz.novros.cp.jms.message.parcel.ParcelsMessage;

@Service
@Profile("jms")
@Slf4j
public class ApplicationJmsService extends AbstractJmsService implements ApplicationService {

	@Autowired
	protected ApplicationJmsService(@Nonnull final JmsTemplate jmsTemplate) {
		super(jmsTemplate);
	}

	@Override
	public Collection<Parcel> refreshParcels(@Nonnull final String[] trackingNumbers) {
		log.debug("Refreshing parcels for tracking numbers({}).", Arrays.toString(trackingNumbers));

		final RefreshParcelsMessage readParcelsMessage = new RefreshParcelsMessage();
		fillBasicInfo(readParcelsMessage);
		readParcelsMessage.setTrackingNumbers(trackingNumbers);

		jmsTemplate.convertAndSend(QueueNames.APPLICATION_QUEUE, readParcelsMessage);
		final ParcelsMessage parcelsMessage = recieveResponse(readParcelsMessage);

		log.info("Parcels(size={}) for tracking numbers({}) were refreshed.", parcelsMessage.getParcels().size(), trackingNumbers);

		return parcelsMessage.getParcels();
	}
}
