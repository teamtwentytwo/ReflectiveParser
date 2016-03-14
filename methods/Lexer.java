package methods;

/**
 * Performs lexical analysis on input text, transforming it into a sequence of terminal symbols.
 */
class Lexer {
	//Enumeration for States used in readParseSymbol NFA
	private enum State{
		Done,
		Initial,
		Identifier,
		Integer,
		Float,
		String,
		Sign,
	}

	private String _text;

	/**
	 * Default constructor.
	 */
	public Lexer() {
		_text = "";
	}

	/**
	 * Constructor that provides some initial input text.
	 *
	 * @param text initial input text
	 */
	public Lexer(String text) {
		_text = text;
	}

	/**
	 * Provides additional text to be read.
	 *
	 * @param text additional input text
	 */
	public void provide(String text) {
		_text += text;
	}

	/**
	 * Parses the next terminal from the remaining input text.
	 *
	 * @return the ParseSymbol representing the parsed terminal symbol
	 *         returns null if the end of the input text has been safely reached
	 */
	public ParseSymbol readNext() throws Exception {
		ParseSymbol terminal = null;
		State state = State.Initial;
		int begin = 0, end = 0;
		while (state != State.Done) {
			char c = _text.charAt(end++);
			switch (state) {
				// Initial State of NFA
				case Initial:
					// Final State for Left Parenthesis
					if (c == '(') {
						terminal = new ParseSymbol_LeftParenthesis();
						state = State.Done;
					}
					// Final State for Right Parenthesis
					else if (c == ')') {
						terminal = new ParseSymbol_RightParenthesis();
						state = State.Done;
					}
					// Move to Intermediate State for checking String
					else if (c == '"') {
						state = State.String;
					}
					// Move to Intermediate State for checking Sign of number
					else if (c == '-' || c == '+'){
						state = State.Sign;
					}
					// Self loop on Initial state for white spaces
					else if (c == ' ' || c == '\t') {
						++begin; // Skip this character
						state = State.Initial;
					}
					// Move to Intermediate State for checking Identifiers
					else if ((c >= 'A' && c <= 'Z')
							|| (c >= 'a' && c <= 'z')
							|| (c == '_')) {
						state = State.Identifier;
					}
					// Move to Intermediate State for checking Numbers
					else if (c >= '0' && c <= '9') {
						state = State.Integer;
					}
					// Empty Transition to final state, return null
					else if (c == '\0') {
						state = State.Done;
					}
					else {
						state = State.Initial;
					}
					break;

				// Intermediate state for string to handle everyting within quotes	
				case String:
					// Final State for Strings
					if (c == '"') {
						String value = _text.substring(begin + 1, end - 1); // Exclude quotes
						terminal = new ParseSymbol_String(value);
						state = State.Done;
					}
					else if (c == '\0' || c == '\n') {
						throw new Exception("No Closing Quotes for String");
					}
					break;

				// Intermediate state to check for valid Identifier
				case Identifier:
					//Final State for Identifiers
					if (!((c >= 'A' && c <= 'Z')
							|| (c >= 'a' && c <= 'z')
							|| (c >= '0' && c <= '9')
							|| (c == '_'))) {
						--end; // Don't consume this character
						String value = _text.substring(begin, end);
						terminal = new ParseSymbol_Identifier(value);
						state = State.Done;
					}
					break;

				// Intermediate state to check for sign of number
				case Sign:
					//Move to Integer state if character after sign is valid
					if (c >= '0' && c <= '9') {
						state = State.Integer;
					}
					else {
						throw new Exception("Invalid Number");
					}
					break;

				// Intermediate state to check for valid Integers and Floats
				case Integer:
					// If '.' appears, number is float, so move to Float State
					if (c == '.') {
						state = State.Float;
					}
					// Final State for Integers
					else if (!(c >= '0' && c <= '9')) {
						--end; // Don't consume this character
						int value = Integer.parseInt(_text.substring(begin, end)); // TODO handle overflow
						terminal = new ParseSymbol_Integer(value);
						state = State.Done;
					}
					break;

				// Intermediate state to check for valid Floats
				case Float:
					//Final State for Floats
					if (!(c >= '0' && c <= '9')) {
						--end; // Don't consume this character
						float value = Float.parseFloat(_text.substring(begin, end)); // TODO handle overflow
						terminal = new ParseSymbol_Float(value);
						state = State.Done;
					}
					break;
			}
		}

		_text = _text.substring(end);
		return terminal;
	}
}
