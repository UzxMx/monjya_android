package com.monjya.android.order;

import android.content.res.Resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.monjya.android.R;
import com.monjya.android.product.Product;
import com.monjya.android.visitor.Visitor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xmx on 2017/3/12.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order implements Serializable {

    public static final String STATUS_NEW_CREATED = "new_created";

    public static final String STATUS_PAID = "paid";

    private Long id;

    private String oid;

    private Date startDate;

    private String startPlace;

    private List<Visitor> visitors;

    private float price;

    private String status;

    private Product product;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("oid")
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @JsonProperty("start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("start_place")
    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    @JsonProperty("visitors")
    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    @JsonProperty("price")
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("product")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getStatusDescription(Resources resources) {
        int res = -1;
        if (STATUS_NEW_CREATED.equals(status)) {
            res = R.string.unpaid;
        } else if (STATUS_PAID.equals(status)) {
            res = R.string.paid;
        }

        if (res > 0) {
            return resources.getString(res);
        } else {
            return "";
        }
    }
}
