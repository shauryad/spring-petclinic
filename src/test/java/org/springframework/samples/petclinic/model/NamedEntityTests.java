package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NamedEntityTests {

	@Test
	void testNameGetterSetter() {
		NamedEntity entity = new NamedEntity();
		assertThat(entity.getName()).isNull();
		entity.setName("TestName");
		assertThat(entity.getName()).isEqualTo("TestName");
	}

	@Test
	void testToString() {
		NamedEntity entity = new NamedEntity();
		entity.setName("MyEntity");
		assertThat(entity.toString()).isEqualTo("MyEntity");
	}

	@Test
	void testToStringWhenNameIsNull() {
		NamedEntity entity = new NamedEntity();
		assertThat(entity.toString()).isNull();
	}

}
