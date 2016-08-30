package me.jasoncampos.nintility.validation;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

/**
 * Binds automatic argument validation on methods annotated with {@link Validate}. Note that valation only occurs on
 * beans, not on method parameters annotated with javax.validation annotations (eg: {@code @Size}). See
 * {@link ValidationInterceptor} for more details.
 *
 * @author Jason Campos <jcampos8782@gmail.com>
 */
public class ValidationModule extends AbstractModule {
	private static final Logger logger = LoggerFactory.getLogger(ValidationModule.class);

	@Override
	protected void configure() {
		logger.info("Registering ValidationInterceptor");
		bindInterceptor(
				any(),
				annotatedWith(Validate.class),
				new ValidationInterceptor());
	}
}
