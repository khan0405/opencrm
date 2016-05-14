package net.devkhan.opencrm.module;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.HashMap;
import java.util.Map;

public class PluginManager implements InitializingBean, DisposableBean {
	private Resource pluginLocation;

	private Map<String, Plugin> plugins;

	public PluginManager(Resource pluginLocation) {
		this.pluginLocation = pluginLocation;
		plugins = new HashMap<>();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (pluginLocation == null) {
			pluginLocation = new ClassPathResource("../plugins");
		}
	}

	public void installPlugin(String pluginName) {

	}

	@Override
	public void destroy() throws Exception {
		plugins.values().forEach(Plugin::unload);
	}
}
