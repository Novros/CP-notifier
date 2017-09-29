package cz.novros.cp.rest.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.net.ssl.SSLContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.client.RestTemplate;

import lombok.NonNull;

import cz.novros.cp.rest.client.entity.Parcel;
import cz.novros.cp.rest.client.service.RestClientService;

@ComponentScan
@SpringBootApplication
@EnableJms
public class Application {

	private static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner run(@NonNull final RestClientService service, @NonNull final JmsTemplate jmsTemplate) throws Exception {
		return args -> {
			final Collection<Parcel> parcelCollection = service.getParcelHistory(Arrays.asList(args));
			final String parcels = mapper.writeValueAsString(parcelCollection);
			
			final Map<String, String> map = new HashMap<>();
			map.put("parcels", parcels);
			map.put("sender", "rest-cp");
			map.put("action", "save");
			map.put("msgId", "id2");

			jmsTemplate.convertAndSend("database-parcel", map);
			final String response = (String) jmsTemplate.receiveSelectedAndConvert("rest-cp", "JMSCorrelationID='" + "id2" + "'");
			System.out.println(response);
		};
	}

	@Bean
	public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		final TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		final SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();
		final SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
		final CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.build();
		final HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);

		return new RestTemplate(requestFactory);
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setErrorHandler(t -> System.err.println("An error has occurred in the message!"));
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}
