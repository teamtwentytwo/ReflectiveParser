package methods;

/**
 * Base class for parser symbols.
 */
abstract class ParseSymbol<T> {
	/**
	 * Enumeration of symbol types.
	 */
	public enum Type {
		// Terminal symbols
		EndOfFile (true),
		LeftParenthesis (true),
		RightParenthesis (true),
		Identifier (true),
		Integer (true),
		Float (true),
		String (true),

		// Non-terminal symbols
		Expression (false),
		ExpressionList (false);

		private final boolean _is_terminal;

		Type(boolean is_terminal) {
			_is_terminal = is_terminal;
		}

		public boolean isTerminal () {
			return _is_terminal;
		}
	}

	private final Type _type;
	private final T _value;

	/**
	 * Constructor that sets the token's type and value.
	 */
	public ParseSymbol(Type type, T value) {
		_type = type;
		_value = value;
	}

	/**
	 * Returns the token's type.
	 */
	public Type getType() {
		return _type;
	}

	/**
	 * Returns the token's value.
	 */
	public T getValue() {
		return _value;
	}
}

class ParseSymbol_EndOfFile extends ParseSymbol<Void> {
	public ParseSymbol_EndOfFile() {
		super(Type.EndOfFile, null);
	}
}

class ParseSymbol_LeftParenthesis extends ParseSymbol<Void> {
	public ParseSymbol_LeftParenthesis() {
		super(Type.LeftParenthesis, null);
	}
}

class ParseSymbol_RightParenthesis extends ParseSymbol<Void> {
	public ParseSymbol_RightParenthesis() {
		super(Type.RightParenthesis, null);
	}
}

class ParseSymbol_Identifier extends ParseSymbol<String> {
	public ParseSymbol_Identifier(String value) {
		super(Type.Identifier, value);
	}
}

class ParseSymbol_Integer extends ParseSymbol<Integer> {
	public ParseSymbol_Integer(Integer value) {
		super(Type.Integer, value);
	}
}

class ParseSymbol_Float extends ParseSymbol<Float> {
	public ParseSymbol_Float(Float value) {
		super(Type.Float, value);
	}
}

class ParseSymbol_String extends ParseSymbol<String> {
	public ParseSymbol_String(String value) {
		super(Type.String, value);
	}
}
