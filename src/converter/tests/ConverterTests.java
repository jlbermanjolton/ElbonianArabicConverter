package converter.tests;

        import converter.ElbonianArabicConverter;
        import converter.exceptions.MalformedNumberException;
        import converter.exceptions.ValueOutOfBoundsException;
        import org.junit.Test;

        import static org.junit.Assert.assertEquals;

/**
 * Test cases for the ElbonianArabicConverter class.
 */
public class ConverterTests {

    @Test
    public void ElbonianToArabicSampleTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("1");
        assertEquals(converter.toElbonian(), "I");
    }

    @Test
    public void ArabicToElbonianSampleTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("I");
        assertEquals(converter.toArabic(), 1);
    }

    @Test(expected = MalformedNumberException.class)
    public void malformedNumberTest() throws MalformedNumberException, ValueOutOfBoundsException {
        throw new MalformedNumberException("TEST");
    }

    @Test(expected = ValueOutOfBoundsException.class)
    public void valueOutOfBoundsTest() throws MalformedNumberException, ValueOutOfBoundsException {
        throw new ValueOutOfBoundsException("TEST");
    }

    @Test
    public void ElbonianToArabicOneLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("X");
        assertEquals(converter.toArabic(), 10);
    }

    @Test
    public void ElbonianToArabicSameTwoLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("XX");
        assertEquals(converter.toArabic(), 20);
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianToArabicSameFourLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("XXXX");
        converter.toArabic();
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianToArabicLowercaseTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("vV");
        assertEquals(converter.toArabic(), 4);
        converter = new ElbonianArabicConverter("lL");
        assertEquals(converter.toArabic(), 40);
        converter = new ElbonianArabicConverter("dD");
        assertEquals(converter.toArabic(), 400);
        converter = new ElbonianArabicConverter("xX");
        converter.toArabic();
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianToArabicCapitalOutOfOrderTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("IV");
        converter.toArabic();
    }

}
