package com.pasha.rabbitutils.keeper;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Keeps data in mind.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
@Getter
public class DataKeeper {

    private Map<String, Set<String>> requestAndResponseQueues;
    private Map<String, Set<String>> queueAndRequests;
    private Map<String, Set<String>> queueAndResponses;
    private Set<String> queuesNowAreListened;

    @Setter
    private String URI;

    public DataKeeper() {
        this.requestAndResponseQueues = new HashMap<>();
        this.queueAndRequests = new HashMap<>();
        this.queueAndResponses = new HashMap<>();
        this.queuesNowAreListened = new HashSet<>();
    }
}
