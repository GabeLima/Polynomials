import java.util.Arrays;
import java.util.HashMap;
/*
 * It is important to note, that unless otherwise stated in the documentation, public methods 
 * excluding constructors, are built around the idea that the current instance of the class is well formed.
 */
public class SparsePolynomial implements Polynomial{
	HashMap<Integer, Integer> polyMap = new HashMap<Integer, Integer>(); // key, value pairs... key = degree, value = coefficient
	private boolean wellFormed = true;
    /**
     * A constructor used to instantiate the class.
     *
     * @param startingArray the starting string used to instantiate the current instance.
     * @param tokens used to split the parameter into iterable subsections.
     * @param multiplier used to alter stored values.
     * @param coeffecient the coefficient stored in the current instance. I spelled it wrong I know.
     * @param exponent the key place in <code> polyMap </code> where the coefficient is stored.
     * @param decreasingExponent checks for the class invariant in the parameter
     * @param splitIndex used to parse integers from the parameter
     * @throws IllegalArgumentException if the parameter breaks the class invariant
     */
	public SparsePolynomial(String startingArray) throws IllegalArgumentException {
		acceptableValues(startingArray);
		String[] tokens = startingArray.split(" ");
		int multiplier = 1;
		int decreasingExponent = 999999999; //just some extremely high random number
		int exponent = 0;
		int coeffecient;
		int splitIndex;
		for(int i = 0; i < tokens.length; i ++) {
			if(tokens[i].contains("^")) {
				splitIndex = tokens[i].indexOf("^") + 1;
				exponent = Integer.parseInt(tokens[i].substring(splitIndex, tokens[i].length()));
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
				polyMap.put(exponent, multiplier * coeffecient);
				//polyArray[exponent] = multiplier * coeffecient;
				multiplier = 1;
			}
			else if(tokens[i].contains("x")) {
				
				splitIndex = tokens[i].indexOf("x");
				exponent = 1;
				//int exponent = Integer.parseInt(startingArray.substring(splitIndex + 1, tokens[i].length()));
				if(tokens[i].length() == 1) {
					coeffecient = 1;
				}
				else {
					coeffecient = Integer.parseInt(tokens[i].substring(0, splitIndex));
					if(coeffecient == 0)
						throw new IllegalArgumentException("Cannot have zeros as coefficients. Please enter valid input.");
				}
				polyMap.put(exponent, multiplier * coeffecient);
				//polyArray[exponent] = multiplier * coeffecient;
				multiplier = 1;
			}
			else if (tokens[i].equals("-")) {
				multiplier = -1;
				continue;
			}
			else if (tokens[i].equals("+")) {
				multiplier = 1;
				continue;
			}
			else { //its just a coeffecient
				exponent = 0;
				coeffecient = multiplier * Integer.parseInt(tokens[i]);
				polyMap.put(exponent, coeffecient);
				//polyArray[0] = multiplier * Integer.parseInt(tokens[i]);
			}
			if(i == 0) {
				decreasingExponent = exponent;
			}
			else {
				if(decreasingExponent <= exponent) {
					wellFormed = false;
					throw new IllegalArgumentException("Exponents must be in decreasing canonical order. Please enter valid input.");
				}
				decreasingExponent = exponent;
			}
		}
	}
    /**
     * A constructor which updates the current instance based off the parameter. It is assumed that the 
     * parameter is a valid Sparse Polynomial HashMap<Integer, Integer>.
     *
     * @param newPolyArray the current instance is effectively replaced with the Hashmap stored in <code> newPolyArray </code>
     */
	protected SparsePolynomial(HashMap<Integer, Integer> newPolyMap) {
		this.polyMap = newPolyMap;
	}
    /**
     * Returns the degree of the polynomial.
     *
     * @return the largest exponent with a non-zero coefficient.  If all terms have zero exponents, it returns 0.
     * It also returns 0 if the length of the array is 0.
     */
	@Override
	public int degree() {
		if(polyMap.size() == 0) {
			return 0;
		}
		else {
			Object[] keysArray = polyMap.keySet().toArray();
			Arrays.sort(keysArray);
			return (int)keysArray[keysArray.length-1];
		}
	}
    /**
     * Returns the coefficient corresponding to the given exponent.  Returns 0 if there is no term with that exponent
     * in the polynomial.
     *
     * @param d the exponent whose coefficient is returned.
     * @return the coefficient of the term of whose exponent is d.
     */
	@Override
	public int getCoefficient(int d) {
		if (polyMap.containsKey(d)) {
			return polyMap.get(d);
		}
		return 0;
	}
    /**
     * @return true if the polynomial represents the zero constant. The array must be empty or filled entirely with zeros.
     */
	@Override
	public boolean isZero() {
		if(polyMap.size() == 0) {
			return true;
		}
		else {
			for(Integer value : polyMap.values()) {
				if (value != 0) {
					return false;
				}
			}
			return true;
		}
	}
    /**
     * Returns a Sparse Polynomial by adding the parameter to the current instance. Neither the current instance nor the
     * parameter are modified.
     *
     * @param q the non-null polynomial to add to <code>this</code>
     * @param newPolyMap the HashMap used to store the result of adding the parameter to the current instance.
     * @param upperBound the highest exponent in the given Polynomial q. Used for iteration purposes.
     * @param lowerBound the lowest exponent in the given Polynomial q. Used for iteration purposes.
     * @return <code>this + </code>q
     * @throws NullPointerException if q is null
     */
	@Override
	public Polynomial add(Polynomial q) throws NullPointerException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		HashMap<Integer, Integer> newPolyMap = new HashMap<Integer, Integer>(polyMap); //shallow copy of polyMap
		int upperBound = q.degree();
		int lowerBound = q.toString().lastIndexOf("x^-");
		if(lowerBound == -1) {
			lowerBound = 0;
		}
		else {
			lowerBound = Integer.parseInt(q.toString().substring(lowerBound + 2, q.toString().length()));
		}
		for(int i = lowerBound; i < upperBound + 1; i ++) {
			if(newPolyMap.containsKey(i)) {
				newPolyMap.replace(i, newPolyMap.get(i) + q.getCoefficient(i));
			}
			else {
				newPolyMap.put(i, q.getCoefficient(i) + this.getCoefficient(i)); 
			}
		}
		return new SparsePolynomial(newPolyMap);
	}
    /**
     * Returns a Sparse Polynomial by multiplying the parameter with the current instance.  Neither the current instance nor
     * the parameter are modified.
     *
     * @param q the polynomial to multiply with <code>this</code>
     * @param newPolyMap the HashMap used to store the result of adding the parameter to the current instance.
     * @param upperBound the highest exponent in the given Polynomial q. Used for iteration purposes.
     * @param lowerBound the lowest exponent in the given Polynomial q. Used for iteration purposes.
     * @return <code>this * </code>q
     * @throws NullPointerException if q is null
     */
	@Override
	public Polynomial multiply(Polynomial q) throws NullPointerException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		HashMap<Integer, Integer> newPolyMap = new HashMap<Integer, Integer>();
		int upperBound = q.degree();
		int lowerBound = q.toString().lastIndexOf("x^-");
		if(lowerBound == -1) {
			lowerBound = 0;
		}
		else {
			lowerBound = Integer.parseInt(q.toString().substring(lowerBound + 2, q.toString().length()));
		}
		for(Integer keys : polyMap.keySet()) {
			for(int i = lowerBound; i < upperBound + 1; i ++) {
				if(newPolyMap.containsKey(keys + i)) {
					newPolyMap.replace(keys + i, newPolyMap.get(keys + i) + q.getCoefficient(i) * this.getCoefficient(keys));// 3x^3 + 4x^2 * 2x = 11x^3
				}
				else {
					newPolyMap.put(keys + i, q.getCoefficient(i) * this.getCoefficient(keys)); 
				}
			}
		}
		return new SparsePolynomial(newPolyMap);
	}
    /**
     * Returns a Sparse Polynomial by subtracting the parameter from the current instance. Neither the current instance nor
     * the parameter are modified. Does so by using the add() method in parallel with negating the given Polynomial
     * using the minus() method.
     *
     * @param q the non-null polynomial to subtract from <code>this</code>
     * @return <code>this - </code>q
     * @throws NullPointerException if q is null
     */
	@Override
	public Polynomial subtract(Polynomial q) throws NullPointerException{
		if(q == null) {
			throw new NullPointerException("The given Polynomial cannot be null");
		}
		return this.add(q.minus());
	}
    /**
     * Returns a Sparse Polynomial by negating the current instance. The current instance is not modified.
     * 
     * @param newPolyMap the HashMap<Integer, Integer> used to store the negation of the current instance.
     * @return -this
     */
	@Override
	public Polynomial minus() {
		HashMap<Integer, Integer> newPolyMap = new HashMap<Integer, Integer>(polyMap);
		for(Integer keys : newPolyMap.keySet()) {
			newPolyMap.replace(keys, -newPolyMap.get(keys));
		}
		return new SparsePolynomial(newPolyMap);
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
		Object[] keysArray = polyMap.keySet().toArray();
		Arrays.sort(keysArray);
		String temp = "";
		for(int i = keysArray.length - 1; i >= 0; i --) {
			if(this.getCoefficient((int)keysArray[i]) != 0) {
				if(temp == "") {
					if(this.getCoefficient((int)keysArray[i]) < 0) {
						temp += "-";
					}
				}
				else if (this.getCoefficient((int)keysArray[i]) > 0) {
					temp += "+ ";
				}
				else {
					temp += "- ";
				}
				if((int)keysArray[i] == 0) {
					temp += Math.abs(this.getCoefficient((int)keysArray[i])) + " ";
					continue;
				}
				else if(Math.abs(this.getCoefficient((int)keysArray[i])) == 1) {
					temp += "x";
				}
				else {
					temp += Math.abs(this.getCoefficient((int)keysArray[i])) + "x";
				}
				if((int)keysArray[i] == 1) {
					temp += " ";
				}
				else {
					temp+= "^" + (int)keysArray[i] + " ";
				}
			}
		}
		if(temp == "") {
			return "0";
		}
		else {
			return temp.substring(0, temp.length()-1); //cuts off and addd space
		}
	}
    /**
     * Checks whether or not an input string is valid based off of SparsePolynomial constraints.
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