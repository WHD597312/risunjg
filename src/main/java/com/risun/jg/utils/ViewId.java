package com.risun.jg.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by whd on 2017/12/22.
 */

@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewId {
    int id() default 0;
}
