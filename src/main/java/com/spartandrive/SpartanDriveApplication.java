package com.spartandrive;

import com.google.android.gcm.server.Sender;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class SpartanDriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpartanDriveApplication.class, args);
    }

    @Value("${gcm.apikey}")
    String gcmApiKey;
    @Value("${gcm.senderId}")
    String senderId;

    @Value("${es.hostName}")
    String esHostname;

    @Value("${es.port}")
    int esPort;

    @Bean
    public Sender gcmClient(){
        return new Sender(gcmApiKey);
    }

    @Bean
    public TransportClient elasticClient() throws UnknownHostException {
        return TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHostname),esPort));
    }

}