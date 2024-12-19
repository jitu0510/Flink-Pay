package com.rmgYantra.loginapp;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerConfig {

//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(org.apache.catalina.Context context) {
//                // Add customizations to the context here if needed
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
//        return tomcat;
//    }
//
//    private org.apache.catalina.connector.Connector createStandardConnector() {
//        org.apache.catalina.connector.Connector connector =
//                new org.apache.catalina.connector.Connector(org.apache.coyote.http11.Http11NioProtocol.class.getName());
//        connector.setScheme("http");
//        connector.setPort(8091);
//        connector.setSecure(false);
//        return connector;
//    }
}
