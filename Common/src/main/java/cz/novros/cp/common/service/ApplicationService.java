package cz.novros.cp.common.service;

import java.util.Collection;

import javax.annotation.Nonnull;

import cz.novros.cp.common.entity.Parcel;

/**
 * Interace which defines main application logic.
 */
public interface ApplicationService {

	/**
	 * Refresh parcels by tracking numbers.
	 *
	 * @param trackingNumbers Parcel which will be updated.
	 *
	 * @return Refreshed parcels.
	 */
	Collection<Parcel> refreshParcels(@Nonnull final String[] trackingNumbers);

}
