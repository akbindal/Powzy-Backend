package com.powzy.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import com.googlecode.objectify.TxnType;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Transact 
{
	TxnType value();
}
