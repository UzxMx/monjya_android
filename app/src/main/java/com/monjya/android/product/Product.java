package com.monjya.android.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xmx on 2017/2/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Product implements Serializable {

    private Long id;

    private String name;

    private String briefDescription;

    private String address;

    private float price;

    private String thumbPhotoUrl;

    private List<String> photos;

    private String openTime;

    private String details;

    private String journey;

    private String playMethod;

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

    @JsonProperty("brief_description")
    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("price")
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @JsonProperty("thumb_photo_url")
    public String getThumbPhotoUrl() {
        return thumbPhotoUrl;
    }

    public void setThumbPhotoUrl(String thumbPhotoUrl) {
        this.thumbPhotoUrl = thumbPhotoUrl;
    }

    @JsonProperty("photos")
    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @JsonProperty("open_time")
    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @JsonProperty("journey")
    public String getJourney() {
        return journey;
    }

    public void setJourney(String journey) {
        this.journey = journey;
    }

    @JsonProperty("play_method")
    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }
}
