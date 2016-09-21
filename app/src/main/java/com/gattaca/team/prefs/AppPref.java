package com.gattaca.team.prefs;

import java.util.Set;

public enum AppPref {
    BitalinoMac(String.class),
    FakeGeneration(Boolean.class),;

    private final Class<?> type;

    AppPref(Class type) {
        this.type = type;
    }

    Class<?> getType() {
        return this.type;
    }

    public void reset() {
        if (this.type.equals(String.class)) {
            this.set(SharedPrefHelper.DEFAULT_String);
        } else if (this.type.equals(Integer.class)) {
            this.set(SharedPrefHelper.DEFAULT_Integer);
        } else if (this.type.equals(Boolean.class)) {
            this.set(SharedPrefHelper.DEFAULT_Boolean);
        } else if (this.type.equals(Long.class)) {
            this.set(SharedPrefHelper.DEFAULT_Long);
        } else if (this.type.equals(Float.class)) {
            this.set(SharedPrefHelper.DEFAULT_Float);
        } else if (this.type.equals(Set.class)) {
            this.set(SharedPrefHelper.DEFAULT_Hash);
        }
    }

    public void setIfNotExist(final Object value) {
        if (get(null) == null) {
            set(value);
        }
    }

    public void set(final Object value) {
        SharedPrefHelper.saveToSharedPref(this.name(), this.getType(), value);
    }

    private Object get(final Object defValue) {
        return SharedPrefHelper.getFromSharedPref(this.name(), this.getType(), defValue);
    }

    public Set getSet() {
        return (Set) this.get(null);
    }

    public String getStr() {
        return (String) this.get(null);
    }

    public String getStr(String a) {
        return (String) this.get(a);
    }

    public Integer getInt() {
        return (Integer) this.get(0);
    }

    public Integer getInt(int a) {
        return (Integer) this.get(a);
    }

    public Boolean getBool() {
        return (Boolean) this.get(false);
    }

    public Boolean getBool(boolean b) {
        return (Boolean) this.get(b);
    }

    public Long getLong() {
        return (Long) this.get(0l);
    }

    public Long getLong(long l) {
        return (Long) this.get(l);
    }

    public Float getFloat() {
        return (Float) this.get(0f);
    }

    public Float getFloat(float f) {
        return (Float) this.get(f);
    }
}
