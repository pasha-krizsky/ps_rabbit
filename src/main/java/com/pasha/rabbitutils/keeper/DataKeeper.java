package com.pasha.rabbitutils.keeper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
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

    public DataKeeper() {
        this.requestAndResponseQueues = new HashMap<>();
        this.queueAndRequests = new HashMap<>();
        this.queueAndResponses = new HashMap<>();
    }
}
