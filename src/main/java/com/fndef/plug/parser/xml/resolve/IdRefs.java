package com.fndef.plug.parser.xml.resolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class IdRefs {
    private final Map<String, IdRef> idRefs = new HashMap<>();
    private final Map<String, List<IdRef>> unresolvedIdRefs = new HashMap<>();

    List<IdRef> get() {
        if (! unresolvedIdRefs.isEmpty()) {
            throw new IllegalStateException("All references are not resolved ["+unresolvedIdRefs.keySet()+"]");
        }
        return new ArrayList<>(idRefs.values());
    }

    void registerId(IdRef idRef, List<String> refs) {
        idRefs.putIfAbsent(idRef.getId(), idRef);
        attemptResolve(idRef);
        for(String r : refs) {
            if (idRefs.containsKey(r)) {
                IdRef ref = idRefs.get(r);
                ref.addRef(idRef);
            } else {
                unresolvedIdRefs.putIfAbsent(r, new ArrayList<>());
                List<IdRef> unresolved = unresolvedIdRefs.get(r);
                unresolved.add(idRef);
            }
        }
    }

    private void attemptResolve(IdRef idRef) {
        List<IdRef> unresolved = unresolvedIdRefs.getOrDefault(idRef.getId(), new ArrayList<>());
        unresolved.forEach(idr -> idRef.addRef(idr));
        unresolvedIdRefs.remove(idRef.getId());
    }
}
