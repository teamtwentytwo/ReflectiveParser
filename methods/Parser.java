package methods;

import java.util.Stack;

/**
 * Parses input text using the Lexer class and an LL(1) grammar.
 */
class Parser {
	private Stack<ParseSymbol.Type> _stack;
	private ParseSymbol _lookahead = null;
	private Lexer _lexer;

	/**
	 * Default constructor.
	 */
	public Parser() {
		_stack = new Stack<ParseSymbol.Type>();
		_stack.push(ParseSymbol.Type.Expression);
		_lexer = new Lexer();
	}

	/**
	 * Constructor that provides some initial input text.
	 *
	 * @param text initial input text
	 */
	public Parser(String text) {
		_stack = new Stack<ParseSymbol.Type>();
		_stack.push(ParseSymbol.Type.Expression);
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
	public void parse() throws Exception {
		// TODO - generate parse output

		if (_lookahead == null) {
			_lookahead = _lexer.readNext();
		}
		while (!_stack.empty() && _lookahead != null) {
			switch (_stack.peek()) {
				// Non-terminals
				case Expression:
					switch (_lookahead.getType()) {
						case LeftParenthesis:
							_stack.pop();
							_stack.push(ParseSymbol.Type.RightParenthesis);
							_stack.push(ParseSymbol.Type.ExpressionList);
							_stack.push(ParseSymbol.Type.Identifier);
							_stack.push(ParseSymbol.Type.LeftParenthesis);
							break;
						case Integer:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Integer);
							break;
						case Float:
							_stack.pop();
							_stack.push(ParseSymbol.Type.Float);
							break;
						case String:
							_stack.pop();
							_stack.push(ParseSymbol.Type.String);
							break;
						default:
							throw new Exception();
					}
					break;

				case ExpressionList:
					switch (_lookahead.getType()) {
						case LeftParenthesis:
						case Integer:
						case Float:
						case String:
							_stack.pop();
							_stack.push(ParseSymbol.Type.ExpressionList);
							_stack.push(ParseSymbol.Type.Expression);
							break;
						case RightParenthesis:
							_stack.pop();
							break;
						default:
							throw new Exception();
					}
					break;

				// Terminals
				default:
					if (_stack.peek() == _lookahead.getType()) {
						_stack.pop();
						_lookahead = _lexer.readNext();
					}
					else {
						throw new Exception();
					}
			}
		}
	}
};
