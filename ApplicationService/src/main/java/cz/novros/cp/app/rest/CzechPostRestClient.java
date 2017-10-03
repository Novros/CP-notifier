package cz.novros.cp.app.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.CzechPostService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CzechPostRestClient extends AbstractRestClient implements CzechPostService {

	@Autowired
	public CzechPostRestClient(final RestTemplate restTemplate, @Value("${cp.cp_service.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.CZECH_POST_SERVICE);
	}

	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
		log.debug("Reading parcels with tracking numbers({}) from czech post rest service.", trackingNumbers);

		final Parcel[] parcels = restTemplate.getForObject(getUrl(EndpointNames.READ_PARCELS_ENDPOINT) + "?numbers={numbers}", Parcel[].class, ImmutableMap.of("numbers", trackingNumbers));

		log.debug("Parcels(count={}) were read from czech post rest service.", parcels.length);

		return Arrays.asList(parcels);
	}
}
