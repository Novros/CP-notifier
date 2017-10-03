package cz.novros.cp.common.service;

import java.util.Collection;

import javax.annotation.Nonnull;

import cz.novros.cp.common.entity.Parcel;

/**
 * This interface defines method over parcel service in CP system.
 */
public interface ParcelService {

	/**
	 * Read parcel for tracking numbers.
	 *
	 * @param trackingNumbers Tracking numbers for which will be parcels read.
	 *
	 * @return Parcels with given tracking numbers.
	 */
	@Nonnull
	Collection<Parcel> readParcels(@Nonnull final String[] trackingNumbers);

	/**
	 * Save parcels into database.
	 *
	 * @param parcels Parcels which will be saved.
	 *
	 * @return Return currently saved parcels.
	 */
	@Nonnull
	Collection<Parcel> saveParcels(@Nonnull final Collection<Parcel> parcels);

	/**
	 * Remove parcels from database.
	 *
	 * @param trackingNumbers Parcels which will be deleted.
	 */
	void removeParcels(@Nonnull final String[] trackingNumbers);
}
