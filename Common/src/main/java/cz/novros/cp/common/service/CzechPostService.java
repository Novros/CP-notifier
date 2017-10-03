package cz.novros.cp.common.service;

import java.util.Collection;

import javax.annotation.Nonnull;

import cz.novros.cp.common.entity.Parcel;

/**
 * Interface which defines method over Czech post rest api.
 */
public interface CzechPostService {

	/**
	 * Read parcels by tracking numbers.
	 *
	 * @param trackingNumbers Collections of tracking numbers, which will be read from rest api.
	 *
	 * @return Collection of read parcels, it might not have some parcels, because they not exists.
	 */
	Collection<Parcel> readParcels(@Nonnull final String[] trackingNumbers);
}
