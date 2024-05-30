package com.qa.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import DataHandlerUtils.JsonCompareUtils;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import org.openqa.selenium.json.Json;
import java.util.*;
public class JsonResponseHandler {
    public synchronized Map<String, JsonElement> validateResponseWithDuplicateKeyValuePairs(String currentKey,JsonElement element,String path,JsonElement ExpectedValue,Map<String, JsonElement> mismatchedElements) {
        String[] keySequence = path.split("\\.");
        JsonElement value=null;
        if(element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for(String key : jsonObject.keySet())
            {
                if(currentKey!="")
                {
                    String[] splittedKey = currentKey.split("[()]+");
                    if(splittedKey[0].equals(key)) {
                        value = jsonObject.get(key);
                        if(value.getAsString().equals(splittedKey[1]))
                        {
                            validateResponseWithDuplicateKeyValuePairs("",value, path,ExpectedValue,mismatchedElements);
                            currentKey="";
                            if(currentKey.equals("")) {
                                path = "";
                                for (int i = 1; i < keySequence.length; i++) {
                                    path = path + keySequence[i] + ".";
                                }
                            }
                            keySequence = path.split("\\.");
                        }else {
                            validateResponseWithDuplicateKeyValuePairs(currentKey,value,
                                    path,ExpectedValue,mismatchedElements);
                        }
                    }
                }
                else if(keySequence[0].contains("|") )
                {
                    String[] splittedKey = keySequence[0].split("[|]");
                    String key1 = splittedKey[0];
                    if(key1.equals(key)) {
                        value = jsonObject.get(key);
                        validateResponseWithDuplicateKeyValuePairs(splittedKey[1],value, path,ExpectedValue,mismatchedElements);
                    }
                }
                else if(keySequence.length==1 ) {
                    if(!jsonObject.get(keySequence[0]).equals(ExpectedValue))
                    {
                        mismatchedElements.put(keySequence[0],jsonObject.get(keySequence[0]));
                    }
                    break;
                }
                else {
                    if(keySequence[0].equals(key)) {
                        value = jsonObject.get(key);
                        validateResponseWithDuplicateKeyValuePairs("",value, path,ExpectedValue,mismatchedElements);
                    }
                }
            }
        }
        else if(element.isJsonArray()) {
            for(JsonElement arrayElement : element.getAsJsonArray())
            {
                if(currentKey!="" ) {
                    validateResponseWithDuplicateKeyValuePairs(currentKey, arrayElement, path,ExpectedValue,mismatchedElements);
                }else {
                    validateResponseWithDuplicateKeyValuePairs("", arrayElement, path,ExpectedValue,mismatchedElements);
                }
            }
        }
        else {
            System.out.println(element.getAsString());
        }
        return mismatchedElements;
    }
}