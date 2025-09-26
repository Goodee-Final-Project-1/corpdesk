package com.goodee.corpdesk.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {
    /**
     * Creates a WebClient configured to use a URI builder that does not perform automatic URI encoding.
     *
     * <p>The returned instance uses a DefaultUriBuilderFactory with EncodingMode.NONE so provided URIs
     * are preserved as-is by the builder.</p>
     *
     * @return a WebClient instance that uses a DefaultUriBuilderFactory with encoding disabled
     */
    @Bean
    public WebClient webClient(){

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
            .uriBuilderFactory(factory)
            .build();
    }
}
