package cz.novros.cp.web.rest;

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

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.service.UserService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class UserRestService extends AbstractRestClient implements UserService {

	@Autowired
	public UserRestService(final RestTemplate restTemplate, @Value("${cp.database.user.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.USER_SERVICE_ENDPOINT);
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		return Arrays.asList(restTemplate.postForObject(getUrl(EndpointNames.ADD_TRACKING_USER_ENDPOINT), trackingNumbers, String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nonnull final Collection<String> trackingNumbers) {
		return Arrays.asList(restTemplate.postForObject(getUrl(EndpointNames.REMOVE_TRACKING_USER_ENDPOINT), trackingNumbers, String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
	}

	@Override
	public Collection<String> readTrackingNumbers(@Nonnull final String username) {
		return Arrays.asList(restTemplate.getForObject(getUrl(EndpointNames.READ_TRACKING_USER_ENDPOINT), String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
	}
}
