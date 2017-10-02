package cz.novros.cp.rest.service;

import javax.annotation.Nonnull;

import org.springframework.web.client.RestTemplate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * This class should extend all rest clients.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AllArgsConstructor
public abstract class AbstractRestClient {

	RestTemplate restTemplate;
	String serverUrl;
	String endpoint;

	protected String getUrl(@Nonnull final String action) {
		return serverUrl + endpoint + action;
	}

	protected String getUrlWithParam(@Nonnull final String action, @Nonnull final String paramName, @Nonnull final String value) {
		return serverUrl + endpoint + action.replace(paramName, value);
	}
}
