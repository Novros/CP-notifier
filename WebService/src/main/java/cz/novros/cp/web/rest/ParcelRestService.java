package cz.novros.cp.web.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

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
public class ParcelRestService extends AbstractRestClient implements ParcelService {

	@Autowired
	public ParcelRestService(final RestTemplate restTemplate, @Value("${cp.database.parcel.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.PARCEL_SERVICE_ENDPOINT);
	}

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
		if (trackingNumbers.isEmpty()) {
			return ImmutableList.of();
		}

		return Arrays.asList(restTemplate.getForObject(getUrl(EndpointNames.READ_PARCELS_ENDPOINT) + "?numbers={numbers}", Parcel[].class,
				ImmutableMap.of("numbers", trackingNumbers)));
	}

	@Nonnull
	@Override
	public Collection<Parcel> saveParcels(@Nonnull final Collection<Parcel> parcels) {
		if (parcels.isEmpty()) {
			return ImmutableList.of();
		}

		return Arrays.asList(restTemplate.postForObject(getUrl(EndpointNames.SAVE_PARCELS_ENDPOINT), parcels, Parcel[].class));
	}

	@Override
	public void removeParcels(@Nonnull final Collection<String> trackingNumbers) {
		if (!trackingNumbers.isEmpty()) {
			restTemplate.delete(getUrl(EndpointNames.REMOVE_PARCELS_ENDPOINT), trackingNumbers); // FIXME
		}
	}
}
