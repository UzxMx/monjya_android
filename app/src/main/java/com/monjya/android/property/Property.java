package com.monjya.android.property;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.monjya.android.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuemingxiang on 16-11-13.
 */

@DatabaseTable(tableName = "property")
public class Property {

    @DatabaseField(columnName = "id", generatedId = true)
    private Long id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "value")
    private String value;

    public Property() {
    }

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer toInteger() {
        if (StringUtils.isBlank(this.value)) {
            return null;
        }
        return Integer.parseInt(this.value);
    }

    public boolean toBoolean() {
        if (StringUtils.isBlank(this.value)) {
            return false;
        }
        return Boolean.parseBoolean(this.value);
    }

    public Date toDate(String format) {
        if (StringUtils.isBlank(this.value)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(this.value);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setDateValue(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        setValue(sdf.format(date));
    }

    public void setBooleanValue(Boolean value) {
        setValue(value.toString());
    }
}
