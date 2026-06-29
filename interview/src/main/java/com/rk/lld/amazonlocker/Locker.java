package com.rk.lld.amazonlocker;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Locker {

    private List<Compartment> compartments;
    private Map<String, AccessToken> accessTokenMap;
    private final Random random;

    Locker(List<Compartment> compartments) {
        this.compartments = compartments;
        accessTokenMap = new HashMap<>();
        random = new Random();
    }

    /*
     * Core logic
     * - Find all the compartments of that particular size
     * - open the compartment
     * -- deposit
     * - mark compartment as occupied
     * - generate the access token
     * - store the accesstoken
     * - return acesstoken
     * Edge cases:
     * - if no compartment with the size
     */
    public AccessToken deposit(CompartmentSize size) {
        Compartment compartment = getCompartment(size);
        if (compartment == null) {
            throw new InvalidParameterException("Could not find Compartment of size " + size);
        }
        compartment.open();
        AccessToken accessToken = generateAccessToken(compartment);
        compartment.markOccupied();
        String code = accessToken.getCode();
        accessTokenMap.put(code, accessToken);
        return accessToken;
    }

    /*
     * Core Logic:
     * - Find the access token for the code
     * - get the compartment from the accesstoken
     * - open the compartment
     * - mark that compartment back to free
     * - remove access token from map
     * Edge Cases:
     * - code is not empty
     * - code is not expired
     * - code exists in map
     */
    public void pickup(String code) {
        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Invalid code");
        }
        if (!accessTokenMap.containsKey(code)) {
            throw new InvalidParameterException("Invalid code");
        }
        AccessToken accessToken = accessTokenMap.get(code);
        if (accessToken.isExpired()) {
            throw new InvalidParameterException("Access Token expired");
        }
        Compartment compartment = accessToken.getCompartment();
        compartment.open();
        compartment.markFree();
        accessTokenMap.remove(code);
    }

    private AccessToken generateAccessToken(Compartment compartment) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        Instant expiration = Instant.now().plus(7, ChronoUnit.DAYS);
        return new AccessToken(code, expiration, compartment);
    }

    private Compartment getCompartment(CompartmentSize size) {
        Compartment compartment = null;
        for (Compartment c : compartments) {
            if (c.getSize() == size && !c.isOccupied())
                return c;
        }
        return compartment;
    }
}
