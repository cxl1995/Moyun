package com.mywl.app.platform.factory;

import com.mywl.app.platform.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 描述: 客户端工厂配置
 * </p>
 * 
 * @author aLiang
 * @date 2022年4月30日 下午10:21:34
 */
@Slf4j
public class MqttPahoClientFactorySetting {

	private MqttPahoClientFactorySettingCallback mqttPahoClientFactorySettingCallback;

	public MqttPahoClientFactorySetting(MqttPahoClientFactorySettingCallback mqttPahoClientFactorySettingCallback) {
		super();
		this.mqttPahoClientFactorySettingCallback = mqttPahoClientFactorySettingCallback;
	}

	/**
	 * 
	 * <p>
	 * 配置MqttPahoClientFactory
	 * </P>
	 * @param channelName 通道名
	 * @param config      配置信息
	 * @param isConsumer  是否是消费者
	 * @return
	 */
	public MqttPahoClientFactory mqttClientFactory(String channelName, MqttProperties.Config config, boolean isConsumer) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();

		options.setServerURIs(config.getUrl());
		options.setCleanSession(config.getCleanSession() == null ? true : config.getCleanSession());
		options.setKeepAliveInterval(config.getKepAliveInterval());
		options.setPassword(config.getPassword().toCharArray());
		options.setUserName(config.getUsername());
		options.setConnectionTimeout(config.getTimeout());
		options.setAutomaticReconnect(config.getAutomaticReconnect() == null ? true : config.getAutomaticReconnect());

		Integer mqttVersion = config.getMqttVersion();
		if (mqttVersion != null) {
			options.setMqttVersion(mqttVersion);
		}

		Integer maxInflight = config.getMaxInflight();
		if (maxInflight != null) {
			options.setMaxInflight(maxInflight);
		}

		MqttProperties.Will will = null;
		if (isConsumer && config.getConsumerWill() != null) {
			will = config.getConsumerWill();
		} else if (!isConsumer && config.getProducerWill() != null) {
			will = config.getProducerWill();
		}
		if (will != null) {
			try {
				options.setWill(will.getTopic(), will.getPayload().getBytes("utf-8"), will.getQos(),
						will.getRetained());
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
		}

		if (mqttPahoClientFactorySettingCallback != null) {
			mqttPahoClientFactorySettingCallback.callback(options, channelName, config, isConsumer);
		}

		factory.setConnectionOptions(options);
		return factory;
	}

	public MqttPahoClientFactorySettingCallback getMqttPahoClientFactorySettingCallback() {
		return mqttPahoClientFactorySettingCallback;
	}

	public void setMqttPahoClientFactorySettingCallback(
			MqttPahoClientFactorySettingCallback mqttPahoClientFactorySettingCallback) {
		this.mqttPahoClientFactorySettingCallback = mqttPahoClientFactorySettingCallback;
	}

}
