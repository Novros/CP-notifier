package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import cz.novros.cp.jms.message.parcel.ReadParcelsMessage;
import cz.novros.cp.jms.message.parcel.RemoveParcelsMessage;
import cz.novros.cp.jms.message.parcel.SaveParcelsMessage;

public interface ParcelJmsService {

	void readParcels(@Nonnull final ReadParcelsMessage message);

	void saveParcels(@Nonnull final SaveParcelsMessage message);

	void removeParcels(@Nonnull final RemoveParcelsMessage message);
}
