package org.springframework.samples.petclinic.vet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class VetsTests {

	@Test
	void testGetVetListReturnsEmptyListByDefault() {
		Vets vets = new Vets();
		assertThat(vets.getVetList()).isNotNull();
		assertThat(vets.getVetList()).isEmpty();
	}

	@Test
	void testGetVetListReturnsSameInstance() {
		Vets vets = new Vets();
		List<Vet> list1 = vets.getVetList();
		List<Vet> list2 = vets.getVetList();
		assertThat(list1).isSameAs(list2);
	}

	@Test
	void testAddVetToList() {
		Vets vets = new Vets();
		Vet vet = new Vet();
		vet.setFirstName("James");
		vet.setLastName("Carter");
		vets.getVetList().add(vet);
		assertThat(vets.getVetList()).hasSize(1);
		assertThat(vets.getVetList().get(0).getFirstName()).isEqualTo("James");
	}

	@Test
	void testAddMultipleVets() {
		Vets vets = new Vets();
		Vet vet1 = new Vet();
		vet1.setFirstName("James");
		Vet vet2 = new Vet();
		vet2.setFirstName("Helen");
		vets.getVetList().add(vet1);
		vets.getVetList().add(vet2);
		assertThat(vets.getVetList()).hasSize(2);
	}

}
