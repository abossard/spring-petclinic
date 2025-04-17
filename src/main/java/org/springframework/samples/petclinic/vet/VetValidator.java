package org.springframework.samples.petclinic.vet;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for <code>Vet</code> forms.
 */
public class VetValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		return Vet.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Vet vet = (Vet) target;

		// Validate first name
		if (!StringUtils.hasText(vet.getFirstName())) {
			errors.rejectValue("firstName", REQUIRED, REQUIRED);
		}

		// Validate last name
		if (!StringUtils.hasText(vet.getLastName())) {
			errors.rejectValue("lastName", REQUIRED, REQUIRED);
		}

		// Validate specialties
		if (vet.getSpecialties() == null || vet.getSpecialties().isEmpty()) {
			errors.rejectValue("specialties", REQUIRED, REQUIRED);
		}
	}

}