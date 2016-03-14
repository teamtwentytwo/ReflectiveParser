package methods;

/**
 * Parses input text using the Lexer class and an LL(1) grammar.
 */
class Parser {
	private Lexer _lexer;

	/**
	 * Default constructor.
	 */
	public Parser() {
		_lexer = new Lexer();
	}

	/**
	 * Constructor that provides some initial input text.
	 *
	 * @param text initial input text
	 */
	public Parser(String text) {
		_lexer = new Lexer(text);
	}


	/**
	 * Provides additional text to be read.
	 *
	 * @param text additional input text
	 */
	public void provide(String text) {
		_lexer.provide(text);
	}

	/**
	 * Performs the parse.
	 */
	public void parse() {
		// TODO
	}
};
