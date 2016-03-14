package methods;

/**
 * Turns text input into a series of Token objects.
 */
class Tokenizer {
	private String _text;
	//Enumeration for States used in readToken NFA
	private enum State{
			INITIAL,
			IDENTIFIER,
			INTEGER,
			FLOAT,
			STRING,
			SIGN
	}
	/**
	 * Default constructor.
	 */
	public Tokenizer() {
		_text = "";
	}

	/**
	 * Constructor that provides some initial input text.
	 *
	 * @param text initial input text
	 */
	public Tokenizer(String text) {
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
	 * Reads a token from the remaining input text.
	 *
	 * @return the Token object that was parsed from the text
	 */
	public Token readToken() {
		Token token = null;
		State state;
		int i = 0;
		int extraWS = 0;
		int state = 0;
		while (token == null && state >= 0) {
			char c = _text.charAt(i++);

			switch (state) {
				//Initial State of NFA
				case INITIAL:
					//Final State for Left Parenthesis
					if (c == '(') {
						token = new Token_LeftParenthesis();
					}
					//Final State for Right Parenthesis
					else if (c == ')') {
						token = new Token_RightParenthesis();
					}
					//Move to Intermediate State for checking String
					else if (c == '"') {
						state = STRING;
					}
					//Move to Intermediate State for checking Sign of number
					else if (c == '-' || c == '+'){
						state = SIGN;
					}
					//Self loop on Initial state for white spaces
					else if (Character.isWhitespace(c)){
						state = INITIAL;
						extraWS++;
					}
					//Move to Intermediate State for checking Identifiers
					else if (Character.isLetter(c)){
						state = IDENTIFIER;
					}
					//Move to Intermediate State for checking Numbers
					else if (Character.isDigit(c)){
						state = INTEGER;
					}
					//Empty Transition to final state, return null
					else if (c == 0) {
						state = -1; // Return null
					}
					else {
						state = INITIAL;
					}
					break;
				//Intermediate state for string to handle everyting within quotes	
				case STRING:
					//Final State for Strings
					if (c == '"') {
						String value = _text.substring(1 + extraWS, i - 1);
						token = new Token_String(value);
					}
					else if (c == 0) {
						throw new Exception("No Closing Quotes for String")
					}
					else {
						state = STRING;
					}
					break;
				//Intermediate state to check for valid Identifier
				case IDENTIFIER:
					if (!c.toString().matches("[a-zA-Z0-9_ ]")){
						throw new Exception("Invalid Identifier")
					}
					//Final State for Identifiers
					else if (Character.isWhitespace(c)){
						String value = _text.substring(extraWS, i - 1);
						token = new Token_Identifier(value);
					}
					else {
						state = IDENTIFIER;
					}
					break;
				//Intermediate state to check for sign of number
				case SIGN: 
					//Move to Integer state if character after sign is valid
					if (Character.isDigit(c)){
						state = INTEGER;
					}
					else{
						throw new Exception("Invalid Number");
					}
					break;
				//Intermediate state to check for valid Integers and Floats
				case INTEGER:
					if (!c.toString().matches("[0-9.) ]")){
						throw new Exception("Invalid Integer")
					}
					//If '.' appears, number is float, so move to Float State
					else if (c == '.'){
						state = FLOAT;
					}
					//Final State for Integers
					else if (Character.isWhitespace(c) || c == ')'){
						int value = Integer.parseInt(_text.substring(extraWS, i - 1));
						token = new Token_Integer(value);
						--i; //don't consume character
					}
					else {
						state = INTEGER;
					}
					break;
				//Intermediate state to check for valid Floats
				case FLOAT:
					if (!c.toString().matches("[0-9) ]")){
						throw new Exception("Invalid Float");
					}
					//Final State for Floats
					else if (Character.isWhitespace(c) || c == ')'){
						double value = double.parseDouble(_text.substring(extraWS, i - 1));
						token = new Token_Float(value);
						--i; //don't consume character
					}
					else {
						state = FLOAT;
					}
					break;
			}
		}

		_text = _text.substring(i);
		return token;
	}
}
