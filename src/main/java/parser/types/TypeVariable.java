package j2script.types;

import j2script.names.Name;

public class TypeVariable implements Type {
    public boolean equals(Object obj) {
        return obj instanceof TypeVariable;
    }
}
