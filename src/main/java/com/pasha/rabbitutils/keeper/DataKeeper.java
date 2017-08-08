package com.pasha.rabbitutils.keeper;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Keeps data in mind.
 *
 * Created by Pavel.Krizskiy on 8/6/2017.
 */
@Getter
public class DataKeeper {

    private Map<String, List<String>> requestAndResponseQueues;
    private Map<String, List<String>> queueAndRequests;
    private Map<String, List<String>> queueAndResponses;

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
