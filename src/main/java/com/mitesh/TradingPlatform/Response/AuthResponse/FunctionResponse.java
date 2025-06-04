package com.mitesh.TradingPlatform.Response.AuthResponse;
import lombok.Data;

import java.util.Map;

@Data
public class FunctionResponse {
    private String functionName;
    private Map<String, Object> argumentMap;

    public FunctionResponse() {}

    // Getters and Setters
    public String getFunctionName() { return functionName; }
    public void setFunctionName(String functionName) { this.functionName = functionName; }
    public Map<String, Object> getArgumentMap() { return argumentMap; }
    public void setArgumentMap(Map<String, Object> argumentMap) { this.argumentMap = argumentMap; }

    @Override
    public String toString() {
        return "FunctionResponse{" +
                "functionName='" + functionName + '\'' +
                ", argumentMap=" + argumentMap +
                '}';
    }


    private String directResponse;

    public String getDirectResponse() { return directResponse; }
    public void setDirectResponse(String directResponse) { this.directResponse = directResponse; }
}
