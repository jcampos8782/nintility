package me.jasoncampos.validation;

import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.HashSet;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
		when(validator.validate(any())).thenReturn(new HashSet<>(Arrays.asList(mock(ConstraintViolation.class))));
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

}
