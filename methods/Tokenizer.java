package methods;

/**
 * Turns text input into a series of Token objects.
 */
class Tokenizer {
	private String _text;

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
		Token token;

		int i = 0;
		int state = 0;
		while (token == null && state >= 0) {
			char c = _text[i++];

			switch (state) {
				case 0:
					if (c == '(') {
						token = new Token_LeftParenthesis();
					}
					else if (c == ')') {
						token = new Token_RightParenthesis();
					}
					else if (c == '"') {
						state = 1;
					}
					else if (c == 0) {
						state = -1; // Return null
					}
					else {
						// TODO error
					}
					break;
				case 1:
					if (c == '"') {
						String value = _text.substring(1, i - 1);
						token = new Token_String(value);
					}
					else if (c == 0) {
						// TODO error
					}
					break;
				// TODO - match Identifier, Integer, Float
			}
		}

		_text = _text.substring(i);
		return token;
	}
}
