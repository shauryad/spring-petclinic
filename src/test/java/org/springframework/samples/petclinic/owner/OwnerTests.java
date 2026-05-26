package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerTests {

	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
	}

	@Test
	void testAddressGetterSetter() {
		assertThat(owner.getAddress()).isEqualTo("110 W. Liberty St.");
		owner.setAddress("New Address");
		assertThat(owner.getAddress()).isEqualTo("New Address");
	}

	@Test
	void testCityGetterSetter() {
		assertThat(owner.getCity()).isEqualTo("Madison");
		owner.setCity("London");
		assertThat(owner.getCity()).isEqualTo("London");
	}

	@Test
	void testTelephoneGetterSetter() {
		assertThat(owner.getTelephone()).isEqualTo("6085551023");
		owner.setTelephone("1234567890");
		assertThat(owner.getTelephone()).isEqualTo("1234567890");
	}

	@Test
	void testGetPetsReturnsEmptyListByDefault() {
		assertThat(owner.getPets()).isNotNull();
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	void testAddPet() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets().get(0).getName()).isEqualTo("Max");
	}

	@Test
	void testAddPetDoesNotAddExistingPet() {
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	void testGetPetByName() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPet("Max")).isNotNull();
		assertThat(owner.getPet("max")).isNotNull();
		assertThat(owner.getPet("Unknown")).isNull();
	}

	@Test
	void testGetPetByNameIgnoreNew() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPet("Max", true)).isNull();
		assertThat(owner.getPet("Max", false)).isNotNull();
	}

	@Test
	void testGetPetById() {
		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("Max");
		owner.getPets().add(pet);
		assertThat(owner.getPet(5)).isNotNull();
		assertThat(owner.getPet(99)).isNull();
	}

	@Test
	void testGetPetByIdReturnsNullForNewPets() {
		Pet pet = new Pet();
		pet.setName("Max");
		owner.addPet(pet);
		assertThat(owner.getPet(Integer.valueOf(1))).isNull();
	}

	@Test
	void testAddVisit() {
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Max");
		owner.getPets().add(pet);
		Visit visit = new Visit();
		visit.setDescription("checkup");
		owner.addVisit(1, visit);
		assertThat(pet.getVisits()).hasSize(1);
	}

	@Test
	void testAddVisitWithNullPetIdThrowsException() {
		assertThatThrownBy(() -> owner.addVisit(null, new Visit())).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testAddVisitWithNullVisitThrowsException() {
		assertThatThrownBy(() -> owner.addVisit(1, null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testAddVisitWithInvalidPetIdThrowsException() {
		assertThatThrownBy(() -> owner.addVisit(99, new Visit())).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void testToString() {
		String result = owner.toString();
		assertThat(result).contains("George");
		assertThat(result).contains("Franklin");
		assertThat(result).contains("110 W. Liberty St.");
		assertThat(result).contains("Madison");
		assertThat(result).contains("6085551023");
	}

	@Test
	void testGetPetByNameWithNullName() {
		Pet pet = new Pet();
		owner.addPet(pet);
		assertThat(owner.getPet("anything")).isNull();
	}

	@Test
	void testAddVisitReturnsSelf() {
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Max");
		owner.getPets().add(pet);
		Visit visit = new Visit();
		visit.setDescription("checkup");
		Owner returned = owner.addVisit(1, visit);
		assertThat(returned).isSameAs(owner);
	}

}
