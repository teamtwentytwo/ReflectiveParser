package methods;

import java.util.List;

abstract class Expression {
	/**
	 * Evaluates the expression and returns its value.
	 * Runtime type checking will be necessary to determine the value's type.
	 */
	public abstract Object evaluate();
}

class Expression_FunctionCall extends Expression {
	private ParseSymbol_LeftParenthesis _left_paren;
	private ParseSymbol_Identifier _identifier;
	private List<Expression> _arguments;
	private ParseSymbol_RightParenthesis _right_paren;

	public Expression_FunctionCall(
			ParseSymbol_LeftParenthesis left_paren,
			ParseSymbol_Identifier identifier,
			List<Expression> arguments,
			ParseSymbol_RightParenthesis right_paren) {
		_left_paren = left_paren;
		_identifier = identifier;
		_arguments = arguments;
		_right_paren = right_paren;
	}

	public Object evaluate() {
		// TODO - look up identifier
		// TODO - call function, pass arguments
		return null; // TODO - return
	}
}

class Expression_Integer extends Expression {
	private ParseSymbol_Integer _symbol;

	public Expression_Integer(ParseSymbol_Integer symbol) {
		_symbol = symbol;
	}

	public Object evaluate() {
		return _symbol.getValue();
	}
}

class Expression_Float extends Expression {
	private ParseSymbol_Float _symbol;

	public Expression_Float(ParseSymbol_Float symbol) {
		_symbol = symbol;
	}

	public Object evaluate() {
		return _symbol.getValue();
	}
}

class Expression_String extends Expression {
	private ParseSymbol_String _symbol;

	public Expression_String(ParseSymbol_String symbol) {
		_symbol = symbol;
	}

	public Object evaluate() {
		return _symbol.getValue();
	}
}
