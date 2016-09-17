package me.jasoncampos.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.ConstraintDescriptor;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import me.jasoncampos.nintility.validation.ValidationInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class ValidationInterceptorTest {

	@Mock
	private Validator validator;

	@Mock
	ValidatorFactory validatorFactory;

	@Mock
	MethodInvocation invocation;

	private Object[] arguments;

	private ValidationInterceptor interceptor;

	@Before
	public void beforeEach() {
		this.interceptor = new ValidationInterceptor(validatorFactory);
		when(validatorFactory.getValidator()).thenReturn(validator);
		when(invocation.getMethod()).thenReturn(this.getClass().getMethods()[0]);
		arguments = new Object[] { mock(Object.class), mock(Object.class) };
		when(invocation.getArguments()).thenReturn(arguments);
	}

	@Test(expected = ConstraintViolationException.class)
	public void withValidationErrorsAnExceptionIsThrown() throws Throwable {
		final Set<ConstraintViolation<Object>> violations = new HashSet<>();
		violations.add(createViolation());
		when(validator.validate(any())).thenReturn(new HashSet<>(Arrays.asList(createViolation())));
		interceptor.invoke(invocation);
	}

	@Test
	public void itValidatesEveryArgument() throws Throwable {
		when(validator.validate(any())).thenReturn(new HashSet<ConstraintViolation<Object>>());
		interceptor.invoke(invocation);
		for (final Object argument : arguments) {
			verify(validator, times(1)).validate(argument);
		}
	}

	@Test
	public void itProceedsWithInvocationIfValidationPasses() throws Throwable {
		when(validator.validate(any())).thenReturn(new HashSet<ConstraintViolation<Object>>());
		interceptor.invoke(invocation);
		verify(invocation, times(1)).proceed();
	}

	private ConstraintViolation<Object> createViolation() {
		return new ConstraintViolation<Object>() {

			@Override
			public String getMessage() {
				return null;
			}

			@Override
			public String getMessageTemplate() {
				return null;
			}

			@Override
			public Object getRootBean() {
				return null;
			}

			@Override
			public Class<Object> getRootBeanClass() {
				return null;
			}

			@Override
			public Object getLeafBean() {
				return null;
			}

			@Override
			public Object[] getExecutableParameters() {
				return null;
			}

			@Override
			public Object getExecutableReturnValue() {
				return null;
			}

			@Override
			public Path getPropertyPath() {
				return null;
			}

			@Override
			public Object getInvalidValue() {
				return null;
			}

			@Override
			public ConstraintDescriptor<?> getConstraintDescriptor() {
				return null;
			}

			@Override
			public <U> U unwrap(final Class<U> type) {
				return null;
			}
		};
	}
}
