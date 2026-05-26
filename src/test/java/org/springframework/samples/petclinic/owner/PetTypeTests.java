package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PetTypeTests {

	@Test
	void testPetTypeInheritsFromNamedEntity() {
		PetType petType = new PetType();
		petType.setName("dog");
		petType.setId(1);
		assertThat(petType.getName()).isEqualTo("dog");
		assertThat(petType.getId()).isEqualTo(1);
	}

	@Test
	void testToString() {
		PetType petType = new PetType();
		petType.setName("cat");
		assertThat(petType.toString()).isEqualTo("cat");
	}

	@Test
	void testDefaultNameIsNull() {
		PetType petType = new PetType();
		assertThat(petType.getName()).isNull();
	}

}
