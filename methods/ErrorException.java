public class ErrorException extends Exception {
	int location;
	public ErrorException(int location){
		this.location = location;
	}
	public String formatMessage(String input, String stackTrace, boolean verbose){
		int begin = stackTrace.indexOf(':');
		int end = stackTrace.indexOf('\n');
		String description = stackTrace.substring(begin + 2, end) + "at offSet" + location + "\n";
		String pointer = "";
		for (int i = 0; i < location; i++) {
			pointer = pointer + "-";	
		}
		pointer = pointer + "^\n";
		if (verbose){
			return (description + pointer + stackTrace);
		}else {
			return (description + pointer);
		}
	}
}
