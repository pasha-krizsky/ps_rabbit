package com.pasha.rabbitutils.util;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/** Utils to work with JSON format.
 *
 *  Created by Pavel.Krizskiy on 8/8/2017.
 */
public class JSONMapper {

    /** Throws strings from request to response. */
    public static String throwStringFromRequest(
            List<String> listOfJSONObjectsToMap,
            String messageResponse,
            String messageRequest) {

        for (int i = 0; i < listOfJSONObjectsToMap.size() - 1; i += 2) {

            // Old str to replace
            String oldString;
            // New str to replace
            String newString;

            List<String> jsonToMapFrom = Arrays.asList(listOfJSONObjectsToMap.get(i).split("\\."));
            List<String> jsonToMapTo = Arrays.asList(listOfJSONObjectsToMap.get(i+1).split("\\."));

            JSONObject jsonToMapF = new JSONObject(messageRequest);

            for (int j = 0; j < jsonToMapFrom.size()-1; ++j) {
                jsonToMapF = (JSONObject) jsonToMapF.get(jsonToMapFrom.get(j));
            }

            newString = (String) jsonToMapF.get(jsonToMapFrom.get(jsonToMapFrom.size() - 1));
            System.out.println("New string: " + newString);

            JSONObject responseJSON = new JSONObject(messageResponse);

            for (int j = 0; j < jsonToMapTo.size()-1; ++j) {
                responseJSON = (JSONObject) responseJSON.get(jsonToMapTo.get(j));
            }

            oldString = (String) responseJSON.get(jsonToMapTo.get(jsonToMapTo.size()-1));
            System.out.println("Old string: " + oldString);
            messageResponse = messageResponse.replaceFirst(oldString, newString);
        }

        return messageResponse;
    }
}
