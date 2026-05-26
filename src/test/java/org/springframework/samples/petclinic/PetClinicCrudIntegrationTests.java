package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PetClinicCrudIntegrationTests {

	@LocalServerPort
	int port;

	@Autowired
	private RestTemplateBuilder builder;

	private RestTemplate restTemplate() {
		return builder.rootUri("http://localhost:" + port).build();
	}

	@Test
	void testWelcomePage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("Welcome");
	}

	@Test
	void testFindOwnersPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/owners/find").build(),
				String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testOwnerDetailsPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/owners/1").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("George");
		assertThat(result.getBody()).contains("Franklin");
	}

	@Test
	void testVetsHtmlPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/vets.html?page=1").build(),
				String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testVetsJsonEndpoint() {
		ResponseEntity<String> result = restTemplate()
			.exchange(RequestEntity.get("/vets").accept(MediaType.APPLICATION_JSON).build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("vetList");
	}

	@Test
	void testNewOwnerFormPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/owners/new").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testOwnerEditPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/owners/1/edit").build(),
				String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).contains("George");
	}

	@Test
	void testNewPetFormPage() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/owners/1/pets/new").build(),
				String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testNewVisitFormPage() {
		ResponseEntity<String> result = restTemplate()
			.exchange(RequestEntity.get("/owners/6/pets/7/visits/new").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testOwnerSearchReturnsResults() {
		ResponseEntity<String> result = restTemplate()
			.exchange(RequestEntity.get("/owners?page=1&lastName=Davis").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void testActuatorHealthEndpoint() {
		ResponseEntity<String> result = restTemplate().exchange(RequestEntity.get("/actuator/health").build(),
				String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
