package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import cz.novros.cp.jms.message.application.RefreshParcelsMessage;

public interface ApplicationJmsService {

	void refreshParcels(@Nonnull final RefreshParcelsMessage message);
}
