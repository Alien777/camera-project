package pl.lasota.crm.security;

import java.util.Arrays;

public class SecureString implements CharSequence {

    private final transient char[] value;

    public SecureString(char[] value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        this.value = value;
    }

    @Override
    public int length() {
        return value.length;
    }

    @Override
    public char charAt(int index) {
        return value[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        Arrays.fill(value, '\u0000');
    }

    public char[] toCharArray() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecureString that = (SecureString) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}