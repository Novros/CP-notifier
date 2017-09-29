package cz.novros.cp.database.parcel.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.parcel.dao.ParcelRepository;
import cz.novros.cp.database.parcel.entity.Parcel;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParcelService {

	ParcelRepository parcelRepository;

	@Autowired
	public ParcelService(@Nonnull final ParcelRepository parcelRepository) {
		this.parcelRepository = parcelRepository;
	}

	@Nonnull
	public Collection<String> readAllTrackingNumbers() {
		final Iterable<Parcel> iterables = parcelRepository.findAll();
		if (iterables == null) {
			return new ArrayList<>();
		}

		return Collections2.transform(Lists.newArrayList(iterables), new Function<Parcel, String>() {
			@Nullable
			@Override
			public String apply(@Nullable final Parcel input) {
				return input.getParcelTrackingNumber();
			}
		});
	}

	@Nonnull
	public Collection<Parcel> findByTrackingNumber(@Nonnull final Collection<String> trackingNumber) {
		final Collection<Parcel> parcels = parcelRepository.findByParcelTrackingNumberIn(trackingNumber);
		return parcels == null ? ImmutableList.of() : parcels;
	}

	@Nullable
	public Iterable<Parcel> saveParcels(@Nonnull final Iterable<Parcel> parcels) {
		return parcelRepository.save(parcels);
	}
}
