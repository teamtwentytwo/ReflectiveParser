package methods;

import java.util.List;

/**
 * Base class for parser symbols.
 */
abstract class ParseSymbol<T> {
	// Enumeration of symbol types.
	public enum Type {
		// Terminal symbols
		EndOfFile (true),
		LeftParenthesis (true),
		RightParenthesis (true),
		Identifier (true),
		Integer (true),
		Float (true),
		String (true),

		// Action symbols
		Act_FunctionCall (false),
		Act_Value (false),
		Act_ExpressionList (false),
		Act_NewExpressionList (false),

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
	private FileLocation _location = null;

	public ParseSymbol(Type type, T value) {
		_type = type;
		_value = value;
	}

	public Type getType() {
		return _type;
	}

	public T getValue() {
		return _value;
	}

	public void setLocation(FileLocation location) {
		_location = location;
	}

	public FileLocation getLocation() {
		return _location;
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

class ParseSymbol_Expression extends ParseSymbol<Expression> {
	public ParseSymbol_Expression(Expression value) {
		super(Type.Expression, value);
	}
}

class ParseSymbol_ExpressionList extends ParseSymbol<List<Expression>> {
	public ParseSymbol_ExpressionList(List<Expression> value) {
		super(Type.ExpressionList, value);
	}
}
