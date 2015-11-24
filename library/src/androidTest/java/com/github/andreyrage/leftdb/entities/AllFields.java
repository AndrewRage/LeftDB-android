package com.github.andreyrage.leftdb.entities;

import com.github.andreyrage.leftdb.annotation.ColumnAutoInc;
import com.github.andreyrage.leftdb.annotation.ColumnDAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rage on 11/18/15.
 */
public class AllFields {

    @ColumnAutoInc private long id;
    private short shortP;
    private Short shortW;
    private int integerP;
    private Integer integerW;
    private long longP;
    private Long longW;
    private float floatP;
    private Float floatW;
    private double doubleP;
    private Double doubleW;
    private boolean booleanP;
    private Boolean booleanW;
    private String stringO;
    private BigDecimal bigDecimalO;
    private Date dateO;
    private Calendar calendarO;
    @ColumnDAO private SerializableObject dao;
    private SerializableObject serialize;
    private ArrayList<SerializableObject> list;

    public AllFields() {
    }

    public AllFields(long id, short shortP, Short shortW, int integerP, Integer integerW, long longP, Long longW, float floatP, Float floatW, double doubleP, Double doubleW, boolean booleanP, Boolean booleanW, String stringO, BigDecimal bigDecimalO, Date dateO, Calendar calendarO, SerializableObject dao, SerializableObject serialize, ArrayList<SerializableObject> list) {
        this.id = id;
        this.shortP = shortP;
        this.shortW = shortW;
        this.integerP = integerP;
        this.integerW = integerW;
        this.longP = longP;
        this.longW = longW;
        this.floatP = floatP;
        this.floatW = floatW;
        this.doubleP = doubleP;
        this.doubleW = doubleW;
        this.booleanP = booleanP;
        this.booleanW = booleanW;
        this.stringO = stringO;
        this.bigDecimalO = bigDecimalO;
        this.dateO = dateO;
        this.calendarO = calendarO;
        this.dao = dao;
        this.serialize = serialize;
        this.list = list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllFields allFields = (AllFields) o;

        if (id != allFields.id) return false;
        if (shortP != allFields.shortP) return false;
        if (integerP != allFields.integerP) return false;
        if (longP != allFields.longP) return false;
        if (Float.compare(allFields.floatP, floatP) != 0) return false;
        if (Double.compare(allFields.doubleP, doubleP) != 0) return false;
        if (booleanP != allFields.booleanP) return false;
        if (shortW != null ? !shortW.equals(allFields.shortW) : allFields.shortW != null)
            return false;
        if (integerW != null ? !integerW.equals(allFields.integerW) : allFields.integerW != null)
            return false;
        if (longW != null ? !longW.equals(allFields.longW) : allFields.longW != null) return false;
        if (floatW != null ? !floatW.equals(allFields.floatW) : allFields.floatW != null)
            return false;
        if (doubleW != null ? !doubleW.equals(allFields.doubleW) : allFields.doubleW != null)
            return false;
        if (booleanW != null ? !booleanW.equals(allFields.booleanW) : allFields.booleanW != null)
            return false;
        if (stringO != null ? !stringO.equals(allFields.stringO) : allFields.stringO != null)
            return false;
        if (bigDecimalO != null ? !bigDecimalO.equals(allFields.bigDecimalO) : allFields.bigDecimalO != null)
            return false;
        if (dateO != null ? !dateO.equals(allFields.dateO) : allFields.dateO != null) return false;
        if (calendarO != null ? !calendarO.equals(allFields.calendarO) : allFields.calendarO != null)
            return false;
        if (dao != null ? !dao.equals(allFields.dao) : allFields.dao != null) return false;
        if (serialize != null ? !serialize.equals(allFields.serialize) : allFields.serialize != null)
            return false;
        return !(list != null ? !list.equals(allFields.list) : allFields.list != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) shortP;
        result = 31 * result + (shortW != null ? shortW.hashCode() : 0);
        result = 31 * result + integerP;
        result = 31 * result + (integerW != null ? integerW.hashCode() : 0);
        result = 31 * result + (int) (longP ^ (longP >>> 32));
        result = 31 * result + (longW != null ? longW.hashCode() : 0);
        result = 31 * result + (floatP != +0.0f ? Float.floatToIntBits(floatP) : 0);
        result = 31 * result + (floatW != null ? floatW.hashCode() : 0);
        temp = Double.doubleToLongBits(doubleP);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (doubleW != null ? doubleW.hashCode() : 0);
        result = 31 * result + (booleanP ? 1 : 0);
        result = 31 * result + (booleanW != null ? booleanW.hashCode() : 0);
        result = 31 * result + (stringO != null ? stringO.hashCode() : 0);
        result = 31 * result + (bigDecimalO != null ? bigDecimalO.hashCode() : 0);
        result = 31 * result + (dateO != null ? dateO.hashCode() : 0);
        result = 31 * result + (calendarO != null ? calendarO.hashCode() : 0);
        result = 31 * result + (dao != null ? dao.hashCode() : 0);
        result = 31 * result + (serialize != null ? serialize.hashCode() : 0);
        result = 31 * result + (list != null ? list.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AllFields{" +
                "id=" + id +
                ", shortP=" + shortP +
                ", shortW=" + shortW +
                ", integerP=" + integerP +
                ", integerW=" + integerW +
                ", longP=" + longP +
                ", longW=" + longW +
                ", floatP=" + floatP +
                ", floatW=" + floatW +
                ", doubleP=" + doubleP +
                ", doubleW=" + doubleW +
                ", booleanP=" + booleanP +
                ", booleanW=" + booleanW +
                ", stringO='" + stringO + '\'' +
                ", bigDecimalO=" + bigDecimalO +
                ", dateO=" + dateO +
                ", calendarO=" + calendarO +
                ", dao=" + dao +
                ", serialize=" + serialize +
                ", list=" + list +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getShortP() {
        return shortP;
    }

    public void setShortP(short shortP) {
        this.shortP = shortP;
    }

    public Short getShortW() {
        return shortW;
    }

    public void setShortW(Short shortW) {
        this.shortW = shortW;
    }

    public int getIntegerP() {
        return integerP;
    }

    public void setIntegerP(int integerP) {
        this.integerP = integerP;
    }

    public Integer getIntegerW() {
        return integerW;
    }

    public void setIntegerW(Integer integerW) {
        this.integerW = integerW;
    }

    public long getLongP() {
        return longP;
    }

    public void setLongP(long longP) {
        this.longP = longP;
    }

    public Long getLongW() {
        return longW;
    }

    public void setLongW(Long longW) {
        this.longW = longW;
    }

    public float getFloatP() {
        return floatP;
    }

    public void setFloatP(float floatP) {
        this.floatP = floatP;
    }

    public Float getFloatW() {
        return floatW;
    }

    public void setFloatW(Float floatW) {
        this.floatW = floatW;
    }

    public double getDoubleP() {
        return doubleP;
    }

    public void setDoubleP(double doubleP) {
        this.doubleP = doubleP;
    }

    public Double getDoubleW() {
        return doubleW;
    }

    public void setDoubleW(Double doubleW) {
        this.doubleW = doubleW;
    }

    public boolean isBooleanP() {
        return booleanP;
    }

    public void setBooleanP(boolean booleanP) {
        this.booleanP = booleanP;
    }

    public Boolean getBooleanW() {
        return booleanW;
    }

    public void setBooleanW(Boolean booleanW) {
        this.booleanW = booleanW;
    }

    public String getStringO() {
        return stringO;
    }

    public void setStringO(String stringO) {
        this.stringO = stringO;
    }

    public BigDecimal getBigDecimalO() {
        return bigDecimalO;
    }

    public void setBigDecimalO(BigDecimal bigDecimalO) {
        this.bigDecimalO = bigDecimalO;
    }

    public Date getDateO() {
        return dateO;
    }

    public void setDateO(Date dateO) {
        this.dateO = dateO;
    }

    public Calendar getCalendarO() {
        return calendarO;
    }

    public void setCalendarO(Calendar calendarO) {
        this.calendarO = calendarO;
    }

    public SerializableObject getDao() {
        return dao;
    }

    public void setDao(SerializableObject dao) {
        this.dao = dao;
    }

    public SerializableObject getSerialize() {
        return serialize;
    }

    public void setSerialize(SerializableObject serialize) {
        this.serialize = serialize;
    }

    public ArrayList<SerializableObject> getList() {
        return list;
    }

    public void setList(ArrayList<SerializableObject> list) {
        this.list = list;
    }
}
