package me.jasoncampos.nintility.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method annotations used to indicate that the method parameters should be validated. Use in conjunction with
 * {@link ValidationInterceptor} and {@link ValidationModule} for automatic argument validation.
 *
 * @author Jason Campos <jcampos8782@gmail.com>
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
}
