package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PersonTests {

	@Test
	void testFirstNameGetterSetter() {
		Person person = new Person();
		person.setFirstName("John");
		assertThat(person.getFirstName()).isEqualTo("John");
	}

	@Test
	void testLastNameGetterSetter() {
		Person person = new Person();
		person.setLastName("Doe");
		assertThat(person.getLastName()).isEqualTo("Doe");
	}

	@Test
	void testDefaultFirstNameIsNull() {
		Person person = new Person();
		assertThat(person.getFirstName()).isNull();
	}

	@Test
	void testDefaultLastNameIsNull() {
		Person person = new Person();
		assertThat(person.getLastName()).isNull();
	}

}
