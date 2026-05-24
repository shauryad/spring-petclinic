/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.transaction.annotation.Transactional;

/**
 * Comprehensive tests for {@link PetTypeRepository}.
 * <p>
 * Uses {@link DataJpaTest} with the default H2 in-memory database. The database is
 * populated with seed data from {@code data.sql} which includes six pet types: cat, dog,
 * lizard, snake, bird, hamster.
 *
 * @author Devin AI
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PetTypeRepositoryTests {

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Nested
	class FindPetTypes {

		@Test
		void shouldReturnAllSixSeededPetTypes() {
			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).hasSize(6);
		}

		@Test
		void shouldReturnPetTypesOrderedByName() {
			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).extracting(PetType::getName)
				.containsExactly("bird", "cat", "dog", "hamster", "lizard", "snake");
		}

		@Test
		void shouldContainExpectedPetTypeNames() {
			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).extracting(PetType::getName)
				.contains("cat", "dog", "bird", "snake", "lizard", "hamster");
		}

		@Test
		void shouldReturnPetTypesWithNonNullIds() {
			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).allSatisfy(petType -> assertThat(petType.getId()).isNotNull());
		}

		@Test
		void shouldReturnPetTypesWithNonBlankNames() {
			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).allSatisfy(petType -> assertThat(petType.getName()).isNotBlank());
		}

		@Test
		@Transactional
		void shouldReflectNewlyAddedPetType() {
			PetType fish = new PetType();
			fish.setName("fish");
			petTypeRepository.save(fish);

			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).hasSize(7);
			assertThat(petTypes).extracting(PetType::getName).contains("fish");
		}

		@Test
		@Transactional
		void shouldMaintainOrderAfterAddingPetType() {
			PetType aardvark = new PetType();
			aardvark.setName("aardvark");
			petTypeRepository.save(aardvark);

			List<PetType> petTypes = petTypeRepository.findPetTypes();
			assertThat(petTypes).extracting(PetType::getName)
				.containsExactly("aardvark", "bird", "cat", "dog", "hamster", "lizard", "snake");
		}

	}

	@Nested
	class FindById {

		@Test
		void shouldFindCatById() {
			Optional<PetType> petType = petTypeRepository.findById(1);
			assertThat(petType).isPresent();
			assertThat(petType.get().getName()).isEqualTo("cat");
		}

		@Test
		void shouldFindDogById() {
			Optional<PetType> petType = petTypeRepository.findById(2);
			assertThat(petType).isPresent();
			assertThat(petType.get().getName()).isEqualTo("dog");
		}

		@Test
		void shouldReturnEmptyOptionalForNonExistentId() {
			Optional<PetType> petType = petTypeRepository.findById(999);
			assertThat(petType).isEmpty();
		}

		@Test
		void shouldReturnEmptyOptionalForNegativeId() {
			Optional<PetType> petType = petTypeRepository.findById(-1);
			assertThat(petType).isEmpty();
		}

		@Test
		void shouldReturnEmptyOptionalForZeroId() {
			Optional<PetType> petType = petTypeRepository.findById(0);
			assertThat(petType).isEmpty();
		}

	}

	@Nested
	class SavePetType {

		@Test
		@Transactional
		void shouldSaveNewPetTypeAndGenerateId() {
			PetType rabbit = new PetType();
			rabbit.setName("rabbit");

			assertThat(rabbit.isNew()).isTrue();
			PetType saved = petTypeRepository.save(rabbit);

			assertThat(saved.getId()).isNotNull();
			assertThat(saved.isNew()).isFalse();
			assertThat(saved.getName()).isEqualTo("rabbit");
		}

		@Test
		@Transactional
		void shouldPersistSavedPetType() {
			PetType parrot = new PetType();
			parrot.setName("parrot");
			PetType saved = petTypeRepository.save(parrot);

			Optional<PetType> retrieved = petTypeRepository.findById(saved.getId());
			assertThat(retrieved).isPresent();
			assertThat(retrieved.get().getName()).isEqualTo("parrot");
		}

		@Test
		@Transactional
		void shouldIncrementCountAfterSave() {
			long countBefore = petTypeRepository.count();

			PetType turtle = new PetType();
			turtle.setName("turtle");
			petTypeRepository.save(turtle);

			long countAfter = petTypeRepository.count();
			assertThat(countAfter).isEqualTo(countBefore + 1);
		}

		@Test
		@Transactional
		void shouldSaveMultiplePetTypes() {
			PetType fish = new PetType();
			fish.setName("fish");
			PetType rabbit = new PetType();
			rabbit.setName("rabbit");

			petTypeRepository.saveAll(List.of(fish, rabbit));

			assertThat(petTypeRepository.count()).isEqualTo(8);
			assertThat(petTypeRepository.findPetTypes()).extracting(PetType::getName).contains("fish", "rabbit");
		}

	}

	@Nested
	class UpdatePetType {

		@Test
		@Transactional
		void shouldUpdateExistingPetTypeName() {
			Optional<PetType> optionalPetType = petTypeRepository.findById(1);
			assertThat(optionalPetType).isPresent();

			PetType petType = optionalPetType.get();
			petType.setName("persian cat");
			petTypeRepository.save(petType);

			Optional<PetType> updated = petTypeRepository.findById(1);
			assertThat(updated).isPresent();
			assertThat(updated.get().getName()).isEqualTo("persian cat");
		}

		@Test
		@Transactional
		void shouldNotChangeCountOnUpdate() {
			long countBefore = petTypeRepository.count();

			Optional<PetType> optionalPetType = petTypeRepository.findById(1);
			assertThat(optionalPetType).isPresent();
			PetType petType = optionalPetType.get();
			petType.setName("updated cat");
			petTypeRepository.save(petType);

			assertThat(petTypeRepository.count()).isEqualTo(countBefore);
		}

		@Test
		@Transactional
		void shouldPreserveIdOnUpdate() {
			Optional<PetType> optionalPetType = petTypeRepository.findById(3);
			assertThat(optionalPetType).isPresent();

			PetType petType = optionalPetType.get();
			Integer originalId = petType.getId();
			petType.setName("iguana");
			PetType saved = petTypeRepository.save(petType);

			assertThat(saved.getId()).isEqualTo(originalId);
		}

	}

	@Nested
	class DeletePetType {

		@Test
		@Transactional
		void shouldDeletePetTypeById() {
			PetType newType = new PetType();
			newType.setName("axolotl");
			PetType saved = petTypeRepository.save(newType);
			petTypeRepository.flush();
			Integer savedId = saved.getId();

			petTypeRepository.deleteById(savedId);
			petTypeRepository.flush();

			assertThat(petTypeRepository.findById(savedId)).isEmpty();
		}

		@Test
		@Transactional
		void shouldDecrementCountAfterDelete() {
			long countBefore = petTypeRepository.count();

			PetType newType = new PetType();
			newType.setName("goldfish");
			PetType saved = petTypeRepository.save(newType);
			petTypeRepository.flush();

			petTypeRepository.deleteById(saved.getId());
			petTypeRepository.flush();

			assertThat(petTypeRepository.count()).isEqualTo(countBefore);
		}

		@Test
		@Transactional
		void shouldDeletePetTypeEntity() {
			PetType newType = new PetType();
			newType.setName("gecko");
			PetType saved = petTypeRepository.save(newType);
			petTypeRepository.flush();

			Integer savedId = saved.getId();
			petTypeRepository.delete(saved);
			petTypeRepository.flush();

			assertThat(petTypeRepository.findById(savedId)).isEmpty();
		}

	}

	@Nested
	class ExistenceChecks {

		@Test
		void shouldReturnTrueForExistingId() {
			assertThat(petTypeRepository.existsById(1)).isTrue();
		}

		@Test
		void shouldReturnFalseForNonExistentId() {
			assertThat(petTypeRepository.existsById(999)).isFalse();
		}

		@Test
		void shouldReturnFalseForZeroId() {
			assertThat(petTypeRepository.existsById(0)).isFalse();
		}

	}

	@Nested
	class CountPetTypes {

		@Test
		void shouldCountAllSeededPetTypes() {
			assertThat(petTypeRepository.count()).isEqualTo(6);
		}

		@Test
		@Transactional
		void shouldReflectCountAfterInsert() {
			PetType ferret = new PetType();
			ferret.setName("ferret");
			petTypeRepository.save(ferret);

			assertThat(petTypeRepository.count()).isEqualTo(7);
		}

	}

	@Nested
	class FindAll {

		@Test
		void shouldFindAllPetTypes() {
			List<PetType> petTypes = petTypeRepository.findAll();
			assertThat(petTypes).hasSize(6);
		}

		@Test
		void shouldReturnSameEntitiesAsFindPetTypes() {
			List<PetType> fromFindAll = petTypeRepository.findAll();
			List<PetType> fromFindPetTypes = petTypeRepository.findPetTypes();

			assertThat(fromFindAll).hasSameSizeAs(fromFindPetTypes);
			assertThat(fromFindAll).extracting(PetType::getName)
				.containsExactlyInAnyOrderElementsOf(fromFindPetTypes.stream().map(PetType::getName).toList());
		}

	}

	@Nested
	class EntityBehavior {

		@Test
		void shouldReturnNameFromToString() {
			Optional<PetType> petType = petTypeRepository.findById(1);
			assertThat(petType).isPresent();
			assertThat(petType.get().toString()).isEqualTo("cat");
		}

		@Test
		void shouldReportNewEntityBeforeSave() {
			PetType petType = new PetType();
			petType.setName("chinchilla");
			assertThat(petType.isNew()).isTrue();
		}

		@Test
		@Transactional
		void shouldReportExistingEntityAfterSave() {
			PetType petType = new PetType();
			petType.setName("chinchilla");
			PetType saved = petTypeRepository.save(petType);
			assertThat(saved.isNew()).isFalse();
		}

	}

}
