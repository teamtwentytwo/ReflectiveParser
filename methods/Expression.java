package methods;

import java.text.ParseException;
import java.lang.reflect.Method;
import java.util.List;

abstract class Expression {
	/**
	 * Evaluates the expression and returns its value.
	 * Runtime type checking will be necessary to determine the value's type.
	 */
	public abstract Object evaluate(Class commands) throws ParseException;
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

	public Object evaluate(Class commands) throws ParseException{
		Object[] parameters = new Object[_arguments.size()];
		for (int i = 0; i < _arguments.size(); i++) {
			parameters[i] = _arguments.get(i).evaluate(commands);
		}

		Class[] types = new Class[_arguments.size()];
		for (int i = 0; i < parameters.length; i++) {
			types[i] = parameters[i].getClass();
			// Normalize int and float
			if (types[i] == int.class) types[i] = Integer.class;
			if (types[i] == float.class) types[i] = Float.class;
		}

		String name = _identifier.getValue();
		try{
			for (Method method : commands.getDeclaredMethods()) {
				if (!name.equals(method.getName())) continue;
	
				Class[] parameter_types = method.getParameterTypes();
				if (types.length != parameter_types.length) continue;
	
				boolean types_matched = true;
				for (int i = 0; i < types.length; ++i) {
					// Normalize int and float
					if (parameter_types[i] == int.class) parameter_types[i] = Integer.class;
					if (parameter_types[i] == float.class) parameter_types[i] = Float.class;
	
					if (types[i] != parameter_types[i]) {
						types_matched = false;
						break;
					}
				}
				if (!types_matched) continue;
	
				// If all checks succeeded, we have found the method
				return method.invoke(null, parameters);
			}
	
			// No method has passed all the checks, the method could not be found
			throw new NoSuchMethodException(name);
		}catch (Exception e){
			String params = "";
			for(Class c: types){
				params += " " + c.toString();
			}
			throw new ParseException("Matching function for ("+ name + params +") not found", _identifier.getLocation().getColumn());		
		}
	}
}

class Expression_Integer extends Expression {
	private ParseSymbol_Integer _symbol;

	public Expression_Integer(ParseSymbol_Integer symbol) {
		_symbol = symbol;
	}

	public Integer evaluate(Class commands) throws ParseException {
		return _symbol.getValue();
	}
}

class Expression_Float extends Expression {
	private ParseSymbol_Float _symbol;

	public Expression_Float(ParseSymbol_Float symbol) {
		_symbol = symbol;
	}

	public Float evaluate(Class commands) throws ParseException {
		return _symbol.getValue();
	}
}

class Expression_String extends Expression {
	private ParseSymbol_String _symbol;

	public Expression_String(ParseSymbol_String symbol) {
		_symbol = symbol;
	}

	public String evaluate(Class commands)throws ParseException {
		return _symbol.getValue();
	}
}
