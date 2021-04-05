package de.legendlime.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientCronJob {

    Logger logger = LoggerFactory.getLogger(WebClientCronJob.class);

    private static final String RESOURCE_URI = "http://localhost:8090/hello";

    @Autowired
    private WebClient webClient;

    @Scheduled(fixedRate = 10000)
    public void logResourceServiceResponse() {

        webClient.get()
            .uri(RESOURCE_URI)
            .retrieve()
            .bodyToMono(String.class)
            .map(string -> "We retrieved the following resource using Client Credentials Grant Type: " + string)
            .subscribe(logger::info);
    }
}
