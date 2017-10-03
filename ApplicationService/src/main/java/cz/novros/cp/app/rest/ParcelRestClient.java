package cz.novros.cp.app.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class ParcelRestClient extends AbstractRestClient implements ParcelService {

	@Autowired
	public ParcelRestClient(final RestTemplate restTemplate, @Value("${cp.database.parcel.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.PARCEL_SERVICE_ENDPOINT);
	}

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nullable final String[] trackingNumbers) {
		log.debug("Reading parcels with tracking numbers({}).", Arrays.toString(trackingNumbers));

		if (trackingNumbers == null || trackingNumbers.length == 0) {
			return ImmutableList.of();
		}

		final Parcel[] parcels = restTemplate.getForObject(getUrl(EndpointNames.READ_PARCELS_ENDPOINT) + "?numbers={numbers}", Parcel[].class, ImmutableMap.of("numbers", trackingNumbers));
		log.info("Parcels({}) with tracking numbers({}) were read.", parcels.length, Arrays.toString(trackingNumbers));

		return Arrays.asList(parcels);
	}

	@Nonnull
	@Override
	public Collection<Parcel> saveParcels(@Nullable final Collection<Parcel> parcels) {
		log.debug("Saving parcels({}) to database.", parcels.size());

		if (parcels == null || parcels.isEmpty()) {
			return ImmutableList.of();
		}

		final Parcel[] updatedParcels = restTemplate.postForObject(getUrl(EndpointNames.SAVE_PARCELS_ENDPOINT), parcels, Parcel[].class);
		log.info("Parcels({}) were saved.", updatedParcels.length);

		return Arrays.asList(updatedParcels);
	}

	@Override
	public void removeParcels(@Nullable final String[] trackingNumbers) {
		log.debug("Removing parcels with tracking numbers({}) from database.", Arrays.toString(trackingNumbers));

		if (trackingNumbers == null || trackingNumbers.length == 0) {
			return;
		}

		restTemplate.delete(getUrl(EndpointNames.REMOVE_PARCELS_ENDPOINT), Arrays.toString(trackingNumbers));
		log.info("Parcels with tracking numbers({}) were removed.", Arrays.toString(trackingNumbers));
	}
}
