package cz.novros.cp.database.parcel.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.parcel.dao.ParcelRepository;
import cz.novros.cp.database.parcel.entity.Parcel;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParcelService {

	ParcelRepository parcelRepository;

	@Autowired
	public ParcelService(@NonNull final ParcelRepository parcelRepository) {
		this.parcelRepository = parcelRepository;
	}

	public Collection<Parcel> findByTrackingNumber(@NonNull final Collection<String> trackingNumber) {
		return parcelRepository.findByParcelTrackingNumberIn(trackingNumber);
	}

	public Iterable<Parcel> saveParcels(@NonNull final Iterable<Parcel> parcels) {
		return parcelRepository.save(parcels);
	}
}
