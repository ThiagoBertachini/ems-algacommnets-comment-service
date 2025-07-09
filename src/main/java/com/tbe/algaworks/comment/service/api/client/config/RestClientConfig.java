package com.tbe.algaworks.comment.service.api.client.config;

import com.tbe.algaworks.comment.service.api.client.ModerationServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public ModerationServiceClient moderationServiceClient(RestClientFactory restClientFactory) {
        RestClient restClient = restClientFactory.createModerationServiceClient();
        RestClientAdapter adpter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factoryProxy = HttpServiceProxyFactory
                .builderFor(adpter).build();
        return factoryProxy.createClient(ModerationServiceClient.class);
    }
}
