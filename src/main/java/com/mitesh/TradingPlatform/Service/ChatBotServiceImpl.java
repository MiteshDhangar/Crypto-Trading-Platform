package com.mitesh.TradingPlatform.Service;

import com.mitesh.TradingPlatform.DTO.CoinDTO;
import com.mitesh.TradingPlatform.Response.AuthResponse.ApiResponse;
import com.mitesh.TradingPlatform.Response.AuthResponse.FunctionResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    String geminiApiKey;
    @Value("${gemini.api.url}")
    String geminiUrl;

    private long convertToLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    private double convertToDouble(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        return 0.0;
    }

    public CoinDTO makeApiRequest(String currencyName) throws Exception {

        String url = "https://api.coingecko.com/api/v3/coins/" + currencyName.toLowerCase();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody != null) {
            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

            CoinDTO coinDTO = new CoinDTO();
            coinDTO.setId((String) responseBody.get("id"));
            coinDTO.setName((String) responseBody.get("name"));
            coinDTO.setSymbol((String) responseBody.get("symbol"));
            if (image != null) {
                coinDTO.setImage((String) image.get("large"));
            }

            coinDTO.setCurrentPrice((convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd"))));
            coinDTO.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
            coinDTO.setMarketCapRank((int) marketData.get("market_cap_rank"));
            coinDTO.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
            coinDTO.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
            coinDTO.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));

            coinDTO.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")));
            coinDTO.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
            coinDTO.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
            coinDTO.setMarketCapChangePercentage24h(convertToDouble(marketData.get("market_cap_change_percentage_24h")));
            coinDTO.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
            coinDTO.setTotalSupply(convertToDouble(marketData.get("total_supply")));
            coinDTO.setAth(convertToLong(((Map<String, Object>) marketData.get("ath")).get("usd")));
            coinDTO.setAthChangePercentage(convertToLong(marketData.get("ath_change_percentage")));
            coinDTO.setAtl(convertToLong(((Map<String, Object>) marketData.get("atl")).get("usd")));
            coinDTO.setAtlChangePercentage(convertToLong(marketData.get("atl_change_percentage")));

            return coinDTO;
        }
        throw new Exception("Coin not found ");
    }

    public FunctionResponse getFunctionResponse(String prompt) {
        String geminiApiUrl = geminiUrl + geminiApiKey;

        JSONObject requestBodyJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "fetchCurrencyData")
                                                .put("description", "Fetches detailed, real-time market data for a specific cryptocurrency. Use this function when a user asks about specific metrics for a coin. Available data includes: coin ID, name, symbol, image URL, current price, market capitalization, market cap rank, total trading volume, 24-hour high, 24-hour low, 24-hour price change (absolute and percentage), 24-hour market cap change (absolute and percentage), circulating supply, total supply, all-time high (ATH) value, ATH percentage change, ATH date, all-time low (ATL) value, ATL date, ATL percentage change, and the last updated timestamp.")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyIdentifier", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description", "Cryptocurrency symbol (BTC), name (Bitcoin), or CoinGecko ID (bitcoin)")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyIdentifier")
                                                        )
                                                )
                                        )
                                )
                        )
                );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(geminiApiUrl, requestEntity, String.class);
            String responseBody = response.getBody();

            JSONObject jsonObject = new JSONObject(responseBody);
            if (!jsonObject.has("candidates") || jsonObject.getJSONArray("candidates").isEmpty()) {
                // Check for promptFeedback if no candidates
                if (jsonObject.has("promptFeedback")) {
                    JSONObject promptFeedback = jsonObject.getJSONObject("promptFeedback");
                    if (promptFeedback.has("blockReason")) {
                        return createTextResponse("My apologies, I cannot process that request due to safety reasons: " + promptFeedback.getString("blockReason"));
                    }
                }
                return createTextResponse("Sorry, I couldn't process that request.");
            }

            JSONObject firstCandidate = jsonObject.getJSONArray("candidates").getJSONObject(0);
            // Check if content is missing (e.g. blocked for safety)
            if (!firstCandidate.has("content")) {
                if (firstCandidate.has("finishReason") && "SAFETY".equals(firstCandidate.getString("finishReason"))) {
                    return createTextResponse("My apologies, I cannot provide a response due to safety guidelines.");
                }
                return createTextResponse("No response content found.");
            }

            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.isEmpty()) {
                return createTextResponse("No response parts found.");
            }

            JSONObject firstPart = parts.getJSONObject(0);
            FunctionResponse res = new FunctionResponse();

            if (firstPart.has("functionCall")) {
                JSONObject functionCall = firstPart.getJSONObject("functionCall");
                res.setFunctionName(functionCall.getString("name"));
                res.setArgumentMap(functionCall.getJSONObject("args").toMap());
            } else if (firstPart.has("text")) {
                res.setFunctionName("direct_response");
                res.setDirectResponse(firstPart.getString("text"));
            } else {
                return createTextResponse("I couldn't find a proper response.");
            }
            return res;

        } catch (Exception e) {
            // Log e.getMessage() or e.printStackTrace() for debugging
            return createTextResponse("Error processing your request: " + e.getMessage());
        }
    }

    private FunctionResponse createTextResponse(String text) {
        FunctionResponse res = new FunctionResponse();
        res.setFunctionName("direct_response");
        res.setDirectResponse(text);
        return res;
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        FunctionResponse res = getFunctionResponse(prompt);

        if ("direct_response".equals(res.getFunctionName())) {
            // MODIFICATION: Instead of using res.getDirectResponse(), call simpleChat
            String simpleChatResponse = simpleChat(prompt);
            return createApiResponse(simpleChatResponse);
        }

        if (!"fetchCurrencyData".equals(res.getFunctionName())) {
            // This case might be less likely to be hit if direct_response now calls simpleChat
            // but kept for robustness if getFunctionResponse changes or returns other function names.
            String simpleChatResponse = simpleChat(prompt); // Fallback to simpleChat
            return createApiResponse(simpleChatResponse);
        }

        String currencyIdentifier = (String) res.getArgumentMap().get("currencyIdentifier");
        if (currencyIdentifier == null || currencyIdentifier.isEmpty()) {
            // If Gemini tried to call fetchCurrencyData but didn't provide identifier
            return createApiResponse("Please specify a cryptocurrency for market data. For other queries, I'll do my best to answer directly.");
        }

        try {
            String coinId = resolveCoinId(currencyIdentifier);
            CoinDTO apiResponse = makeApiRequest(coinId);
            return processGeminiFinalResponse(prompt, res, currencyIdentifier, apiResponse);
        } catch (Exception e) {
            // If coin data fetching fails, provide a more general error.
            // Alternatively, could also try a simpleChat response here.
            return createApiResponse("Error fetching data for: " + currencyIdentifier + ". " + e.getMessage() + ". For other topics, feel free to ask.");
        }
    }

    private String resolveCoinId(String input) throws Exception {
        String searchUrl = "https://api.coingecko.com/api/v3/search?query=" + UriComponentsBuilder.fromPath(input.toLowerCase()).build().toUriString();
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(searchUrl, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("coins")) {
                // The CoinGecko API returns a list directly in the 'coins' field
                java.util.List<Map<String, Object>> coinsList = (java.util.List<Map<String, Object>>) responseBody.get("coins");
                if (coinsList != null && !coinsList.isEmpty()) {
                    // Try exact match first for symbol or name
                    for (Map<String, Object> coinMap : coinsList) {
                        if (input.equalsIgnoreCase((String) coinMap.get("symbol"))) {
                            return (String) coinMap.get("id");
                        }
                        if (input.equalsIgnoreCase((String) coinMap.get("name"))) {
                            return (String) coinMap.get("id");
                        }
                    }
                    // Fallback to first result's ID
                    return (String) coinsList.get(0).get("id");
                }
            }
            throw new Exception("No matching cryptocurrency found for: " + input);
        } catch (HttpClientErrorException.NotFound e) {
            throw new Exception("Cryptocurrency not found (API 404): " + input);
        } catch (Exception e) {
            // Catching other potential exceptions during parsing or request
            throw new Exception("Error resolving coin ID for '" + input + "': " + e.getMessage());
        }
    }

    private ApiResponse processGeminiFinalResponse(String prompt, FunctionResponse res,
                                                   String identifier, CoinDTO apiResponse) {
        try {
            JSONObject requestBody = new JSONObject()
                    .put("contents", new JSONArray()
                            .put(createUserMessage(prompt))
                            .put(createModelFunctionCall(res, identifier))
                            .put(createFunctionResponse(res, apiResponse))
                    )
                    .put("tools", new JSONArray()
                            .put(createToolDefinition()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

            RestTemplate restTemplate = new RestTemplate(); // Create new RestTemplate for this call
            ResponseEntity<String> response = restTemplate.postForEntity(
                    geminiUrl + geminiApiKey, request, String.class);

            return parseFinalResponse(response.getBody());
        } catch (Exception e) {
            // Log e.getMessage() or e.printStackTrace() for debugging
            return createApiResponse("Error generating final response: " + e.getMessage());
        }
    }

    private JSONObject createUserMessage(String prompt) {
        return new JSONObject()
                .put("role", "user")
                .put("parts", new JSONArray()
                        .put(new JSONObject().put("text", prompt)));
    }

    private JSONObject createModelFunctionCall(FunctionResponse res, String identifier) {
        return new JSONObject()
                .put("role", "model")
                .put("parts", new JSONArray()
                        .put(new JSONObject()
                                .put("functionCall", new JSONObject()
                                        .put("name", res.getFunctionName())
                                        .put("args", new JSONObject()
                                                .put("currencyIdentifier", identifier)))));
    }

    private JSONObject createFunctionResponse(FunctionResponse res, CoinDTO data) {
        // Convert CoinDTO to JSONObject for the function response
        JSONObject dataJson = new JSONObject();
        if (data != null) {
            dataJson.put("id", data.getId());
            dataJson.put("name", data.getName());
            dataJson.put("symbol", data.getSymbol());
            dataJson.put("image", data.getImage());
            dataJson.put("currentPrice", data.getCurrentPrice());
            dataJson.put("marketCap", data.getMarketCap());
            dataJson.put("marketCapRank", data.getMarketCapRank());
            dataJson.put("totalVolume", data.getTotalVolume());
            dataJson.put("high24h", data.getHigh24h());
            dataJson.put("low24h", data.getLow24h());
            dataJson.put("priceChange24h", data.getPriceChange24h());
            dataJson.put("priceChangePercentage24h", data.getPriceChangePercentage24h());
            dataJson.put("marketCapChange24h", data.getMarketCapChange24h());
            dataJson.put("marketCapChangePercentage24h", data.getMarketCapChangePercentage24h());
            dataJson.put("circulatingSupply", data.getCirculatingSupply());
            dataJson.put("totalSupply", data.getTotalSupply());
            dataJson.put("ath", data.getAth());
            dataJson.put("athChangePercentage", data.getAthChangePercentage());
            dataJson.put("atl", data.getAtl());
            dataJson.put("atlChangePercentage", data.getAtlChangePercentage());
        }

        return new JSONObject()
                .put("role", "function")
                .put("parts", new JSONArray()
                        .put(new JSONObject()
                                .put("functionResponse", new JSONObject()
                                        .put("name", res.getFunctionName())
                                        .put("response", new JSONObject()
                                                .put("result", dataJson))))); // Ensure 'result' contains the JSON object
    }

    private JSONObject createToolDefinition() {
        return new JSONObject()
                .put("functionDeclarations", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "fetchCurrencyData")
                                .put("description", "Get real-time cryptocurrency market data")
                                .put("parameters", new JSONObject()
                                        .put("type", "OBJECT")
                                        .put("properties", new JSONObject()
                                                .put("currencyIdentifier", new JSONObject()
                                                        .put("type", "STRING")
                                                        .put("description", "Cryptocurrency symbol, name, or ID")))
                                        .put("required", new JSONArray().put("currencyIdentifier")))));
    }

    private ApiResponse parseFinalResponse(String responseBody) {
        try {
            JSONObject responseJson = new JSONObject(responseBody);
            if (!responseJson.has("candidates") || responseJson.getJSONArray("candidates").isEmpty()) {
                if (responseJson.has("promptFeedback")) {
                    JSONObject promptFeedback = responseJson.getJSONObject("promptFeedback");
                    if (promptFeedback.has("blockReason")) {
                        return createApiResponse("My apologies, I cannot provide a final response due to safety reasons: " + promptFeedback.getString("blockReason"));
                    }
                }
                return createApiResponse("Failed to parse final response: No candidates found.");
            }
            JSONArray candidates = responseJson.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);

            if (!firstCandidate.has("content")) {
                if (firstCandidate.has("finishReason") && "SAFETY".equals(firstCandidate.getString("finishReason"))) {
                    return createApiResponse("My apologies, the final response could not be generated due to safety guidelines.");
                }
                return createApiResponse("Failed to parse final response: No content found in candidate.");
            }
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.isEmpty() || !parts.getJSONObject(0).has("text")) {
                return createApiResponse("Failed to parse final response: No text part found.");
            }
            String text = parts.getJSONObject(0).getString("text");
            return createApiResponse(text);
        } catch (Exception e) {
            // Log e.getMessage() or e.printStackTrace() for debugging
            return createApiResponse("Failed to parse final response: " + e.getMessage());
        }
    }

    private ApiResponse createApiResponse(String message) {
        ApiResponse response = new ApiResponse();
        response.setMessage(message);
        return response;
    }


    @Override
    public String simpleChat(String prompt) {
        String geminiApiUrl = geminiUrl + geminiApiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", prompt))))
                ).toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(geminiApiUrl, requestEntity, String.class);
            String responseBodyString = response.getBody();

            if (responseBodyString == null || responseBodyString.isEmpty()) {
                return "Received an empty response from the AI service.";
            }

            JSONObject responseJson = new JSONObject(responseBodyString);

            if (responseJson.has("promptFeedback")) {
                JSONObject promptFeedback = responseJson.getJSONObject("promptFeedback");
                if (promptFeedback.has("blockReason")) {
                    return "My apologies, I cannot respond to that prompt due to safety reasons: " + promptFeedback.getString("blockReason");
                }
            }

            if (!responseJson.has("candidates") || responseJson.getJSONArray("candidates").isEmpty()) {
                return "Sorry, I couldn't get a response for that.";
            }

            JSONArray candidates = responseJson.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);

            if (firstCandidate.has("finishReason") && "SAFETY".equals(firstCandidate.getString("finishReason"))) {
                return "My apologies, I cannot provide a response due to safety guidelines.";
            }

            if (!firstCandidate.has("content") || !firstCandidate.getJSONObject("content").has("parts") || firstCandidate.getJSONObject("content").getJSONArray("parts").isEmpty()) {
                return "Sorry, the response was not in the expected format.";
            }

            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject firstPart = parts.getJSONObject(0);

            if (firstPart.has("text")) {
                return firstPart.getString("text");
            } else {
                return "Sorry, I couldn't extract a text response.";
            }

        } catch (HttpClientErrorException e) {
            // Log e.getResponseBodyAsString() or e.getStatusCode() for more details
            return "Error communicating with the AI service: " + e.getStatusCode() + " " + e.getResponseBodyAsString();
        } catch (Exception e) {
            // Log e.getMessage() or e.printStackTrace() for debugging
            return "Error during simple chat: " + e.getMessage();
        }
    }
}