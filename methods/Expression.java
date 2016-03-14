package methods;

import java.lang.reflect.Method;
import java.util.List;

abstract class Expression {
	/**
	 * Evaluates the expression and returns its value.
	 * Runtime type checking will be necessary to determine the value's type.
	 */
	public abstract Object evaluate(Class commands) throws Exception;
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

	public Object evaluate(Class commands) throws Exception{

		String funcName = _identifier.getValue();
		Object[] parameters = new Object[_arguments.size()];

		for (int i = 0; i < _arguments.size(); i++) {
			parameters[i] = _arguments.get(i).evaluate(commands);

		}
		Class[] types = new Class[_arguments.size()];

		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].getClass();
		}
		Method method = commands.getMethod(funcName, types);

		Object returnValue = method.invoke(null, parameters);

		return returnValue;
	}
}

class Expression_Integer extends Expression {
	private ParseSymbol_Integer _symbol;

	public Expression_Integer(ParseSymbol_Integer symbol) {
		_symbol = symbol;
	}

	public Integer evaluate(Class commands) throws Exception {
		return _symbol.getValue();
	}
}

class Expression_Float extends Expression {
	private ParseSymbol_Float _symbol;

	public Expression_Float(ParseSymbol_Float symbol) {
		_symbol = symbol;
	}

	public Float evaluate(Class commands) throws Exception {
		return _symbol.getValue();
	}
}

class Expression_String extends Expression {
	private ParseSymbol_String _symbol;

	public Expression_String(ParseSymbol_String symbol) {
		_symbol = symbol;
	}

	public String evaluate(Class commands)throws Exception {
		return _symbol.getValue();
	}
}
