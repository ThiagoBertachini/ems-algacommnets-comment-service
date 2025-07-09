package com.tbe.algaworks.comment.service.api.client.config;

import com.tbe.algaworks.comment.service.api.client.exceptions.ModerationClientBadGatewayException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RestClientFactory {

    private final RestClient.Builder restClient;

    public RestClient createModerationServiceClient() {
        return restClient.baseUrl("http://localhost:8081")
/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Creates a {@link RestClient} instance configured to access the moderation service,
     * which is assumed to be running locally at <code>http://localhost:8081</code>.
     *
     * <p>This client is configured to treat any error status code as a
     * {@link ModerationClientBadGatewayException}.
     *
     * @return a configured {@link RestClient} instance
     */
/* <<<<<<<<<<  fd9efb1b-45f2-4e61-ad3e-9cceb732d742  >>>>>>>>>>> */
                .requestFactory(generateRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new ModerationClientBadGatewayException();
                }).build();
    }

    private ClientHttpRequestFactory generateRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5)); // timeout to connect
        requestFactory.setReadTimeout(Duration.ofSeconds(5)); // timeout to read, after connect
        return requestFactory;
    }
}
