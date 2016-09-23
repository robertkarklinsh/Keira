package com.gattaca.team.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({RRType.A, RRType.a, RRType.J, RRType.S, RRType.V, RRType.r, RRType.N})
@Retention(RetentionPolicy.SOURCE)
public @interface RRType {
    String A = "A";
    String a = "a";
    String J = "J";
    String S = "D";
    String V = "V";
    String r = "r";
    String N = "N";
}
