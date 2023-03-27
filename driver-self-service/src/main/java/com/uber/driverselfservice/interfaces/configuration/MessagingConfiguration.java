package com.uber.driverselfservice.interfaces.configuration;

import java.util.HashMap;
import java.util.Map;

import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingExpiredEvent;
import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingResponseEvent;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class MessagingConfiguration {
	@Value("${onboardingmanagement.tcpBrokerBindAddress}")
	private String brokerURL;

	@Value("${spring.activemq.user}")
	private String username;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setMessageConverter(jacksonJmsMessageConverter());
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerURL);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);
		connectionFactory.setTrustAllPackages(true);
		return connectionFactory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");

		final Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put("com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingResponseEvent", DriverOnboardingResponseEvent.class);
		typeIdMappings.put("com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingExpiredEvent", DriverOnboardingExpiredEvent.class);
		converter.setTypeIdMappings(typeIdMappings);
		return converter;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency("1-1");
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}
}