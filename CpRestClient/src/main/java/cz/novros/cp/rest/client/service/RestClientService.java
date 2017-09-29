package cz.novros.cp.rest.client.service;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.rest.client.entity.Parcel;
import cz.novros.cp.rest.client.rest.RestApiClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientService {

	RestApiClient restClient;

	@Autowired
	public RestClientService(@Nonnull final RestApiClient restClient) {
		this.restClient = restClient;
	}

	@Nonnull
	public Collection<Parcel> getParcelHistory(@Nonnull final Collection<String> parcelIds) {
		return parcelIds.isEmpty() ? ImmutableList.of() : restClient.getParcelHistory(parcelIds);
	}
}
