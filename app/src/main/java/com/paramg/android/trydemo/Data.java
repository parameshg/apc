package com.paramg.android.trydemo;

import org.json.JSONException;
import org.json.JSONObject;

public class Data implements JSONable
{
    public String proxyServerHost;
    public String proxyServerPort;
    public String proxyServerUsername;

    public String timestamp;
    public String httpMethod;
    public String url;
    public String userAgent;
    public String referrer;
    public String statusCode;
    public String contentType;
    public String sourceIpAddress;
    public String destinationIpAddress;

    @Override
    public String toString()
    {
        return toJSONObject().toString();
    }

    @Override
    public JSONObject toJSONObject()
    {
        JSONObject result = new JSONObject();

        try
        {
            result.put("proxyServerHost", proxyServerHost);
            result.put("proxyServerPort", proxyServerPort);
            result.put("proxyServerUsername", proxyServerUsername);
            result.put("timestamp", timestamp);
            result.put("httpMethod", httpMethod);
            result.put("url", url);
            result.put("userAgent", userAgent);
            result.put("referrer", referrer);
            result.put("statusCode", statusCode);
            result.put("contentType", contentType);
            result.put("sourceIpAddress", sourceIpAddress);
            result.put("destinationIpAddress", destinationIpAddress);
        }
        catch (JSONException e)
        {
            result = null;
        }

        return result;
    }

    @Override
    public void fromJSONObject(JSONObject json)
    {
        try
        {
            proxyServerHost = json.getString("proxyServerHost");
            proxyServerPort = json.getString("proxyServerPort");
            proxyServerUsername = json.getString("proxyServerUsername");
            timestamp = json.getString("timestamp");
            httpMethod = json.getString("httpMethod");
            url = json.getString("url");
            userAgent = json.getString("userAgent");
            referrer = json.getString("referrer");
            statusCode = json.getString("statusCode");
            contentType = json.getString("contentType");
            sourceIpAddress = json.getString("sourceIpAddress");
            destinationIpAddress = json.getString("destinationIpAddress");
        }
        catch (JSONException e)
        {
        }
    }
}