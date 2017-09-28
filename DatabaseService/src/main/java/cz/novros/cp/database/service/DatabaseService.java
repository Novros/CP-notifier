package cz.novros.cp.database.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.dao.ParcelRepository;
import cz.novros.cp.database.entity.Parcel;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseService {

	ParcelRepository parcelRepository;

	@Autowired
	public DatabaseService(@NonNull final ParcelRepository parcelRepository) {
		this.parcelRepository = parcelRepository;
	}

	public Iterable<Parcel> readAll() {
		return parcelRepository.findAll();
	}

	public Collection<Parcel> findByTrackingNumber(@NonNull final Collection<String> trackingNumber) {
		return parcelRepository.findByParcelTrackingNumberIn(trackingNumber);
	}
	
	public Iterable<Parcel> saveParcels(@NonNull final Iterable<Parcel> parcels) {
		return parcelRepository.save(parcels);
	}
}
