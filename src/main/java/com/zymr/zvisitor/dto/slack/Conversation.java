package com.zymr.zvisitor.dto.slack;

import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conversation {

    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("members")
    private Set<String> members;

    @JsonProperty("error")
    private String error;

    @JsonProperty("response_metadata")
    private ResponseMeta responseMeta;

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ResponseMeta getResponseMeta() {
        return responseMeta;
    }

    public void setResponseMeta(ResponseMeta responseMeta) {
        this.responseMeta = responseMeta;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String toResponse() {
        return "Conversation [ok=" + ok + ", error=" + error + "]";
    }
}
