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

class TokenLeftParenthesis extends Token<Void> {
	public TokenLeftParenthesis() {
		super(TokenType.LeftParenthesis, null);
	}
}

class TokenRightParenthesis extends Token<Void> {
	public TokenRightParenthesis() {
		super(TokenType.RightParenthesis, null);
	}
}

class TokenIdentifier extends Token<String> {
	public TokenIdentifier(String value) {
		super(TokenType.Identifier, value);
	}
}

class TokenInteger extends Token<Integer> {
	public TokenInteger(Integer value) {
		super(TokenType.Integer, value);
	}
}

class TokenFloat extends Token<Float> {
	public TokenFloat(Float value) {
		super(TokenType.Float, value);
	}
}

class TokenString extends Token<String> {
	public TokenString(String value) {
		super(TokenType.String, value);
	}
}
