package cz.novros.cp.web.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ApplicationService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class ApplicationRestClient extends AbstractRestClient implements ApplicationService {

	@Autowired
	public ApplicationRestClient(@Nonnull final RestTemplate restTemplate, @Value("${cp.application.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.APPLICATION_SERVICE_ENDPOINT);
	}

	@Override
	public Collection<Parcel> refreshParcels(@Nullable final String[] trackingNumbers) {
		log.debug("Refreshing parcels with tracking numbers({}).", Arrays.toString(trackingNumbers));

		if (trackingNumbers == null || trackingNumbers.length == 0) {
			return ImmutableList.of();
		}

		final Parcel[] parcels = restTemplate.postForObject(getUrl(EndpointNames.REFRESH_PARCEL_ENDPOINT), trackingNumbers, Parcel[].class);
		log.info("Parcels({}) with tracking numbers({}) where refreshed.", parcels.length, trackingNumbers);

		return Arrays.asList(parcels);
	}
}
