package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OwnerRepositoryTests {

	@Autowired
	private OwnerRepository owners;

	@Test
	void shouldFindOwnerById() {
		Owner owner = this.owners.findById(1);
		assertThat(owner).isNotNull();
		assertThat(owner.getFirstName()).isEqualTo("George");
		assertThat(owner.getLastName()).isEqualTo("Franklin");
	}

	@Test
	void shouldReturnNullForNonExistentOwner() {
		Owner owner = this.owners.findById(999);
		assertThat(owner).isNull();
	}

	@Test
	void shouldFindOwnersByLastNameWithPagination() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Owner> results = this.owners.findByLastName("Davis", pageable);
		assertThat(results.getContent()).hasSize(2);
	}

	@Test
	void shouldReturnEmptyPageForNonExistentLastName() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Owner> results = this.owners.findByLastName("NoSuchName", pageable);
		assertThat(results.getContent()).isEmpty();
	}

	@Test
	void shouldFindOwnersByPartialLastName() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> results = this.owners.findByLastName("Fr", pageable);
		assertThat(results.getContent()).isNotEmpty();
		assertThat(results.getContent().get(0).getLastName()).startsWith("Fr");
	}

	@Test
	void shouldFindAllOwners() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<Owner> results = this.owners.findAll(pageable);
		assertThat(results.getContent()).isNotEmpty();
		assertThat(results.getTotalElements()).isGreaterThanOrEqualTo(10);
	}

	@Test
	void shouldFindAllOwnersWithPagination() {
		Pageable pageable = PageRequest.of(0, 3);
		Page<Owner> results = this.owners.findAll(pageable);
		assertThat(results.getContent()).hasSize(3);
		assertThat(results.getTotalPages()).isGreaterThan(1);
	}

	@Test
	@Transactional
	void shouldSaveNewOwner() {
		Owner owner = new Owner();
		owner.setFirstName("New");
		owner.setLastName("Owner");
		owner.setAddress("123 Test St");
		owner.setCity("TestCity");
		owner.setTelephone("1234567890");
		this.owners.save(owner);
		assertThat(owner.getId()).isNotNull();
	}

	@Test
	@Transactional
	void shouldUpdateExistingOwner() {
		Owner owner = this.owners.findById(1);
		owner.setCity("NewCity");
		this.owners.save(owner);
		Owner updated = this.owners.findById(1);
		assertThat(updated.getCity()).isEqualTo("NewCity");
	}

	@Test
	void shouldFindPetTypes() {
		List<PetType> petTypes = this.owners.findPetTypes();
		assertThat(petTypes).isNotEmpty();
		assertThat(petTypes.size()).isGreaterThanOrEqualTo(6);
	}

	@Test
	void shouldFindPetTypesInAlphabeticalOrder() {
		List<PetType> petTypes = this.owners.findPetTypes();
		for (int i = 1; i < petTypes.size(); i++) {
			assertThat(petTypes.get(i).getName().compareTo(petTypes.get(i - 1).getName())).isGreaterThanOrEqualTo(0);
		}
	}

	@Test
	void shouldFindOwnerWithPets() {
		Owner owner = this.owners.findById(6);
		assertThat(owner.getPets()).isNotEmpty();
	}

	@Test
	@Transactional
	void shouldAddPetToOwner() {
		Owner owner = this.owners.findById(6);
		int petCount = owner.getPets().size();
		Pet pet = new Pet();
		pet.setName("testpet");
		pet.setBirthDate(LocalDate.now());
		List<PetType> types = this.owners.findPetTypes();
		pet.setType(types.get(0));
		owner.addPet(pet);
		this.owners.save(owner);
		Owner reloaded = this.owners.findById(6);
		assertThat(reloaded.getPets()).hasSize(petCount + 1);
	}

	@Test
	void shouldFindByEmptyLastName() {
		Pageable pageable = PageRequest.of(0, 20);
		Page<Owner> results = this.owners.findByLastName("", pageable);
		assertThat(results.getContent()).isNotEmpty();
	}

}
