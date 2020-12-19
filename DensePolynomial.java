/*
 * It is important to note, that unless otherwise stated in the documentation, public methods 
 * excluding constructors, are built around the idea that the current instance of the class is well formed.
 */
public class DensePolynomial implements Polynomial{
	int[] polyArray;
	private boolean wellFormed = true;
    /**
     * A constructor used to instantiate the class.
     *
     * @param startingArray the starting string used to instantiate the current instance.
     * @param tokens used to split the parameter into iterable subsections.
     * @param multiplier used to alter stored values.
     * @param coeffecient the coefficient stored in the current instance. I spelled it wrong I know.
     * @param exponent the place in <code> polyArray </code> where the coefficient is stored.
     * @param decreasingExponent checks for the class invariant in the parameter
     * @param splitIndex used to parse integers from the parameter
     * @throws IllegalArgumentException if the parameter breaks the class invariant
     */
	public DensePolynomial(String startingArray) throws IllegalArgumentException { 
		acceptableValues(startingArray);
		String[] tokens = startingArray.split(" ");
		int multiplier = 1;
		int decreasingExponent = 999999999; //just some extremely high random number
		for(int i = 0; i < tokens.length; i ++) {
			if(tokens[i].contains("^")) {
				int splitIndex = tokens[i].indexOf("^") + 1;
				int exponent = Integer.parseInt(tokens[i].substring(splitIndex, tokens[i].length()));
				if (exponent < 0) {
					wellFormed = false;
					throw new IllegalArgumentException("Cannot have a negative exponent with DensePolynomials. Please enter valid input.");
				}
				int coeffecient;
				if(splitIndex - 2 == 0) {
					coeffecient = 1;
				}
				else {
					if(tokens[i].substring(0, splitIndex-2).equals("-")) {
						coeffecient = -1;
					}
					else {
						coeffecient = Integer.parseInt(tokens[i].substring(0, splitIndex-2));
						if(coeffecient == 0)
							throw new IllegalArgumentException("Cannot have zeros as coefficients. Please enter valid input.");
					}
				}
				if(i == 0) {
					polyArray = new int[exponent + 1];
					initializeArray();
					decreasingExponent = exponent;
				}
				else {
					if(decreasingExponent <= exponent) {
						wellFormed = false;
						throw new IllegalArgumentException("Exponents must be in decreasing canonical order. Please enter valid input.");
					}
					decreasingExponent = exponent;
				}
				polyArray[exponent] = multiplier * coeffecient;
				multiplier = 1;
			}
			else if(tokens[i].contains("x")) {
				
				int splitIndex = tokens[i].indexOf("x");
				int exponent = 1;
				int coeffecient ;
				if(tokens[i].length() == 1) {
					coeffecient = 1;
				}
				else {
					coeffecient = Integer.parseInt(tokens[i].substring(0, splitIndex));
					if(coeffecient == 0)
						throw new IllegalArgumentException("Cannot have zeros as coefficients. Please enter valid input.");
				}
				if(i == 0) {
					polyArray = new int[exponent + 1];
					initializeArray();
					decreasingExponent = exponent;
				}
				else {
					if(decreasingExponent <= exponent) {
						wellFormed = false;
						throw new IllegalArgumentException("Exponents must be in decreasing canonical order. Please enter valid input.");
					}
					decreasingExponent = exponent;
				}
				polyArray[exponent] = multiplier * coeffecient;
				multiplier = 1;
			}
			else if (tokens[i].equals("-")) {
				multiplier = -1;
			}
			else if (tokens[i].equals("+")) {
				multiplier = 1;
			}
			else {
				int exponent = 0;
				if(i == 0) {
					exponent = 0;
					polyArray = new int[1];
				}
				else if(decreasingExponent <= exponent) {
					wellFormed = false;
					throw new IllegalArgumentException("Exponents must be in decreasing canonical order. Please enter valid input.");
				}
				polyArray[0] = multiplier * Integer.parseInt(tokens[i]);
			}
		}
	}
	private void initializeArray() {
		for(int i = 0; i < polyArray.length; i ++) {
			polyArray[i] = 0;
		}
	}
    /**
     * A constructor which updates the current instance based off the parameter. It is assumed that the 
     * parameter is a valid Dense Polynomial int[].
     *
     * @param newPolyArray the current instance is effectively replaced with the int[] stored in <code> newPolyArray </code>
     */
	protected DensePolynomial(int[] newPolyArray) {
		this.polyArray = newPolyArray;
	}

    /**
     * Returns the degree of the polynomial.
     *
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     * It also returns 0 if the length of the array is 0.
     */
	@Override
	public int degree() {
		if(polyArray.length == 0) {
			return 0;
		}
		else {
			int index = 0;
			for(int i = 0; i < polyArray.length; i ++) {
				if(polyArray[i] != 0) {
					index = i;
				}
			}
			return index;
		}
	}
    /**
     * Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial, or if the given exponent is negative (impossible for DensePolynomials)
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
	@Override
	public int getCoefficient(int d) {
		if(d >= polyArray.length || d < 0) {
			return 0;
		}
		else {
			return polyArray[d];
		}
	}
    /**
     * @return true if the polynomial represents the zero constant. The array must be empty or filled entirely with zeros.
     */
	@Override
	public boolean isZero() {
		for(int i = 0; i < polyArray.length; i ++) {
			if(polyArray[i] != 0) {
				return false;
			}
		}
		return true;
	}
    /**
     * Returns a Dense Polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @param newPolyArray the array used to store the result of adding the parameter to the current instance.
     * @param upperBound the highest exponent in the given Polynomial q. Used for iteration purposes.
     * @param lowerBound the lowest exponent in the given Polynomial q. Used for iteration purposes.
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if q is a SparsePolynomial containing negative exponents
     */
	@Override
	public Polynomial add(Polynomial q) throws NullPointerException, IllegalArgumentException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		int[] newPolyArray ;//= new int[polyArray.length];
		int upperBound = q.degree();
		int lowerBound = q.toString().lastIndexOf("x^-");
		if(lowerBound == -1) {
			lowerBound = 0;
		}
		else {
			lowerBound = Integer.parseInt(q.toString().substring(lowerBound + 2, q.toString().length()));
		}
		if(lowerBound < 0) {
			throw new IllegalArgumentException("Cannot have negative degrees adding to a Dense Polynomial. Please enter valid input.");
		}
		if(upperBound > this.degree()) {
			newPolyArray = new int[upperBound + 1];
		}
		else {
			newPolyArray = new int[this.degree() + 1];
		}
		for(int i = 0; i< polyArray.length; i++) {
			newPolyArray[i] = polyArray[i];
		}
		for(int i = lowerBound; i < upperBound + 1; i ++) {
			newPolyArray[i] += q.getCoefficient(i);
		}
		return new DensePolynomial(newPolyArray);
	}
    /**
     * Returns a Dense Polynomial by multiplying the parameter with the current instance.  Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the polynomial to multiply with <code>this</code>
     * @param newPolyArray the array used to store the result of multiplying the parameter to the current instance.
     * @param upperBound the highest exponent in the given Polynomial q. Used for iteration purposes.
     * @param lowerBound the lowest exponent in the given Polynomial q. Used for iteration purposes.
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if q is a SparsePolynomial containing negative exponents
     */
	@Override
	public Polynomial multiply(Polynomial q) throws NullPointerException, IllegalArgumentException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		int upperBound = q.degree();
		int lowerBound = q.toString().lastIndexOf("x^-");
		if(lowerBound == -1) {
			lowerBound = 0;
		}
		else {
			lowerBound = Integer.parseInt(q.toString().substring(lowerBound + 2, q.toString().length()));
		}
		if(lowerBound < 0) {
			throw new IllegalArgumentException("Cannot have negative degrees adding to a Dense Polynomial. Please enter valid input.");
		}
		int[] newPolyArray = new int[this.degree() + q.degree() + 1];
		for(int n = 0; n < this.degree() + 1; n ++) {
			for(int i = lowerBound; i < upperBound + 1; i ++) {
				newPolyArray[n+i] += q.getCoefficient(i) * this.getCoefficient(n);
			}
		}
		
		return new DensePolynomial(newPolyArray);
	}
    /**
     * Returns a Dense Polynomial by subtracting the parameter from the current instance. Neither the current instance nor
     * the parameter are modified. Does so by using the add() method in tangent with negating the given Polynomial
     * using the minus() method.
     *
     * @param q the non-null polynomial to subtract from <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     * @throws IllegalArgumentException if q is a SparsePolynomial containing negative exponents
     */
	@Override
	public Polynomial subtract(Polynomial q) throws NullPointerException, IllegalArgumentException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		return this.add(q.minus());
	}
    /**
     * Returns a Dense Polynomial by negating the current instance. The current instance is not modified.
     * 
     * @param newPolyArray the int[] used to store the negation of the current instance.
     * @return -this
     */
	@Override
	public Polynomial minus() {
		int[] newPolyArray = new int[polyArray.length];
		for(int i = 0; i < polyArray.length; i ++) {
			newPolyArray[i] = -this.getCoefficient(i); 
		}
		return new DensePolynomial(newPolyArray);
	}
    /**
     * Returns the {@param wellFormed}, which denotes if the class invariant holds true or not.
     * 
     * @param wellFormed a boolean checked at the creation of the Polynomial, denotes whether
     * or not the class invariant holds true.
     * @return {@literal true} if the class invariant holds, and {@literal false} otherwise.
     */
	@Override
	public boolean wellFormed() {
		return wellFormed;
	}
    /**
     * Returns a string representation of the current instance.
     * 
     * @param temp used to store the string representation eventually returned.
     * @return a string representation of <code>this </code> Polynomials instance
     */
	@Override
	public String toString() {
		String temp = "";
		for(int i = this.degree(); i >= 0; i --) {
			if(this.getCoefficient(i) != 0) {
				if(temp == "") {
					if(this.getCoefficient(i) < 0) {
						temp += "-";
					}
				}
				else if (this.getCoefficient(i) > 0) {
					temp += "+ ";
				}
				else {
					temp += "- ";
				}
				if(i == 0) {
					temp += Math.abs(this.getCoefficient(i)) + " ";
					break;
				}
				else if(Math.abs(this.getCoefficient(i)) == 1) {
					temp += "x";
				}
				else {
					temp += Math.abs(this.getCoefficient(i)) + "x";
				}
				if(i == 1) {
					temp += " ";
				}
				else {
					temp+= "^" + i + " ";
				}
			}
			
		}
		if(temp == "") {
			return "0";
		}
		else {
			return temp.substring(0, temp.length()-1);
		}
	}
    /**
     * Checks whether or not an input string is valid based off of DensePolynomial constraints.
     * Used in tangent with the DensePolynomial constructor.
     * 
     * @param inputString a String used in the creation of <code> this </code>Polynomial. 
     * Checked for errors in this method.
     * @param okCharacters, denotes acceptable characters that determines if a throw is necessary in checking 
     * <code> inputString </code>
     * @param acceptable a boolean that determines if a throw is necessary in checking <code> inputString </code>
     * @return a string representation of <code>this </code> Polynomials instance
     * @throws IllegalArgumentException if there is in invalid argument in <code> inputString </code>. 
     * Invalid arguments are values contained in <code> inputString </code> that are not contained in 
     * <code> okCharacters </code>
     */
	private void acceptableValues(String inputString) throws IllegalArgumentException{
		String okCharacters = "0123456789+-^x ";
		for(int i = 0; i < inputString.length(); i ++) {
			boolean acceptable = false;
			for(int k = 0; k< okCharacters.length(); k++) {
				if (okCharacters.charAt(k) == inputString.charAt(i)) {
					acceptable = true;
				}
			}
			if (!acceptable) {
				wellFormed = false;
				throw new IllegalArgumentException("Input: '" + inputString.charAt(i) + "' is not valid. Please enter valid input.");
			}
		}
	}
    /**
     * Checks whether or not the current instance is equal to the parameter.
     * @param q the polynomial to check equality with
     * @return true or false depending on if this instances<code> toString() </code> is equal to 
     * the parameters <code> toString() </code>
     */
	@Override
	public boolean equals(Object q) {
		return this.toString().equals(q.toString());
	}
}