package com.song.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertiesConfigFileUtil extends PropertyPlaceholderConfigurer{

	private static Map<String, String> ctxPropertiesMap; 
	 
	public static String readValue(String key) {
		return ctxPropertiesMap.get(key);
	}
	
	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		// TODO Auto-generated method stub
		super.processProperties(beanFactoryToProcess, props);
		
		ctxPropertiesMap = new HashMap<String, String>(); 
        for (Object key : props.keySet()) { 
            String keyStr = key.toString(); 
            String value = props.getProperty(keyStr); 
            ctxPropertiesMap.put(keyStr, value); 
        }   
	}
	
	
}
