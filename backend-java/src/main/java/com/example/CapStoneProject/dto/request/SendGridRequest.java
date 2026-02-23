package com.example.CapStoneProject.dto.request;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class SendGridRequest {

    @JsonProperty("event")
    private String event;

    @JsonProperty("email")
    private String email;

    @JsonProperty("timestamp")
    private Long timestamp;

    /**
     * SendGrid flattens custom arguments into the root JSON.
     * This Map will catch 'emailId' and any other custom args automatically.
     */
    private Map<String, String> customArgs = new HashMap<>();

    @JsonAnySetter
    public void addCustomArg(String key, String value) {
        this.customArgs.put(key, value);
    }
}