package me.jasoncampos.nintility.validation;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor for executing bean validation using javax.validation annotations. The interceptor does not perform
 * validation of annotations placed in the method signature. For example, the following is NOT supported:
 * <br />
 *
 * <pre>
 * // Size constraint is *not* enforced
 * public void doSometing(@Size(min=5,max=10) String something) {
 *     ...
 * }
 * </pre>
 *
 * @author Jason Campos <jcampos8782@gmail.com>
 */
public class ValidationInterceptor implements MethodInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(ValidationInterceptor.class);
	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Validator validator = factory.getValidator();
		final Object[] arguments = invocation.getArguments();
		final Set<ConstraintViolation<?>> allViolations = new LinkedHashSet<>();

		logger.debug("Intercepted {}.{} for argument validation", invocation.getMethod().getClass().getSimpleName(), invocation.getMethod());
		for (final Object object : arguments) {
			final Set<?> violations = validator.validate(object);
			for (final Object obj : violations) {
				allViolations.add((ConstraintViolation<?>) obj);
			}
		}

		if (!allViolations.isEmpty()) {
			if (logger.isErrorEnabled()) {
				logViolations(allViolations);
			}

			throw new ConstraintViolationException(
					String.format("Validation failed: %s.%s (%d failures)",
							invocation.getMethod().getDeclaringClass().getSimpleName(),
							invocation.getMethod().getName(),
							allViolations.size()),
					allViolations);
		}

		return invocation.proceed();
	}

	private void logViolations(final Set<ConstraintViolation<?>> violations) {
		for (final ConstraintViolation<?> violation : violations) {
			logger.debug(String.format("%s.%s %s (value=%s)",
					violation.getRootBeanClass().getSimpleName(),
					violation.getPropertyPath(),
					violation.getMessage(),
					violation.getInvalidValue()));
		}
	}
}
