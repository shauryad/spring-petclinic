package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class VisitTests {

	@Test
	void testDefaultDateIsToday() {
		Visit visit = new Visit();
		assertThat(visit.getDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void testDateGetterSetter() {
		Visit visit = new Visit();
		LocalDate date = LocalDate.of(2023, 6, 15);
		visit.setDate(date);
		assertThat(visit.getDate()).isEqualTo(date);
	}

	@Test
	void testDescriptionGetterSetter() {
		Visit visit = new Visit();
		assertThat(visit.getDescription()).isNull();
		visit.setDescription("annual checkup");
		assertThat(visit.getDescription()).isEqualTo("annual checkup");
	}

	@Test
	void testVisitInheritsFromBaseEntity() {
		Visit visit = new Visit();
		visit.setId(10);
		assertThat(visit.getId()).isEqualTo(10);
		assertThat(visit.isNew()).isFalse();
	}

	@Test
	void testNewVisitIsNew() {
		Visit visit = new Visit();
		assertThat(visit.isNew()).isTrue();
	}

}
