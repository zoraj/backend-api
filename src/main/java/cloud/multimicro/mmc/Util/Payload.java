package cloud.multimicro.mmc.Util;

import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;

public class Payload {

    private Payload() {
        throw new IllegalStateException();
    }

    /** 
     * All fields should be passed in as HashSet of String 
     *  Return a string containing the missing fields
    */
    public static String isOk(JsonObject payload, Set<String> mandatoryFields) {
        var missingFields = mandatoryFields.stream().filter(field -> !payload.containsKey(field)).collect(Collectors.toList());
        return missingFields.isEmpty() ? null : missingFields.toString();
    }
}
