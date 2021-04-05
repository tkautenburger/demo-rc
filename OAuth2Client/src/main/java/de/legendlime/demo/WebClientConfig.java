package de.legendlime.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * OAuth2 web client security configuration for client credentials grant
 * @author Thomas Kautenburger
 *
 */
@EnableWebFluxSecurity
public class WebClientConfig {
	
	private static final String REGISTRATION_ID = "keycloak";

	/**
	 * Creating the authorized client manager to be used with the web client
	 * @param clientRegistrationRepository contains the oauth2 registration settings from application.yml
	 * @param authorizedClientService contains the authorized clients
	 * @return returns the authorized client manager to be used when creating the web client
	 */
	@Bean
	public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
			ReactiveClientRegistrationRepository clientRegistrationRepository,
			ReactiveOAuth2AuthorizedClientService authorizedClientService) {

		ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
				.builder().clientCredentials().build();

		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientService);
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;
	}

	/**
	 * Creating a REST web client with the authentication setting
	 * @param authorizedClientManager the authorized client manager bean
	 * @return the newly created web client to be used as REST client
	 */
	@Bean
	public WebClient webClient(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);
		// set the keycloak provider and registration settings as the default
		oauth.setDefaultClientRegistrationId(REGISTRATION_ID);
		return WebClient.builder().filter(oauth).build();
	}

}
