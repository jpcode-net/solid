/*******************************************************
 * Copyright 2018 jpcode
 * contact http://www.jpcode.net/
 * 
 * --- solid
 * 
 ********************************************************/

package net.jpcode.solid;

import java.util.HashMap;
import java.util.Map;

import net.jpcode.solid.spi.Initialable;

/**
 * @author billy zhang
 * 
 */
public class ComponentFactory<T> {

	private Map<String, Class<? extends T>> classMap = new HashMap<>();
	private Map<String, T> componentMap = new HashMap<>();
	
	private Map<String, Map<String, Object>> config;
	
	public void setConfig(Map<String, Map<String, Object>> config) {
		this.config = config;
	}
	
	public void register(String name, Class<? extends T> componentClass) {
		classMap.put(name, componentClass);
	}

	public T getDefault() {
		if (classMap.containsKey("default")) {
			return getComponent("default");
		} 

		return null;
	}
	
	public T getComponent(String name) {
		if (componentMap.containsKey(name)) {
			return componentMap.get(name);
		}
		
		if (classMap.containsKey(name)) {
			Class<? extends T> clazz = classMap.get(name);
			try {
				T component = clazz.newInstance();
				componentMap.put(name, component);
				
				// TODO: Refactor
				if (component instanceof Initialable) {
					String key = clazz.getSimpleName().toLowerCase();
					if (config.containsKey(key)) {
						((Initialable)component).init(config.get(key));
					}
					
				}
				
				return component;
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return getDefault();
	}
	
}
