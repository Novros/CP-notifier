package cz.novros.cp.app.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.CzechPostService;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestService;

@RestController
@RequestMapping(EndpointNames.APPLICATION_SERVICE_ENDPOINT)
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApplicationRestService extends AbstractRestService implements cz.novros.cp.common.service.ApplicationService {

	CzechPostService czechPostService;
	ParcelService parcelService;

	@RequestMapping(value = EndpointNames.REFRESH_PARCEL_ENDPOINT, method = RequestMethod.POST)
	@Override
	public Collection<Parcel> refreshParcels(@RequestBody @Nonnull final String[] trackingNumbers) {
		log.info("Reading parcels with tracking numbers({}) from czech post service.", Arrays.toString(trackingNumbers));

		Collection<Parcel> parcels = czechPostService.readParcels(trackingNumbers);

		log.info("Parcels(count={}) with tracking numbers({}) were read from czech post service.", parcels.size(), Arrays.toString(trackingNumbers));
		log.info("Saving updated parcels(count={}) to parcel service.", parcels.size());

		parcels = parcelService.saveParcels(parcels);

		log.info("Updated parcels(count={}) in parcel service and application.", parcels.size());

		return parcels;
	}
}
