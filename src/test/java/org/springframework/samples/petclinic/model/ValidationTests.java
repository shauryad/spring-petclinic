package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Locale;

class ValidationTests {

	private Validator validator;

	@BeforeEach
	void setup() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
		factory.afterPropertiesSet();
		this.validator = factory;
	}

	@Test
	void shouldValidateOwnerWithAllFieldsSet() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isEmpty();
	}

	@Test
	void shouldRejectOwnerWithEmptyFirstName() {
		Owner owner = new Owner();
		owner.setFirstName("");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectOwnerWithEmptyLastName() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectOwnerWithEmptyAddress() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectOwnerWithEmptyCity() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("");
		owner.setTelephone("6085551023");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectOwnerWithEmptyTelephone() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectOwnerWithNonDigitTelephone() {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("abcdefghij");
		Set<ConstraintViolation<Owner>> violations = validator.validate(owner);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectVisitWithEmptyDescription() {
		Visit visit = new Visit();
		visit.setDescription("");
		Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
		assertThat(violations).isNotEmpty();
		assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description"))).isTrue();
	}

	@Test
	void shouldValidateVisitWithDescription() {
		Visit visit = new Visit();
		visit.setDescription("Routine checkup");
		Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
		assertThat(violations).isEmpty();
	}

	@Test
	void shouldRejectPersonWithNullFirstName() {
		Person person = new Person();
		person.setLastName("Smith");
		Set<ConstraintViolation<Person>> violations = validator.validate(person);
		assertThat(violations).isNotEmpty();
	}

	@Test
	void shouldRejectPersonWithNullLastName() {
		Person person = new Person();
		person.setFirstName("John");
		Set<ConstraintViolation<Person>> violations = validator.validate(person);
		assertThat(violations).isNotEmpty();
	}

}
