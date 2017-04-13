package converter;

import converter.exceptions.MalformedNumberException;
import converter.exceptions.ValueOutOfBoundsException;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

/**
 * This class implements a converter that takes a string that represents a number in either the
 * Elbonian or Arabic numeral form. This class has methods that will return a value in the chosen form.
 *
 * @version 3/18/17
 */
public class ElbonianArabicConverter {

    // A string that holds the number (Elbonian or Arabic) you would like to convert
    private final String number;
    private Integer arabicValue;
    private BiMap<String, Integer> conversions;
    private ArrayList<Integer> sortedArabic;


    /**
     * Constructor for the ElbonianArabic class that takes a string. The string should contain a valid
     * Elbonian or Arabic numeral. The String can have leading or trailing spaces. But there should be no
     * spaces within the actual number (ie. "9 9" is not ok, but " 99 " is ok). If the String is an Arabic
     * number it should be checked to make sure it is within the Elbonian number systems bounds. If the
     * number is Elbonian, it must be a valid Elbonian representation of a number.
     *
     * @param number A string that represents either a Elbonian or Arabic number.
     * @throws MalformedNumberException Thrown if the value is an Elbonian number that does not conform
     * to the rules of the Elbonian number system. Leading and trailing spaces should not throw an error.
     * @throws ValueOutOfBoundsException Thrown if the value is an Arabic number that cannot be represented
     * in the Elbonian number system.
     */
    public ElbonianArabicConverter(String number) throws MalformedNumberException, ValueOutOfBoundsException {
        this.number = number;
        initConversions();
        checkValid();
    }

    private void initConversions() {
        conversions = new BiMap<String, Integer>();
        sortedArabic = new ArrayList<Integer>();
        conversions.put("M", 1000);
        conversions.put("D", 500);
        conversions.put("C", 100);
        conversions.put("L", 50);
        conversions.put("X", 10);
        conversions.put("V", 5);
        conversions.put("I", 1);
        conversions.put("d", -100);
        conversions.put("l", -10);
        conversions.put("v", -1);
        for (BiMap.Entry entry : conversions.entrySet()) {
            sortedArabic.add((Integer) entry.getRight());
            sortedArabic.add((Integer) entry.getRight());
            sortedArabic.add((Integer) entry.getRight());
        }
        Collections.sort(sortedArabic);
        Collections.reverse(sortedArabic);
    }

    /**
     * Converts the number to an Arabic numeral or returns the current value as an int if it is already
     * in the Arabic form.
     *
     * @return An arabic value
     */
    public int toArabic() throws MalformedNumberException{
        if (isArabic()) {
            return arabicValue;
        }
        int sum = 0;
        for (String letter : number.split("")) {
            sum += getArabic(letter);
        }
        return sum;
    }

    /**
     * Converts the number to an Elbonian numeral or returns the current value if it is already in the Elbonian form.
     *
     * @return An Elbonian value
     */
    public String toElbonian() {
        if (!isArabic()) {
            return number;
        }
        String elbonian = "";
        String nextElbonian;
        Integer arabic = arabicValue;
        while (arabic > 0) {
            Integer firstDigit = getFirstDigit(arabic);
            if (arabic < 4000 && firstDigit == 4) {
                Pair<String, Integer> pair = getMultipleOf4(arabic);
                elbonian += pair.getKey();
                arabic -= pair.getValue();
            } else if (firstDigit == 9) {
                Pair<String, Integer> pair = getMultipleOf9(arabic);
                elbonian += pair.getKey();
                arabic -= pair.getValue();
            } else {
                nextElbonian = getLargestElbonian(arabic);
                elbonian += nextElbonian;
                try {
                    arabic -= getArabic(nextElbonian);
                } catch (MalformedNumberException e) {

                }
            }
        }
        return elbonian;
    }

    private Pair<String,Integer> getMultipleOf9(Integer number) {
        Integer multiplier = new Double(Math.pow(10, number.toString().length() - 1)).intValue();
        String elbonian = getElbonian(5 * multiplier);
        Integer arabic = 9 * multiplier;
        return new Pair<>(elbonian + elbonian.toLowerCase() + elbonian, arabic);
    }

    private Pair<String, Integer> getMultipleOf4(Integer number) {
        Integer multiplier = new Double(Math.pow(10, number.toString().length() - 1)).intValue();
        String elbonian = getElbonian(5 * multiplier);
        Integer arabic = 4 * multiplier;
        return new Pair<>(elbonian.toLowerCase() + elbonian, arabic);
    }

    private Integer getFirstDigit(Integer number) {
        return Integer.parseInt(Integer.toString(number).substring(0, 1));
    }

    private String getLargestElbonian(int arabic) {
        for (Integer number: sortedArabic) {
            if (number <= arabic) {
                sortedArabic.remove(number);
                return getElbonian(number);
            }
        }
        throw new RuntimeException();
    }

    private void checkValid() throws MalformedNumberException, ValueOutOfBoundsException {
        if (isArabic()) {
            checkBounds();
        } else {
            checkLetters();
            checkThreeOfAnyLetter();
            checkCapitalOutOfOrder();
            checkLowercase();
            checkRedundant();
        }
    }

    private void checkLetters() throws MalformedNumberException {
        for (String letter : number.split("")) {
            getArabic(letter);
        }
    }

    private void checkBounds() throws ValueOutOfBoundsException {
        if (1 > arabicValue || arabicValue > 4443) {
            throw new ValueOutOfBoundsException("Number must be between 1 and 4443");
        }
    }

    private void checkRedundant() throws MalformedNumberException {
        checkMultipleOf4();
        checkMultipleOf5();
        checkMultipleOf9();
    }

    private void checkMultipleOf5() throws MalformedNumberException {
        if ((number.contains("DD") && !number.contains("MMM"))) {
            throw redundantLetters("DD", "M");
        } else if ((number.contains("LL") && !number.contains("CCC"))) {
            throw redundantLetters("LL", "C");
        } else if ((number.contains("VV") && !number.contains("XXX"))) {
            throw redundantLetters("VV", "X");
        }
    }

    private void checkMultipleOf4() throws MalformedNumberException {
        for (int i = 0; i < number.length() - 2; i++) {
            if (Character.isLowerCase(number.charAt(i)) &&
                    getArabic(number.charAt(i)) ==  -1 * getArabic(number.charAt(i + 2))) {
                throw redundantLetters(number.substring(i, i + 2), Character.toString(number.charAt(i + 1)));
            }
        }
    }

    private void checkMultipleOf9() throws MalformedNumberException {
        for (int i = 0; i < number.length() - 2; i++) {
            if (Character.isLowerCase(number.charAt(i)) &&
                    getArabic(Character.toUpperCase(number.charAt(i))).equals(getArabic(number.charAt(i + 2)))) {
                throw redundantLetters(number.substring(i, i + 2),
                        String.format("%c%c%c", number.charAt(i + 2), number.charAt(i), number.charAt(i + 2)));
            }
        }
    }

    private MalformedNumberException redundantLetters(String redundant, String compact) {
        return new MalformedNumberException(redundant + " could be simplfied to " + compact);
    }

    private void checkLowercase() throws MalformedNumberException {
        // lowercase does not come before uppercase
        char last = number.charAt(number.length() - 1);
        if (Character.isLowerCase(last)) {
            throw lowercaseOutOfOrder(last);
        }
        for (int i = 0; i < number.length() - 1; i++) {
            if (Character.isLowerCase(number.charAt(i)) &&
                    Character.toUpperCase(number.charAt(i)) != number.charAt(i + 1)) {
                throw lowercaseOutOfOrder(number.charAt(i));
            }
        }
    }

    private MalformedNumberException lowercaseOutOfOrder(char letter) {
        return new MalformedNumberException(letter + " must appears before " + Character.toUpperCase(letter));
    }

    private void checkCapitalOutOfOrder() throws MalformedNumberException {
        for (int i = 1; i < number.length(); i++) {
            if (Character.isLowerCase(number.charAt(i)) || Character.isLowerCase(number.charAt(i - 1)) ) {
                continue;
            }
            if (getArabic(number.charAt(i)) > getArabic(number.charAt(i - 1))) {
                throw new MalformedNumberException(number.charAt(i - 1) + " appears before " + number.charAt(i));
            }
        }
    }

    private void checkThreeOfLetter(String letter, Long count) throws MalformedNumberException{
        if (count > 3) {
            throw new MalformedNumberException(letter.toUpperCase() + " occurred " + count + " times");
        }
    }

    private void checkThreeOfAnyLetter() throws MalformedNumberException {
        Map<String, Long> frequentChars = getLetterFrequencies();
        for (Map.Entry<String, Long> entry : frequentChars.entrySet()){
            checkThreeOfLetter(entry.getKey(), entry.getValue());
        }
    }

    private Integer getArabic(char elbonian) throws MalformedNumberException {
        return getArabic(Character.toString(elbonian));
    }

    private Integer getArabic(String elbonian) throws MalformedNumberException {
        if (conversions.get(elbonian) != null) {
            return conversions.get(elbonian).getRight();
        } else {
            throw new MalformedNumberException("Unrecognized numeral: " + elbonian);
        }
    }

    private String getElbonian(Integer arabic) {
        if (conversions.get(arabic) != null) {
            return conversions.get(arabic).getLeft();
        } else {
            throw new RuntimeException();
        }
    }

    private Map<String, Long> getLetterFrequencies() {
        return stream(number.toLowerCase().split("")).collect(groupingBy(c -> c, counting()));
    }

    private boolean isArabic() {
        try {
            arabicValue = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}

class BiMap<T1, T2> {
    class Entry<T1, T2>
    {
        private final T1 item1;
        private final T2 item2;

        Entry(T1 item1, T2 item2)
        {
            this.item1 = item1;
            this.item2 = item2;
        }

        public T1 getLeft() {
            return item1;
        }

        public T2 getRight() {
            return item2;
        }
    }

    private final HashMap< Object, Entry<T1, T2> > mappings = new HashMap<>();
    private int loopedLinkCount;

    void put(T1 item1, T2 item2)
    {
        remove(item1);
        remove(item2);
        Entry<T1, T2> entry = new Entry<T1, T2>( item1, item2 );
        mappings.put( item1, entry );
        mappings.put( item2, entry );
        if( Objects.equals(item1, item2) ){
            loopedLinkCount++;
        }
    }

    /**
     *
     * @param key
     * @return an entry containing the key and it's one to one mapping or null if there is
     * no mapping for the key.
     */
    Entry<T1, T2> get(Object key)
    {
        return mappings.get(key);
    }

    Entry<T1, T2> remove(Object key)
    {
        Entry<T1, T2> entry = mappings.remove( key );
        if( entry == null ){
            return null;
        }
        if( Objects.equals( entry.item2, entry.item1 ) ){
            loopedLinkCount--;
            return entry;
        }
        return mappings.remove( Objects.equals( key, entry.item1 )  ?  entry.item2  :  entry.item1 );
    }

    public Set< Entry<T1, T2> > entrySet()
    {
        return new HashSet<>( mappings.values() );
    }

    public int size()
    {
        return ( mappings.size() + loopedLinkCount ) / 2;
    }
}
