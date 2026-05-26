package org.springframework.samples.petclinic.vet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class VetModelTests {

	@Test
	void testGetSpecialtiesReturnsEmptyListByDefault() {
		Vet vet = new Vet();
		assertThat(vet.getSpecialties()).isEmpty();
	}

	@Test
	void testAddSpecialty() {
		Vet vet = new Vet();
		Specialty specialty = new Specialty();
		specialty.setName("radiology");
		vet.addSpecialty(specialty);
		assertThat(vet.getSpecialties()).hasSize(1);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("radiology");
	}

	@Test
	void testGetNrOfSpecialties() {
		Vet vet = new Vet();
		assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
		Specialty specialty1 = new Specialty();
		specialty1.setName("surgery");
		vet.addSpecialty(specialty1);
		assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
		Specialty specialty2 = new Specialty();
		specialty2.setName("dentistry");
		vet.addSpecialty(specialty2);
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
	}

	@Test
	void testSpecialtiesAreSortedByName() {
		Vet vet = new Vet();
		Specialty surgery = new Specialty();
		surgery.setName("surgery");
		Specialty dentistry = new Specialty();
		dentistry.setName("dentistry");
		Specialty radiology = new Specialty();
		radiology.setName("radiology");
		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);
		vet.addSpecialty(radiology);
		List<Specialty> specialties = vet.getSpecialties();
		assertThat(specialties.get(0).getName()).isEqualTo("dentistry");
		assertThat(specialties.get(1).getName()).isEqualTo("radiology");
		assertThat(specialties.get(2).getName()).isEqualTo("surgery");
	}

	@Test
	void testGetSpecialtiesReturnsUnmodifiableList() {
		Vet vet = new Vet();
		Specialty specialty = new Specialty();
		specialty.setName("surgery");
		vet.addSpecialty(specialty);
		List<Specialty> specialties = vet.getSpecialties();
		try {
			specialties.add(new Specialty());
			assertThat(false).isTrue();
		}
		catch (UnsupportedOperationException e) {
			assertThat(true).isTrue();
		}
	}

	@Test
	void testVetInheritsFromPerson() {
		Vet vet = new Vet();
		vet.setFirstName("James");
		vet.setLastName("Carter");
		vet.setId(1);
		assertThat(vet.getFirstName()).isEqualTo("James");
		assertThat(vet.getLastName()).isEqualTo("Carter");
		assertThat(vet.getId()).isEqualTo(1);
	}

}
