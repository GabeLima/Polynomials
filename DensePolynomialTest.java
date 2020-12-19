import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class DensePolynomialTest{
	@Test
	public void testDegree() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		assertEquals(p.degree(), 2);
		Polynomial q = new DensePolynomial("x^50 + 5");
		assertEquals(q.degree(), 50);
		Polynomial k = new DensePolynomial("7");
		assertEquals(k.degree(), 0);
	}
	/**
	 * Testing a negative index to see assure a 0 result.
	 */
	@Test
	public void testGetCoefficient() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		assertEquals(p.getCoefficient(2), 3);
		assertEquals(p.getCoefficient(1), 2);
		assertEquals(p.getCoefficient(0), -1);
		Polynomial q = new DensePolynomial("x^50 + 5");
		assertEquals(q.getCoefficient(50), 1);
		assertEquals(q.getCoefficient(0), 5);
		assertEquals(q.getCoefficient(-1), 0);
	}
	@Test
	public void testIsZero() {
		Polynomial p = new DensePolynomial("200x + 5");
		assertFalse(p.isZero());
		Polynomial q = new DensePolynomial("9");
		assertFalse(q.isZero());
		Polynomial z = new DensePolynomial("0");
		assertTrue(z.isZero());
		assertEquals(z.toString(), "0");
	}
	/**
	 * Test to make sure that a negative index IllegalArgumentException is thrown
	 */
	@Test
	public void testAdd() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial q = new SparsePolynomial("x^50 + 5");
		Polynomial sum1 = p.add(q);
		assertEquals(sum1.toString(), "x^50 + 3x^2 + 2x + 4");
		Polynomial result = new DensePolynomial("x^50 + 3x^2 + 2x + 4");
		assertEquals(sum1, result);
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial k = new SparsePolynomial("3x^2 + 2x^3 - 10x^-20");
			p.add(k);
		});
	}
	/**
	 * Test to make sure multiply throws a negative index IllegalArgumentExcepion
	 */
	@Test
	public void testMultiply() {
		Polynomial m1 = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial m2 = new DensePolynomial("x^50 + 5");
		Polynomial mult1 = m1.multiply(m2); 
		assertEquals(mult1.toString(), "3x^52 + 2x^51 - x^50 + 15x^2 + 10x - 5");
		Polynomial result = new DensePolynomial("3x^52 + 2x^51 - x^50 + 15x^2 + 10x - 5");
		assertEquals(mult1, result);
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial m3 = new SparsePolynomial("-50x^12 + -7x^-12");
			m2.multiply(m3);
		});
	}
	@Test
	public void testSubtract() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial q = new SparsePolynomial("5x + 5");
		Polynomial result = new DensePolynomial("3x^2 - 3x - 6");
		assertEquals(p.subtract(q), result);
		assertTrue(p.subtract(p).isZero());
		
		Polynomial t1 = new DensePolynomial("20x^20 + 5x^5");
		Polynomial t2 = new DensePolynomial("15x^19 + 5x^5");
		Polynomial result2 = new DensePolynomial("20x^20 - 15x^19");
		assertEquals(t1.subtract(t2), result2);
	}
	@Test
	public void testMinus() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial result = new DensePolynomial("-3x^2 - 2x + 1");
		assertEquals(p.minus(), result);
		Polynomial q = new DensePolynomial("x^50 + 5");
		Polynomial result2 = new DensePolynomial("-x^50 - 5");
		assertEquals(q.minus(), result2);
		assertEquals(q.minus().minus().toString(), "x^50 + 5");
	}
	/**
	 * Test to make sure IllegalArgumentExceptions are thrown in various, weird cases.
	 * Also test to make sure that wellFormed returns false in these cases.
	 */
	@Test
	public void testWellFormed(){
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new DensePolynomial("3/2x^2 + 2x - 1");
			assertFalse(p.wellFormed());
		});
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new DensePolynomial("3x^2 + 2x^3 - 1");
			assertFalse(p.wellFormed());
		});
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new DensePolynomial("3x^2 + 2x - x^-5");
			assertFalse(p.wellFormed());
		});
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new DensePolynomial("0x^2 + 2x - x^-5");
			assertFalse(p.wellFormed());
		});
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		assertTrue(p.wellFormed());
	}
	@Test
	public void testToString() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		assertEquals(p.toString(), "3x^2 + 2x - 1");
		Polynomial q = new DensePolynomial("3x^2 + 2x - 1");
		assertEquals(q.minus().toString(), "-3x^2 - 2x + 1");
	}
	@Test
	public void testEquals() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial q = new SparsePolynomial("3x^2 + 2x - 1");
		assertEquals(p, q);
		assertEquals(p,p);
		Polynomial k = new DensePolynomial("5x^2 + 4x - 3");
		Polynomial r = new DensePolynomial("2x^2 + 2x - 2");
		assertEquals(k.subtract(r), p);
	}
	
}
	
	
	