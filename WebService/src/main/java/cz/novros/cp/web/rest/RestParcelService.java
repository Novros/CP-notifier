package cz.novros.cp.web.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Service;

import cz.novros.cp.jms.entity.Parcel;
import cz.novros.cp.web.service.ParcelService;

@Service
public class RestParcelService implements ParcelService {

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
		return null;
	}

	@Nonnull
	@Override
	public Collection<Parcel> refreshParcels(@Nonnull final Collection<String> trackingNumbers) {
		return null;
	}
}
