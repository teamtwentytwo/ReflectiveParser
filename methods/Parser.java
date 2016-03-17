package methods;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Parses input text using the Lexer class and an LL(1) grammar.
 */
class Parser {
	private Stack<ParseSymbol.Type> _stack;
	private Stack<ParseSymbol> _semantic_stack;
	private ParseSymbol _lookahead = null;
	private Lexer _lexer;

	/**
	 * Default constructor.
	 */
	public Parser() {
		_stack = new Stack<ParseSymbol.Type>();
		_stack.push(ParseSymbol.Type.EndOfFile);
		_stack.push(ParseSymbol.Type.Expression);
		_semantic_stack = new Stack<ParseSymbol>();
		_lexer = new Lexer();
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
	 * Closes input on Lexer, allowing the parse to fully complete.
	 */
	public void closeInput() {
		_lexer.closeInput();
	}

	public boolean isInputClosed() {
		return _lexer.isInputClosed();
	}

	/**
	 * Performs the parse.
	 *
	 * @return If the parse completed, returns the parse output, otherwise null.
	 *         The parse can only complete after a call to closeInput().
	 */
	public Expression parse() throws ParseException {
		if (_lookahead == null) {
			_lookahead = _lexer.readNext();
		}

		while (!_stack.empty() && _lookahead != null) {
			switch (_stack.peek()) {
				// Non-terminal symbols
				case Expression:
					switch (_lookahead.getType()) {
						case LeftParenthesis:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Act_FunctionCall);
							_stack.push(ParseSymbol.Type.RightParenthesis);
							_stack.push(ParseSymbol.Type.ExpressionList);
							_stack.push(ParseSymbol.Type.Identifier);
							_stack.push(ParseSymbol.Type.LeftParenthesis);
							break;
						case Integer:
						case Float:
						case String:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Act_Value);
							_stack.push(_lookahead.getType());
							break;
						default:
							//throw new ParseException("Missing Left Parenthesis", _lookahead.getLocation().getColumn() - 1);
							throw new ParseException("Invalid Expression Read", _lookahead.getLocation().getColumn());
					}
					break;

				case ExpressionList:
					switch (_lookahead.getType()) {
						case LeftParenthesis:
						case Integer:
						case Float:
						case String:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Act_ExpressionList);
							_stack.push(ParseSymbol.Type.ExpressionList);
							_stack.push(ParseSymbol.Type.Expression);
							break;
						case RightParenthesis:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Act_NewExpressionList);
							break;
						default:
							//throw new ParseException("Missing Right Parenthesis", _lookahead.getLocation().getColumn());
							throw new ParseException("Invalid Expression Read", _lookahead.getLocation().getColumn());
					}
					break;

				// Action symbols
				case Act_FunctionCall: {
						_stack.pop();
						ParseSymbol_RightParenthesis right_paren = (ParseSymbol_RightParenthesis)_semantic_stack.pop();
						ParseSymbol_ExpressionList arguments = (ParseSymbol_ExpressionList)_semantic_stack.pop();
						ParseSymbol_Identifier identifier = (ParseSymbol_Identifier)_semantic_stack.pop();
						ParseSymbol_LeftParenthesis left_paren = (ParseSymbol_LeftParenthesis)_semantic_stack.pop();
						Expression expression = new Expression_FunctionCall(left_paren, identifier, arguments.getValue(), right_paren);
						_semantic_stack.push(new ParseSymbol_Expression(expression));
					}
					break;

				case Act_Value: {
						_stack.pop();
						ParseSymbol symbol = _semantic_stack.pop();
						Expression expression = null;
						switch (symbol.getType()) {
							case Integer:
								expression = new Expression_Integer((ParseSymbol_Integer)symbol);
								break;
							case Float:
								expression = new Expression_Float((ParseSymbol_Float)symbol);
								break;
							case String:
								expression = new Expression_String((ParseSymbol_String)symbol);
								break;
						}
						_semantic_stack.push(new ParseSymbol_Expression(expression));
					}
					break;

				case Act_ExpressionList: {
						_stack.pop();
						ParseSymbol_ExpressionList list = (ParseSymbol_ExpressionList)_semantic_stack.pop();
						ParseSymbol_Expression expression = (ParseSymbol_Expression)_semantic_stack.pop();
						list.getValue().add(0, expression.getValue());
						_semantic_stack.push(list);
					}
					break;

				case Act_NewExpressionList: {
						_stack.pop();
						_semantic_stack.push(new ParseSymbol_ExpressionList(new LinkedList<Expression>()));
					}
					break;

				// Terminal symbols
				default:
					if (_stack.peek() == _lookahead.getType()) {
						_stack.pop();
						if (_lookahead.getType() != ParseSymbol.Type.EndOfFile) {
							_semantic_stack.push(_lookahead);
						}
						_lookahead = _lexer.readNext();
					}
					else {
						throw new ParseException("Invalid Expression Read", _lookahead.getLocation().getColumn());
					}
			}
		}

		if (_stack.empty()) {
			return (Expression)_semantic_stack.peek().getValue();
		}
		else {
			return null;
		}
	}
};
