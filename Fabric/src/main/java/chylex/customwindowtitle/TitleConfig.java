package chylex.customwindowtitle;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class TitleConfig{
	private static final Map<String, String> DEFAULTS;
	
	static{
		final Map<String, String> defaults = new LinkedHashMap<>();
		
		defaults.put("title", "Minecraft {mcversion}");
		defaults.put("icon16", "");
		defaults.put("icon32", "");
		
		DEFAULTS = Collections.unmodifiableMap(defaults);
	}
	
	public static TitleConfig read(final String folder){
		final Path configFile = Paths.get(folder, "customwindowtitle-client.toml");
		final Map<String, String> config = new LinkedHashMap<>(DEFAULTS);
		
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
		
		final String icon16 = config.get("icon16");
		final String icon32 = config.get("icon32");
		
		final Path pathIcon16 = icon16.isEmpty() ? null : Paths.get(folder, icon16);
		final Path pathIcon32 = icon32.isEmpty() ? null : Paths.get(folder, icon32);
		
		if ((pathIcon16 == null) != (pathIcon32 == null)){
			throw new RuntimeException("CustomWindowTitle configuration specifies only one icon, both 'icon16' and 'icon32' must be set.");
		}
		
		if (pathIcon16 != null && Files.notExists(pathIcon16)){
			throw new RuntimeException("CustomWindowTitle 16x16 icon not found: " + pathIcon16);
		}
		
		if (pathIcon32 != null && Files.notExists(pathIcon32)){
			throw new RuntimeException("CustomWindowTitle 32x32 icon not found: " + pathIcon32);
		}
		
		return new TitleConfig(config.get("title"), pathIcon16, pathIcon32);
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
	private final Path icon16;
	private final Path icon32;
	
	private TitleConfig(final String title, final Path icon16, final Path icon32){
		this.title = title;
		this.icon16 = icon16;
		this.icon32 = icon32;
	}
	
	public String getTitle(){
		return title;
	}
	
	public boolean hasIcon(){
		return icon16 != null && icon32 != null;
	}
	
	public InputStream readIcon16(){
		try{
			return Files.newInputStream(icon16, StandardOpenOption.READ);
		}catch(final IOException e){
			throw new RuntimeException("CustomWindowTitle could not open the specified 16x16 icon: " + icon16, e);
		}
	}
	
	public InputStream readIcon32(){
		try{
			return Files.newInputStream(icon32, StandardOpenOption.READ);
		}catch(final IOException e){
			throw new RuntimeException("CustomWindowTitle could not open the specified 32x32 icon: " + icon16, e);
		}
	}
}
