package methods;

/**
 * Turns text input into a series of methods.Token objects.
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
	 * @return the methods.Token object that was parsed from the text
	 */
	public Token readToken() {
		// TODO
		return null;
	}
}
