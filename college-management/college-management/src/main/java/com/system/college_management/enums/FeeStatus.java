package com.system.college_management.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FeeStatus {

	Paid, Pending;
	
	 @JsonCreator
	    public static FeeStatus fromValue(String value) {
	        if ("true".equalsIgnoreCase(value)) return Paid;
	        if ("false".equalsIgnoreCase(value)) return Pending;
	        if ("Paid".equalsIgnoreCase(value)) return Paid;
	        if ("Pending".equalsIgnoreCase(value)) return Pending;
	        throw new IllegalArgumentException("Invalid FeeStatus value: " + value);
	    }
	 @JsonValue
	    public String toValue() {
	        return this.name();
	    }
}
