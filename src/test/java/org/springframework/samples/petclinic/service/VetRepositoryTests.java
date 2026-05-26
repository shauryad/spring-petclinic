package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class VetRepositoryTests {

	@Autowired
	private VetRepository vets;

	@Test
	void shouldFindAllVets() {
		Collection<Vet> allVets = this.vets.findAll();
		assertThat(allVets).isNotEmpty();
		assertThat(allVets.size()).isGreaterThanOrEqualTo(6);
	}

	@Test
	void shouldFindAllVetsPaginated() {
		Pageable pageable = PageRequest.of(0, 3);
		Page<Vet> page = this.vets.findAll(pageable);
		assertThat(page.getContent()).hasSize(3);
		assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(6);
	}

	@Test
	void shouldFindVetWithSpecialties() {
		Collection<Vet> allVets = this.vets.findAll();
		boolean foundVetWithSpecialty = allVets.stream().anyMatch(v -> v.getNrOfSpecialties() > 0);
		assertThat(foundVetWithSpecialty).isTrue();
	}

	@Test
	void shouldFindVetWithoutSpecialties() {
		Collection<Vet> allVets = this.vets.findAll();
		boolean foundVetWithoutSpecialty = allVets.stream().anyMatch(v -> v.getNrOfSpecialties() == 0);
		assertThat(foundVetWithoutSpecialty).isTrue();
	}

	@Test
	void shouldReturnCorrectPageSize() {
		Pageable pageable = PageRequest.of(0, 2);
		Page<Vet> page = this.vets.findAll(pageable);
		assertThat(page.getContent().size()).isLessThanOrEqualTo(2);
	}

	@Test
	void shouldReturnSecondPage() {
		Pageable pageable = PageRequest.of(1, 3);
		Page<Vet> page = this.vets.findAll(pageable);
		assertThat(page.getNumber()).isEqualTo(1);
	}

	@Test
	void shouldContainVetJamesCarter() {
		Collection<Vet> allVets = this.vets.findAll();
		boolean found = allVets.stream()
			.anyMatch(v -> "James".equals(v.getFirstName()) && "Carter".equals(v.getLastName()));
		assertThat(found).isTrue();
	}

	@Test
	void shouldFindVetDouglas() {
		Collection<Vet> allVets = this.vets.findAll();
		Vet douglas = allVets.stream().filter(v -> "Douglas".equals(v.getLastName())).findFirst().orElse(null);
		assertThat(douglas).isNotNull();
		assertThat(douglas.getNrOfSpecialties()).isEqualTo(2);
	}

}
