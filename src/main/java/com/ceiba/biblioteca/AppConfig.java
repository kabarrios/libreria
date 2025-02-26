//package com.ceiba.biblioteca;
//
//import io.netty.channel.ChannelOption;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//import reactor.netty.tcp.TcpClient;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    public WebClient webClient() {
//        TcpClient timeoutClient = TcpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000);
//        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.from(timeoutClient))).build();
//    }
//}
