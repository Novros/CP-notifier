package cz.novros.cp.web.rest;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
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
public class UserRestClient extends AbstractRestClient implements UserService {

	@Autowired
	public UserRestClient(final RestTemplate restTemplate, @Value("${cp.database.user.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.USER_SERVICE_ENDPOINT);
	}

	@Nonnull
	@Override
	public Collection<String> addTrackingNumbers(@Nonnull final String username, @Nullable final String[] trackingNumbers) {
		log.info("Adding tracking numbers({}) to user({}).", username, trackingNumbers);

		if (trackingNumbers == null || trackingNumbers.length == 0) {
			return ImmutableList.of();
		}

		final Collection<String> numbers = Arrays.asList(restTemplate.postForObject(getUrl(EndpointNames.ADD_TRACKING_USER_ENDPOINT), trackingNumbers, String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
		log.info("Tracking numbers({}) of user({}) were updated.", numbers, username);

		return numbers;
	}

	@Nonnull
	@Override
	public Collection<String> removeTrackingNumbers(@Nonnull final String username, @Nullable final String[] trackingNumbers) {
		log.info("Remove tracking numbers({}) to user({}).", username, trackingNumbers);

		if (trackingNumbers == null || trackingNumbers.length == 0) {
			return ImmutableList.of();
		}

		final Collection<String> numbers = Arrays.asList(restTemplate.postForObject(getUrl(EndpointNames.REMOVE_TRACKING_USER_ENDPOINT), trackingNumbers, String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
		log.info("Tracking numbers({}) of user({}) were updated.", numbers, username);

		return numbers;
	}

	@Override
	public Collection<String> readTrackingNumbers(@Nonnull final String username) {
		log.debug("Reading tracking numbers of user({}).", username);

		final Collection<String> numbers = Arrays.asList(restTemplate.getForObject(getUrl(EndpointNames.READ_TRACKING_USER_ENDPOINT), String[].class, ImmutableMap.of(EndpointNames.USERNAME_PARAM, username)));
		log.info("Tracking numbers({}) of user({}) were read.", numbers, username);

		return numbers;
	}
}
