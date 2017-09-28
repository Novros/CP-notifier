package cz.novros.cp.database.dao;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

import cz.novros.cp.database.entity.Parcel;

public interface ParcelRepository extends CrudRepository<Parcel, Long> {

	Collection<Parcel> findByParcelTrackingNumberIn(@NonNull final Collection<String> parcelTracingNumbers);
}
