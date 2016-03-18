package methods;

import java.text.ParseException;

/**
 * Performs lexical analysis on input text, transforming it into a sequence of terminal symbols.
 */
class Lexer {
	//Enumeration for States used in readParseSymbol NFA
	private enum State{
		Final,
		Initial,
		Identifier,
		Integer,
		Float,
		String,
		Sign,
	}

	private String _text = "\0";
	private FileLocation _location;
	private boolean _input_closed = false;

	/**
	 * Default constructor.
	 */
	public Lexer() {
		_location = new FileLocation(null);
	}

	/**
	 * Provides additional text to be read.
	 *
	 * @param text additional input text
	 */
	public void provide(String text) {
		if (!isInputClosed()) {
			_text = _text.substring(0, _text.length() - 1) + text + '\0';
		}
	}

	/**
	 * Marks the end of input: readNext can now return an EOF,
	 * and provide will discard input.
	 */
	public void closeInput() {
		_input_closed = true;
	}

	public boolean isInputClosed() {
		return _input_closed;
	}

	/**
	 * Parses the next terminal from the remaining input text.
	 *
	 * @return the ParseSymbol representing the parsed terminal symbol
	 *         returns null if the end of the input text has been safely reached
	 *         returns an EOF terminal instead of null if provideEOF() has been called
	 */
	public ParseSymbol readNext() throws ParseException {
		ParseSymbol terminal = null;
		int begin = 0, end = 0;

		State state = State.Initial;
		while (state != State.Final) {
			char c = _text.charAt(end++);
			switch (state) {
				// Initial State of NFA
				case Initial:
					// Final State for Left Parenthesis
					if (c == '(') {
						terminal = new ParseSymbol_LeftParenthesis();
						state = State.Final;
					}
					// Final State for Right Parenthesis
					else if (c == ')') {
						terminal = new ParseSymbol_RightParenthesis();
						state = State.Final;
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
					// End of text, transition to final state, return null (or EndOfFile if isInputClosed())
					else if (c == '\0') {
						--end; // Don't consume the null terminator
						if (isInputClosed()) {
							terminal = new ParseSymbol_EndOfFile();
						}
						state = State.Final;
					}
					else {
						throw new ParseException("Invalid character found", _location.getColumn() + 1);
					}
					break;

				// Intermediate state for string to handle everything within quotes
				case String:
					// Final State for Strings
					if (c == '"') {
						String value = _text.substring(begin + 1, end - 1); // Exclude quotes
						terminal = new ParseSymbol_String(value);
						state = State.Final;
					}
					else if (c == '\0') {
						throw new ParseException("Reached end-of-input while reading string", _location.getColumn() + end - 1);
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
						state = State.Final;
					}
					break;

				// Intermediate state to check for sign of number
				case Sign:
					//Move to Integer state if character after sign is valid
					if (c >= '0' && c <= '9') {
						state = State.Integer;
					}
					else {
						throw new ParseException("Invalid number", _location.getColumn() + 1);
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
						state = State.Final;
					}
					break;

				// Intermediate state to check for valid Floats
				case Float:
					//Final State for Floats
					if (!(c >= '0' && c <= '9')) {
						--end; // Don't consume this character
						float value = Float.parseFloat(_text.substring(begin, end)); // TODO handle overflow
						terminal = new ParseSymbol_Float(value);
						state = State.Final;
					}
					break;
			}
		}

		// Update the location data, and attach it to the terminal
		if (terminal != null) {
			terminal.setLocation(_location.copy());
			terminal.getLocation().skipColumns(begin);
		}
		_location.skipColumns(end);

		// Trim the input data of the used portion
		_text = _text.substring(end);

		return terminal;
	}
}
