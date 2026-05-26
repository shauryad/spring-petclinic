package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PetTests {

	@Test
	void testBirthDateGetterSetter() {
		Pet pet = new Pet();
		LocalDate date = LocalDate.of(2020, 1, 1);
		pet.setBirthDate(date);
		assertThat(pet.getBirthDate()).isEqualTo(date);
	}

	@Test
	void testTypeGetterSetter() {
		Pet pet = new Pet();
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		assertThat(pet.getType()).isNotNull();
		assertThat(pet.getType().getName()).isEqualTo("dog");
	}

	@Test
	void testDefaultBirthDateIsNull() {
		Pet pet = new Pet();
		assertThat(pet.getBirthDate()).isNull();
	}

	@Test
	void testDefaultTypeIsNull() {
		Pet pet = new Pet();
		assertThat(pet.getType()).isNull();
	}

	@Test
	void testGetVisitsReturnsEmptySetByDefault() {
		Pet pet = new Pet();
		assertThat(pet.getVisits()).isNotNull();
		assertThat(pet.getVisits()).isEmpty();
	}

	@Test
	void testAddVisit() {
		Pet pet = new Pet();
		Visit visit = new Visit();
		visit.setDescription("checkup");
		pet.addVisit(visit);
		assertThat(pet.getVisits()).hasSize(1);
	}

	@Test
	void testAddMultipleVisits() {
		Pet pet = new Pet();
		Visit visit1 = new Visit();
		visit1.setDescription("checkup");
		Visit visit2 = new Visit();
		visit2.setDescription("vaccination");
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		assertThat(pet.getVisits()).hasSize(2);
	}

	@Test
	void testPetInheritsFromNamedEntity() {
		Pet pet = new Pet();
		pet.setName("Max");
		pet.setId(1);
		assertThat(pet.getName()).isEqualTo("Max");
		assertThat(pet.getId()).isEqualTo(1);
	}

}
