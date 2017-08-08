package com.pasha.rabbitutils.util;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class JSONMapper {

    public static String throwStringFromRequest(
            List<String> listOfJSONObjectsToMap,
            String messageToResponse,
            String fullRequestMessage) {

        String oldString = "";
        String newString = "";

        for (int i = 0; i < listOfJSONObjectsToMap.size() - 1; i += 2) {

            List<String> jsonToMapFrom = Arrays.asList(listOfJSONObjectsToMap.get(i).split("\\."));
            List<String> jsonToMapTo = Arrays.asList(listOfJSONObjectsToMap.get(i+1).split("\\."));

            // Get JSON to map from
            JSONObject jsonToMapF = new JSONObject(fullRequestMessage);

            for (int j = 0; j < jsonToMapFrom.size()-1; ++j) {
                jsonToMapF = (JSONObject) jsonToMapF.get(jsonToMapFrom.get(j));
            }

            newString = (String) jsonToMapF.get(jsonToMapFrom.get(jsonToMapFrom.size()-1));
            System.out.println("New string: " + newString);

            JSONObject responseJSON = new JSONObject(messageToResponse);

            for (int j = 0; j < jsonToMapTo.size()-1; ++j) {
                responseJSON = (JSONObject) responseJSON.get(jsonToMapTo.get(j));
            }

            oldString = (String) responseJSON.get(jsonToMapTo.get(jsonToMapTo.size()-1));
            System.out.println("Old string: " + oldString);
            messageToResponse = messageToResponse.replaceFirst(oldString, newString);
        }

        return messageToResponse;
    }
}
