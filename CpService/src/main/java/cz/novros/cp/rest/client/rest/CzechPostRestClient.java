package cz.novros.cp.rest.client.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.rest.client.entity.Parcel;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CzechPostRestClient {

	private static final int MAXIMAL_COUNT_OF_PARCELS = 10;
	private static final String PARCEL_HISTORY_PATH = "https://b2c.cpost.cz/services/ParcelHistory/getDataAsJson?idParcel=";
	private static final String JOINER = ";";

	RestTemplate restTemplate;

	@Autowired
	public CzechPostRestClient(@Nonnull final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Nonnull
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> parcelIds) {
		final Collection<Parcel> parcelHistories = new HashSet<>();

		partitionCollection(parcelIds, MAXIMAL_COUNT_OF_PARCELS)
				.forEach(ids -> parcelHistories.addAll(Arrays.asList(readParcelsForMaxN(ids))));

		return parcelHistories;
	}

	@Nonnull
	private Parcel[] readParcelsForMaxN(@Nonnull final Collection<String> parcelIds) {
		return restTemplate.getForObject(
				PARCEL_HISTORY_PATH + parcelIds.stream().collect(Collectors.joining(JOINER))
				, Parcel[].class);
	}

	@Nonnull
	private <T> Collection<Collection<T>> partitionCollection(final Collection<T> collection, final int size) {
		final Collection<Collection<T>> returnList = new ArrayList<>();
		final Collection<T> tenList = new HashSet<>(size);

		for (final T item : collection) {
			if (tenList.size() == size) {
				returnList.add(new ArrayList<>(tenList));
				tenList.clear();
			}

			tenList.add(item);
		}

		if (!tenList.isEmpty()) {
			returnList.add(tenList);
		}

		return returnList;
	}
}
