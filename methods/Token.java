package methods;

/**
 * Base class for tokens: terminal symbols used in parsing.
 */
abstract class Token<T> {
	public enum TokenType {
		LeftParenthesis,
		RightParenthesis,
		Identifier,
		Integer,
		Float,
		String,
	}

	private final TokenType _type;
	private final T _value;

	/**
	 * Constructor that sets the token's type and value.
	 */
	public Token(TokenType type, T value) {
		_type = type;
		_value = value;
	}

	/**
	 * Returns the token's type.
	 */
	public TokenType getType() {
		return _type;
	}

	/**
	 * Returns the token's value.
	 */
	public T getValue() {
		return _value;
	}
}

class Token_LeftParenthesis extends Token<Void> {
	public Token_LeftParenthesis() {
		super(TokenType.LeftParenthesis, null);
	}
}

class Token_RightParenthesis extends Token<Void> {
	public Token_RightParenthesis() {
		super(TokenType.RightParenthesis, null);
	}
}

class Token_Identifier extends Token<String> {
	public Token_Identifier(String value) {
		super(TokenType.Identifier, value);
	}
}

class Token_Integer extends Token<Integer> {
	public Token_Integer(Integer value) {
		super(TokenType.Integer, value);
	}
}

class Token_Float extends Token<Float> {
	public Token_Float(Float value) {
		super(TokenType.Float, value);
	}
}

class Token_String extends Token<String> {
	public Token_String(String value) {
		super(TokenType.String, value);
	}
}
