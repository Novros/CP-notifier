package cz.novros.cp.web.rest;

import java.util.Collection;

import javax.annotation.Nonnull;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.Parcel;
import cz.novros.cp.common.service.ParcelService;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class ParcelRestService extends AbstractRestClient implements ParcelService {

	@Nonnull
	@Override
	public Collection<Parcel> readParcels(@Nonnull final Collection<String> trackingNumbers) {
		return null;
	}
}
