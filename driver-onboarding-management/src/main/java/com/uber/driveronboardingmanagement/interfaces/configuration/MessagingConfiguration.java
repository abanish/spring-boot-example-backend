package com.uber.driveronboardingmanagement.interfaces.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestEvent;

/**
 * The MessagingConfiguration class configures the ActiveMQ message broker.
 *
 * */
@Configuration
public class MessagingConfiguration {
	@Value("${onboardingmanagement.stompBrokerBindAddress}")
	private String stompBrokerBindAddress;

	@Value("${onboardingmanagement.tcpBrokerBindAddress}")
	private String tcpBrokerBindAddress;

	@Value("${spring.activemq.user}")
	private String username;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public BrokerService broker() throws Exception {
		final BrokerService broker = new BrokerService();
		broker.addConnector(stompBrokerBindAddress);
		broker.addConnector(tcpBrokerBindAddress);
		broker.setPersistent(true);
		broker.setUseJmx(true);

		final Map<String, String> userPasswords = new HashMap<>();
		userPasswords.put(username, password);
		SimpleAuthenticationPlugin authenticationPlugin = new SimpleAuthenticationPlugin();
		authenticationPlugin.setUserPasswords(userPasswords);
		broker.setPlugins(new BrokerPlugin[] { authenticationPlugin });
		return broker;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");

		final Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put("com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestEvent", DriverOnboardingRequestEvent.class);
		converter.setTypeIdMappings(typeIdMappings);
		return converter;
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setTrustAllPackages(true);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);
		return connectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency("1-1");
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setMessageConverter(jacksonJmsMessageConverter());
		template.setConnectionFactory(connectionFactory());
		return template;
	}
}