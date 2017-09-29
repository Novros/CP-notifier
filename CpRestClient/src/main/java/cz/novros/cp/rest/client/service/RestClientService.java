package cz.novros.cp.rest.client.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import cz.novros.cp.rest.client.entity.Parcel;
import cz.novros.cp.rest.client.rest.RestApiClient;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientService {

	RestApiClient restClient;

	@Autowired
	public RestClientService(@NonNull final RestApiClient restClient) {
		this.restClient = restClient;
	}

	public Collection<Parcel> getParcelHistory(@NonNull final Collection<String> parcelIds) {
		if (parcelIds.isEmpty()) {
			return new ArrayList<>();
		}
	
		return restClient.getParcelHistory(parcelIds);
	}
}
