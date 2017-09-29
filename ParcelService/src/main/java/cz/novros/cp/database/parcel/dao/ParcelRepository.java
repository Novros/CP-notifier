package cz.novros.cp.database.parcel.dao;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;

import lombok.NonNull;

import cz.novros.cp.database.parcel.entity.Parcel;

public interface ParcelRepository extends CrudRepository<Parcel, String> {

	Collection<Parcel> findByParcelTrackingNumberIn(@NonNull final Collection<String> parcelTracingNumbers);
}
