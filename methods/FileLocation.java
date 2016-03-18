package methods;

/**
 * Represents a location in a file.
 */
class FileLocation {
	private String _filename;
	private int _line;
	private int _column;

	/**
	 * Constructor that initializes to line 0, column 0.
	 * @param filename the name of the file
	 */
	public FileLocation(String filename) {
		this(filename, 0, 0);
	}

	/**
	 * Full constructor.
	 * @param filename the name of the file
	 * @param line the line to start at
	 * @param column the column to start at
	 */
	public FileLocation(String filename, int line, int column) {
		if (filename == null) {
			filename = "";
		}
		_filename = filename;
		_line = line;
		_column = column;
	}

	/**
	 * Creates a copy of the object.
	 * @return a copy of the object
	 */
	public FileLocation copy() {
		return new FileLocation(_filename, _line, _column);
	}

	public String getFilename() {
		return _filename;
	}

	public int getLine() {
		return _line;
	}

	public int getColumn() {
		return _column;
	}

	/**
	 * Performs a 'line feed'; increments line number, sets column number to 0.
	 */
	public void lineFeed() {
		++_line;
		_column = 0;
	}

	/**
	 * Skips a number of columns.
	 * @param n the number of columns to skip
	 */
	public void skipColumns(int n) {
		_column += n;
	}

	/**
	 * Moves to the given location.
	 * @param line the line to move to
	 * @param column the column to move to
	 */
	public void goTo(int line, int column) {
		_line = line;
		_column = column;
	}
}
