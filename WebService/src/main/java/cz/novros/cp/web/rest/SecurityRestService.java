package cz.novros.cp.web.rest;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import cz.novros.cp.common.entity.User;
import cz.novros.cp.common.service.SecurityUserService;
import cz.novros.cp.rest.EndpointNames;
import cz.novros.cp.rest.service.AbstractRestClient;

@Service
@Profile("rest")
@Primary
@Slf4j
public class SecurityRestService extends AbstractRestClient implements SecurityUserService {

	@Autowired
	public SecurityRestService(final RestTemplate restTemplate, @Value("${cp.database.user.url}") final String serverUrl) {
		super(restTemplate, serverUrl, EndpointNames.SECURITY_SERVICE_ENDPOINT);
	}

	@Override
	public boolean registerUser(@Nonnull final User user) {
		return restTemplate.postForObject(getUrl(EndpointNames.REGISTER_USER_ENDPOINT), user, Boolean.class);
	}

	@Override
	public boolean loginUser(@Nonnull final User user) {
		return restTemplate.postForObject(getUrl(EndpointNames.LOGIN_USER_ENDPOINT), user, Boolean.class);
	}
}
