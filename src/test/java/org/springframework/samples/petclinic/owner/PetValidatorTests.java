package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetValidatorTests {

	private final PetValidator validator = new PetValidator();

	@Test
	void testSupportsReturnsTrueForPet() {
		assertThat(validator.supports(Pet.class)).isTrue();
	}

	@Test
	void testSupportsReturnsFalseForOtherClasses() {
		assertThat(validator.supports(Owner.class)).isFalse();
		assertThat(validator.supports(String.class)).isFalse();
	}

	@Test
	void testValidPet() {
		Pet pet = new Pet();
		pet.setName("Max");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasErrors()).isFalse();
	}

	@Test
	void testRejectsEmptyName() {
		Pet pet = new Pet();
		pet.setName("");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasFieldErrors("name")).isTrue();
	}

	@Test
	void testRejectsNullName() {
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasFieldErrors("name")).isTrue();
	}

	@Test
	void testRejectsNullTypeForNewPet() {
		Pet pet = new Pet();
		pet.setName("Max");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasFieldErrors("type")).isTrue();
	}

	@Test
	void testAcceptsNullTypeForExistingPet() {
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Max");
		pet.setBirthDate(LocalDate.of(2020, 1, 1));
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasFieldErrors("type")).isFalse();
	}

	@Test
	void testRejectsNullBirthDate() {
		Pet pet = new Pet();
		pet.setName("Max");
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.hasFieldErrors("birthDate")).isTrue();
	}

	@Test
	void testRejectsAllFieldsMissing() {
		Pet pet = new Pet();
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);
		assertThat(errors.getErrorCount()).isGreaterThanOrEqualTo(3);
	}

}
