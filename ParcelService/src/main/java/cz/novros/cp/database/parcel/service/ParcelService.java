package cz.novros.cp.database.parcel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.database.parcel.dao.ParcelRepository;
import cz.novros.cp.database.parcel.entity.Parcel;
import cz.novros.cp.database.parcel.jms.EntityConverter;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ParcelService implements cz.novros.cp.common.service.ParcelService {

	ParcelRepository parcelRepository;

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
	@Override
	public Collection<cz.novros.cp.common.entity.Parcel> readParcels(@Nonnull final String[] trackingNumbers) {
		final Collection<Parcel> databaseParcels = parcelRepository.findByParcelTrackingNumberIn(Arrays.asList(trackingNumbers));
		return databaseParcels == null ? ImmutableList.of() : EntityConverter.convertParcelToCommon(databaseParcels);
	}

	@Nonnull
	@Override
	public Collection<cz.novros.cp.common.entity.Parcel> saveParcels(@Nonnull final Collection<cz.novros.cp.common.entity.Parcel> parcels) {
		Iterable<Parcel> databaseParcels = EntityConverter.convertParcelFromCommon(parcels);
		databaseParcels = parcelRepository.save(databaseParcels);
		return databaseParcels == null ? ImmutableSet.of() : EntityConverter.convertParcelToCommon(databaseParcels);
	}

	@Override
	public void removeParcels(@Nonnull final String[] trackingNumbers) {
		for (final String tracking : trackingNumbers) {
			parcelRepository.delete(tracking);
		}
	}
}
