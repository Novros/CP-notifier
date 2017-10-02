package cz.novros.cp.jms.service;

import javax.annotation.Nonnull;

import cz.novros.cp.jms.message.parcel.TrackingNumbersMessage;

/**
 * Interface which defines method over Czech post rest api.
 */
public interface CzechPostJmsService {

	/**
	 * Read parcels by tracking numbers.
	 *
	 * @param trackingNumbers Message with tracking numbers to read.
	 */
	void readParcels(@Nonnull final TrackingNumbersMessage trackingNumbers);
}
