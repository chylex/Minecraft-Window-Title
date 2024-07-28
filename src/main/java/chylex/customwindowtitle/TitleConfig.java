package chylex.customwindowtitle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TitleConfig {
	private static final Map<String, String> DEFAULTS;
	private static TitleConfig instance;

	static {
		final Map<String, String> defaults = new LinkedHashMap<>();
		defaults.put("title", "Minecraft {mcversion}");
		defaults.put("squareIcon", "");
		DEFAULTS = Collections.unmodifiableMap(defaults);
	}

	public static TitleConfig read(final String folder) {
		if (instance == null) {
			final Path configFile = Paths.get(folder, "customwindowtitle-client.toml");
			final Map<String, String> config = new LinkedHashMap<>(DEFAULTS);

			try {
				if (!Files.exists(configFile)) {
					Files.write(configFile, config.entrySet().stream()
							.map(entry -> String.format("%s = '%s'", entry.getKey(), entry.getValue()))
							.collect(Collectors.toList()), StandardCharsets.UTF_8);
				} else {
					Files.readAllLines(configFile, StandardCharsets.UTF_8).stream()
							.map(String::trim)
							.filter(line -> !line.isEmpty())
							.forEach(line -> {
								final String[] split = line.split("=", 2);

								if (split.length != 2) {
									throw new RuntimeException("CustomWindowTitle configuration has an invalid line: " + line);
								}

								final String key = split[0].trim();
								final String value = parseTrimmedValue(split[1].trim());

								if (config.containsKey(key)) {
									config.put(key, value);
								} else {
									throw new RuntimeException("CustomWindowTitle configuration has an invalid key: " + key);
								}
							});
				}
			} catch (final IOException e) {
				throw new RuntimeException("CustomWindowTitle configuration error", e);
			}

			final String iconPath = config.get("squareIcon");

			final Path pathIcon = iconPath.isEmpty() ? null : Paths.get(folder, iconPath);

			if (pathIcon != null && Files.notExists(pathIcon)) {
				throw new RuntimeException("CustomWindowTitle icon not found: " + pathIcon);
			}

			instance = new TitleConfig(config.get("title"), pathIcon);
		}
		return instance;
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

	public static TitleConfig getInstance() {
		return instance;
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
