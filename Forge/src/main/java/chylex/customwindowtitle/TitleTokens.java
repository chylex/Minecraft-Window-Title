package chylex.customwindowtitle;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class TitleTokens{
	
	// Registry
	
	private static final Map<String, Function<String[], String>> tokenMap = new HashMap<>();
	
	public static void registerToken(String token, Function<String[], String> processor){
		tokenMap.putIfAbsent(token, processor);
	}
	
	public static Function<String[], String> getTokenFunction(String token){
		return tokenMap.getOrDefault(token, args -> null);
	}
	
	// Arguments
	
	public static Function<String[], String> noArgs(Supplier<String> func){
		return args -> args.length > 0 ? fail("expected no arguments, got " + args.length) : func.get();
	}
	
	public static Function<String[], String> oneArg(UnaryOperator<String> func){
		return args -> args.length != 1 ? fail("expected 1 argument, got " + args.length) : func.apply(args[0]);
	}
	
	public static Function<String[], String> rangeArgs(int min, int max, Function<String[], String> func){
		return args -> args.length < min || args.length > max ? fail("expected between " + min + " and " + max + " arguments, got " + args.length) : func.apply(args);
	}
	
	private static String fail(String message){
		throw new TokenException(message);
	}
	
	// Static class
	
	private TitleTokens(){}
}
