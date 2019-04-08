package j2script.access;

public class PrivateAccess implements Access {
    public int hashCode() {
        return 1;
    }

    public boolean equals(Object other) {
        return other instanceof PrivateAccess;
    }

    public String toString() {
        return "private";
    }
}