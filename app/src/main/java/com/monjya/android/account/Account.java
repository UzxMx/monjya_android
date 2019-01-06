package com.monjya.android.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by xmx on 2017/2/12.
 */

@DatabaseTable(tableName = "account")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @DatabaseField(columnName = "id", id = true)
    private Long id;

    @DatabaseField(columnName = "token")
    private String token;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
