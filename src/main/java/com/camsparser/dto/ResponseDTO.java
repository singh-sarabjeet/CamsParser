package com.camsparser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDTO<T> {

    private MetaDTO meta;
    private T data;

    @JsonProperty("meta")
    public MetaDTO getMetaDTO() {
        return meta;
    }

    public void setMetaDTO(MetaDTO meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }
}
