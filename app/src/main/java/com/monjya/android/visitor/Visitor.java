package com.monjya.android.visitor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by xmx on 2017/3/8.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Visitor implements Serializable {

    private Long id;

    private String name;

    private String telephone;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.id != null && this.id.equals(((Visitor) obj).getId())) {
            return true;
        }
        return super.equals(obj);
    }
}
