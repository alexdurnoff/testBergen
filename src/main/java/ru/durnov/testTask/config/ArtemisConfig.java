package ru.durnov.testTask.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;


import java.util.Arrays;

@Configuration
@EnableJms
public class ArtemisConfig {
    /*@Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost");
        factory.setTrustedPackages(Arrays.asList("ru.durnov.requestbody"));
        return factory;
    }*/
}
