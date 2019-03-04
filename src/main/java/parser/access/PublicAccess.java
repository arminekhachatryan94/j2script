package j2script.access;

public class PublicAccess implements Access {
    public int hashCode() {
        return 2;
    }

    public boolean equals(Object other) {
        return other instanceof PublicAccess;
    }

    public String toString() {
        return "public";
    }
}