package chylex.customwindowtitle;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TitleParser {
	private static final Pattern tokenRegex = Pattern.compile("\\{([a-z]+)(?::([^}]+))?}");
	private static final Logger logger = LogManager.getLogger("CustomWindowTitle");
	
	public static String parse(final String input) {
		final StringBuilder buffer = new StringBuilder();
		final Matcher matcher = tokenRegex.matcher(input);
		
		while (matcher.find()) {
			final String token = matcher.group(1);
			final String[] args = StringUtils.split(matcher.group(2), ',');
			
			String result = null;
			
			try {
				result = TitleTokens.getTokenFunction(token).apply(args == null ? ArrayUtils.EMPTY_STRING_ARRAY : args);
			} catch (final TokenException e) {
				logger.warn("Error processing token '" + token + "': " + e.getMessage());
			} catch (final Throwable t) {
				logger.warn("Error processing token '" + token + "': " + t.getMessage(), t);
			}
			
			if (result == null) {
				matcher.appendReplacement(buffer, input.substring(matcher.start(), matcher.end()));
			}
			else {
				matcher.appendReplacement(buffer, result);
			}
		}
		
		matcher.appendTail(buffer);
		return buffer.toString();
	}
	
	// Static class
	
	private TitleParser() {}
}
