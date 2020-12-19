import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class SparsePolynomialTest{
	@Test
	public void testDegree() {
		Polynomial p = new SparsePolynomial("3x^200 + 2x - 1");
		assertEquals(p.degree(), 200);
		Polynomial q = new SparsePolynomial("x + 5");
		assertEquals(q.degree(), 1);
		Polynomial k = new SparsePolynomial("7");
		assertEquals(k.degree(), 0);
	}
	@Test
	public void testGetCoefficient() {
		Polynomial p = new SparsePolynomial("3x^2 + 2x - 1");
		assertEquals(p.getCoefficient(2), 3);
		assertEquals(p.getCoefficient(1), 2);
		assertEquals(p.getCoefficient(0), -1);
		Polynomial q = new SparsePolynomial("x^50 + 5 - 22x^-200");
		assertEquals(q.getCoefficient(50), 1);
		assertEquals(q.getCoefficient(0), 5);
		assertEquals(q.getCoefficient(-200), -22);
		assertEquals(q.getCoefficient(-1), 0);
	}
	@Test
	public void testIsZero() {
		Polynomial p = new SparsePolynomial("200x + 5");
		assertFalse(p.isZero());
		Polynomial q = new SparsePolynomial("9");
		assertFalse(q.isZero());
		Polynomial z = new SparsePolynomial("0");
		assertTrue(z.isZero());
		assertEquals(z.toString(), "0");
	}
	@Test
	public void testAdd() {
		Polynomial p = new SparsePolynomial("3x^2 + 2x - 1 - 4x^-7");
		Polynomial q = new SparsePolynomial("x^50 + 5 + 22x^-200");
		Polynomial sum1 = p.add(q);
		Polynomial result = new SparsePolynomial("x^50 + 3x^2 + 2x + 4 - 4x^-7 + 22x^-200");
		assertEquals(sum1, result);
		Polynomial second = new DensePolynomial("x^50 + 7");
		Polynomial result2 = new SparsePolynomial("x^50 + 3x^2 + 2x + 6 - 4x^-7");
		assertEquals(p.add(second), result2);
	}
	
	@Test
	public void testMultiply() {
		Polynomial m1 = new SparsePolynomial("3x^2 + 2x - 1");
		Polynomial m2 = new DensePolynomial("x^50 + 5");
		Polynomial mult1 = m1.multiply(m2); //dense * sparse //works
		Polynomial result = new DensePolynomial("3x^52 + 2x^51 - x^50 + 15x^2 + 10x - 5");
		assertEquals(mult1, result);
		Polynomial m3 = new SparsePolynomial("-50x^12 + -7x^-12");
		Polynomial result2 = new SparsePolynomial("-50x^62 - 7x^38 - 250x^12 - 35x^-12");
		assertEquals(m3.multiply(m2), result2);
	}
	@Test
	public void testSubtract() {
		Polynomial p = new DensePolynomial("3x^2 + 2x - 1");
		Polynomial q = new SparsePolynomial("5x + 5");
		Polynomial result = new DensePolynomial("3x^2 - 3x - 6").minus();
		assertEquals(q.subtract(p), result);
		assertTrue(p.subtract(p).isZero());
		q = new SparsePolynomial("5x + 5x^-6");
		Polynomial result2 = new SparsePolynomial("3x^2 - 3x - 1 - 5x^-6").minus();
		assertEquals(q.subtract(p), result2);
	}
	@Test
	public void testMinus() {
		Polynomial p = new SparsePolynomial("3x^2 + 2x - x^-20");
		Polynomial result = new SparsePolynomial("-3x^2 - 2x + x^-20");
		assertEquals(p.minus(), result);
		Polynomial q = new SparsePolynomial("x^50 + 5x^-1");
		Polynomial result2 = new SparsePolynomial("-x^50 - 5x^-1");
		assertEquals(q.minus(), result2);
	}
	/**
	 * Test to make sure that IllegalArguementException's are thrown with weird input, along with 
	 * wellFormed() being false.
	 */
	@Test
	public void testWellFormed() throws IllegalArgumentException{
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new SparsePolynomial("3/2x^2 + 2x - 1");
			assertFalse(p.wellFormed());
		});
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new SparsePolynomial("3x^2 + 2x^3 - 1");
			assertFalse(p.wellFormed());
		});
		assertThrows(IllegalArgumentException.class, () ->
		{
			Polynomial p = new SparsePolynomial("3x^-2 + 2x^-6 - x^-5");
			assertFalse(p.wellFormed());
		});
		Polynomial p = new SparsePolynomial("3x^2 + 2x - x^-50");
		assertTrue(p.wellFormed());
	}
	@Test
	public void testToString() {
		Polynomial p = new SparsePolynomial("3x^2 + 2x - x^-50");
		assertEquals(p.toString(), "3x^2 + 2x - x^-50");
		Polynomial q = new SparsePolynomial("3x^2 + 2x - 1");
		assertEquals(q.minus().toString(), "-3x^2 - 2x + 1");
	}
	@Test
	public void testEquals() {
		Polynomial p = new SparsePolynomial("3x^2 + 2x - x^-2");
		Polynomial q = new SparsePolynomial("3x^2 + 2x - x^-2");
		assertEquals(p, q);
		assertEquals(q,q);
		Polynomial k = new SparsePolynomial("5x^2 + 4x - 3x^-2");
		Polynomial r = new SparsePolynomial("2x^2 + 2x - 2x^-2");
		assertEquals(k.subtract(r), p);
	}
	
}
	
	
	