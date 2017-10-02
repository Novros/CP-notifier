package cz.novros.cp.database.parcel.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestService;

@RestController
@RequestMapping(EndpointNames.PARCEL_SERVICE_ENDPOINT)
@Primary
@Profile("rest")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ParcelRestService extends AbstractRestService implements ParcelService {

	cz.novros.cp.database.parcel.service.ParcelService parcelService;

	@Nonnull
	@Override
	@RequestMapping(value = EndpointNames.READ_PARCELS_ENDPOINT, method = RequestMethod.GET)
	public Collection<Parcel> readParcels(@RequestParam("numbers") @Nonnull final Collection<String> trackingNumbers) {
		log.debug("Reading parcels for tracking numbers({}).", trackingNumbers);

		final Collection<Parcel> parcels = parcelService.readParcels(trackingNumbers);

		log.debug("Parcels for tracking numbers({}) where read. (count={})", trackingNumbers, parcels.size());

		return parcels;
	}

	@Override
	@Nonnull
	@RequestMapping(value = EndpointNames.SAVE_PARCELS_ENDPOINT, method = RequestMethod.POST)
	public Collection<Parcel> saveParcels(@RequestBody @Nonnull final Collection<Parcel> parcels) {
		log.debug("Saving parcels to database(count={}).", parcels.size());

		final Collection<Parcel> savedParcels = parcelService.saveParcels(parcels);

		log.info("Parcels(count={}) were saved.", savedParcels.size());

		return savedParcels;
	}

	@Override
	@RequestMapping(value = EndpointNames.REMOVE_PARCELS_ENDPOINT, method = RequestMethod.DELETE)
	public void removeParcels(@RequestBody @Nonnull final Collection<String> trackingNumbers) {
		log.debug("Removing parcels from database(count={}).", trackingNumbers.size());

		parcelService.removeParcels(trackingNumbers);

		log.info("Parcels(count={}) were removed.", trackingNumbers.size());
	}
}
