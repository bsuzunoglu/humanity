package com.ttaylorr.dev.humanity.server.configuration.providers;

import java.util.HashMap;
import java.util.Set;

import com.ttaylorr.dev.humanity.server.configuration.ConfigurationProvider;

public class ServerNormalConfigurationProvider implements ConfigurationProvider {

	private HashMap<String, String> data;
	private String file;

	public ServerNormalConfigurationProvider(String file) {
		this.file = file;
		data = new HashMap<>();
		readFromFile();
	}

	private void readFromFile() {
		throw new UnsupportedOperationException("ClientNormalConfigurationProvider not implemented yet.");
	}

	@Override
	public String getByKey(String key) {
		return data.get(key);
	}

	@Override
	public Set<String> getKeys() {
		return data.keySet();
	}

	@Override
	public boolean hasKey(String key) {
		return data.containsKey(key);
	}

	public void setMaxPlayers(int mplayers) {
		data.put(ConfigurationProvider.MAX_PLAYERS_KEY, mplayers + "");
	}

}
