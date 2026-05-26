package org.springframework.samples.petclinic.vet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SpecialtyTests {

	@Test
	void testSpecialtyInheritsFromNamedEntity() {
		Specialty specialty = new Specialty();
		specialty.setName("radiology");
		specialty.setId(1);
		assertThat(specialty.getName()).isEqualTo("radiology");
		assertThat(specialty.getId()).isEqualTo(1);
	}

	@Test
	void testToString() {
		Specialty specialty = new Specialty();
		specialty.setName("surgery");
		assertThat(specialty.toString()).isEqualTo("surgery");
	}

	@Test
	void testDefaultNameIsNull() {
		Specialty specialty = new Specialty();
		assertThat(specialty.getName()).isNull();
	}

}
