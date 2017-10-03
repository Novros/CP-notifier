package cz.novros.cp.rest.client.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.CzechPostService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.client.service.CzechPostRestClientService;
import cz.novros.cp.rest.service.AbstractRestService;

@RestController
@RequestMapping(EndpointNames.CZECH_POST_SERVICE)
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CzechPostRestService extends AbstractRestService implements CzechPostService {

	CzechPostRestClientService czechPostRestClientService;

	@RequestMapping(EndpointNames.READ_PARCELS_ENDPOINT)
	@Override
	public Collection<Parcel> readParcels(@RequestParam("numbers") @Nonnull final String[] numbers) {
		log.info("Reading parcels with tracking numbers({}) from czech post rest api.", Arrays.toString(numbers));

		final Collection<Parcel> parcels = czechPostRestClientService.readParcels(numbers);

		log.info("Parcels(count={}) with tracking numbers({}) were read from czech post rest api.", parcels.size(), Arrays.toString(numbers));

		return parcels;
	}
}
