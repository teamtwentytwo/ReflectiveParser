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
	 */
	public FileLocation(String filename) {
		this(filename, 0, 0);
	}

	/**
	 * Full constructor.
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
	 */
	public FileLocation copy() {
		return new FileLocation(_filename, _line, _column);
	}

	/**
	 * Returns the filename.
	 */
	public String getFilename() {
		return _filename;
	}

	/**
	 * Returns the line number.
	 */
	public int getLine() {
		return _line;
	}

	/**
	 * Returns the column number.
	 */
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
	 */
	public void skipColumns(int n) {
		_column += n;
	}

	/**
	 * Moves to the given location.
	 */
	public void goTo(int line, int column) {
		_line = line;
		_column = column;
	}

	/**
	 * Converts the location to a printable string.
	 */
	public String toString() {
		return _filename + ":" + Integer.toString(_line) + ":" + Integer.toString(_column);
	}
	// TODO - better output formatting/interface?
}
