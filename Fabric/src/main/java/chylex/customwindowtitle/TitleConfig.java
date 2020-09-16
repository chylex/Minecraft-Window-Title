package chylex.customwindowtitle;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TitleConfig{
	private static final Map<String, String> DEFAULTS = ImmutableMap.<String, String>builder()
		.put("title", "Minecraft {mcversion}")
		.build();
	
	public static TitleConfig read(final String folder){
		final Path configFile = Paths.get(folder, "customwindowtitle-client.toml");
		final Map<String, String> config = new HashMap<>(DEFAULTS);
		
		try{
			if (!Files.exists(configFile)){
				Files.write(configFile, config.entrySet().stream().map(entry -> String.format("%s = '%s'", entry.getKey(), entry.getValue())).collect(Collectors.toList()), StandardCharsets.UTF_8);
			}
			else{
				Files.readAllLines(configFile, StandardCharsets.UTF_8).stream().map(String::trim).filter(line -> !line.isEmpty()).forEach(line -> {
					final String[] split = line.split("=", 2);
					
					if (split.length != 2){
						throw new RuntimeException("CustomWindowTitle configuration has an invalid line: " + line);
					}
					
					final String key = split[0].trim();
					final String value = parseTrimmedValue(split[1].trim());
					
					if (config.containsKey(key)){
						config.put(key, value);
					}
					else{
						throw new RuntimeException("CustomWindowTitle configuration has an invalid key: " + key);
					}
				});
			}
		}catch(final IOException e){
			throw new RuntimeException("CustomWindowTitle configuration error", e);
		}
		
		return new TitleConfig(config.get("title"));
	}
	
	private static String parseTrimmedValue(String value){
		if (value.isEmpty()){
			return value;
		}
		
		final char surrounding = value.charAt(0);
		final int length = value.length();
		
		if (value.charAt(length - 1) == surrounding){
			value = value.substring(1, length - 1);
			
			if (surrounding == '"'){
				value = value.replace("\\\"", "\"").replace("\\\\", "\\");
			}
		}
		
		return value;
	}
	
	private final String title;
	
	private TitleConfig(final String title){
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
}
