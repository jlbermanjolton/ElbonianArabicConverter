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
    public void ElbonianToArabicOneUppercaseTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("X");
        assertEquals(converter.toArabic(), 10);
    }

    @Test
    public void ElbonianToArabicSameTwoUppercaseTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("XX");
        assertEquals(converter.toArabic(), 20);
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianToArabicSameFourLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("XXXX");
    }

    @Test()
    public void ElbonianToArabicUppercaseValueTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("MDCLXVI");
        assertEquals(converter.toArabic(), 1666);
    }

    @Test()
    public void ElbonianToArabicLowercaseValueTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("dDlLvV");
        assertEquals(converter.toArabic(), 444);
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianUppercaseOutOfOrderTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("IV");
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianOneLowercaseTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("l");
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianLowercaseOutOfOrderTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("lV");
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianRedundantTwoLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("dDC");
    }

    @Test(expected = MalformedNumberException.class)
    public void ElbonianRedundantOneLetterTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("dDD");
    }

    @Test(expected = ValueOutOfBoundsException.class)
    public void ArabicTooLargeTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("4999");
    }

    @Test(expected = ValueOutOfBoundsException.class)
    public void ArabicTooSmallTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("0");
    }

    @Test
    public void ArabicToElbonianUppercaseTest() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("1666");
        assertEquals(converter.toElbonian(), "MDCLXVI");
    }

    @Test
    public void ArabicToElbonianMultipleOf4Test() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("444");
        assertEquals(converter.toElbonian(), "dDlLvV");
    }

    @Test
    public void ArabicToElbonianMultipleOf9Test() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("3999");
        assertEquals(converter.toElbonian(), "MMMDdDLlLVvV");
    }

    @Test(expected = MalformedNumberException.class)
    public void malformedNumberTest1() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("O");
        converter.toArabic();
    }

    @Test(expected = MalformedNumberException.class)
    public void malformedNumberTest3() throws MalformedNumberException, ValueOutOfBoundsException {
        ElbonianArabicConverter converter = new ElbonianArabicConverter("VV");
    }

}
