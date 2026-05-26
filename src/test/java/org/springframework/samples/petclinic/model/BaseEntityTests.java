package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BaseEntityTests {

	@Test
	void testIdGetterSetter() {
		BaseEntity entity = new BaseEntity();
		assertThat(entity.getId()).isNull();
		entity.setId(42);
		assertThat(entity.getId()).isEqualTo(42);
	}

	@Test
	void testIsNewWhenIdIsNull() {
		BaseEntity entity = new BaseEntity();
		assertThat(entity.isNew()).isTrue();
	}

	@Test
	void testIsNewWhenIdIsSet() {
		BaseEntity entity = new BaseEntity();
		entity.setId(1);
		assertThat(entity.isNew()).isFalse();
	}

}
