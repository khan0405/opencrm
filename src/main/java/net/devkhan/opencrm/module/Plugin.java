package net.devkhan.opencrm.module;


public interface Plugin {
	void load(PluginManager pm);
	void onLoaded();
	void unload();
	void onUnloaded();
}
