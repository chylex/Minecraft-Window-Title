package chylex.customwindowtitle;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TitleConfig {
	private static final String KEY_TITLE = "title";
	private static final String KEY_ICON = "icon";
	
	private static final ImmutableMap<String, String> DEFAULTS = ImmutableMap.<String, String>builder()
		.put(KEY_TITLE, "Minecraft {mcversion}")
		.put(KEY_ICON, "")
		.buildOrThrow();
	
	private static volatile TitleConfig instance;
	
	public static TitleConfig getInstance() {
		return instance;
	}
	
	public static TitleConfig load(String folder) {
		if (instance != null) {
			throw new IllegalStateException("TitleConfig has already been loaded and cannot be loaded again.");
		}
		
		if (instance == null) {
			synchronized(TitleConfig.class) {
				if (instance == null) {
					instance = loadImpl(folder);
				}
			}
		}
		
		return instance;
	}
	
	private static TitleConfig loadImpl(String folder) {
		Path configFile = Paths.get(folder, "customwindowtitle-client.toml");
		Map<String, String> config = new LinkedHashMap<>(DEFAULTS);
		
		try {
			if (!Files.exists(configFile)) {
				Files.write(configFile, config.entrySet().stream()
					.map(entry -> String.format("%s = '%s'", entry.getKey(), entry.getValue()))
					.collect(Collectors.toList()), StandardCharsets.UTF_8);
			}
			else {
				Files.readAllLines(configFile, StandardCharsets.UTF_8).stream()
					.map(String::trim)
					.filter(line -> !line.isEmpty())
					.forEach(line -> {
						String[] split = line.split("=", 2);
						
						if (split.length != 2) {
							throw new RuntimeException("CustomWindowTitle configuration has an invalid line: " + line);
						}
						
						String key = split[0].trim();
						String value = parseTrimmedValue(split[1].trim());
						
						if (config.containsKey(key)) {
							config.put(key, value);
						}
						else {
							throw new RuntimeException("CustomWindowTitle configuration has an invalid key: " + key);
						}
					});
			}
		} catch (IOException e) {
			throw new RuntimeException("CustomWindowTitle configuration error", e);
		}
		
		String iconPathStr = config.get(KEY_ICON);
		Path iconPath = iconPathStr.isEmpty() ? null : Paths.get(folder, iconPathStr);
		if (iconPath != null && Files.notExists(iconPath)) {
			throw new RuntimeException("CustomWindowTitle configuration points to a missing icon: " + iconPath);
		}
		
		return new TitleConfig(config.get(KEY_TITLE), iconPath);
	}
	
	private static String parseTrimmedValue(String value) {
		if (value.isEmpty()) {
			return value;
		}
		
		final char surrounding = value.charAt(0);
		final int length = value.length();
		
		if (value.charAt(length - 1) == surrounding) {
			value = value.substring(1, length - 1);
			
			if (surrounding == '"') {
				value = value.replace("\\\"", "\"").replace("\\\\", "\\");
			}
		}
		
		return value;
	}
	
	private final String title;
	private final Path icon;
	
	private TitleConfig(final String title, final Path icon) {
		this.title = title;
		this.icon = icon;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean hasIcon() {
		return icon != null;
	}
	
	public Path getIconPath() {
		return icon;
	}
}
