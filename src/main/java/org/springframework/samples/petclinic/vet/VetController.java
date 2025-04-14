/*
 * Copyright 2012-2019 the original author or authors.
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
package org.springframework.samples.petclinic.vet;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.cache.annotation.CacheEvict;

import jakarta.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private final VetRepository vetRepository;

	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	public VetController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "lastName") String sort, @RequestParam(defaultValue = "asc") String order,
			Model model) {
		// Use paginated and sorted lookup
		Page<Vet> paginated = findPaginated(page, sort, order);
		return addPaginationModel(page, paginated, model);
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page, String sort, String order) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize,
				order.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending());
		return vetRepository.findAll(pageable);
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("vet")
	public Vet findVet(@PathVariable(name = "vetId", required = false) Integer vetId) {
		return vetId == null ? new Vet() : this.vetRepository.findById(vetId)
			.orElseThrow(() -> new IllegalArgumentException("Vet not found with id: " + vetId));
	}

	@GetMapping("/vets/new")
	public String initCreationForm(Model model) {
		model.addAttribute("vet", new Vet());
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/new")
	@CacheEvict(value = "vets", allEntries = true)
	public String processCreationForm(@Valid Vet vet, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}

		this.vetRepository.save(vet);
		redirectAttributes.addFlashAttribute("message", "New Vet Created");
		return "redirect:/vets/" + vet.getId();
	}

	@GetMapping("/vets/{vetId}/edit")
	public String initUpdateVetForm(@PathVariable("vetId") int vetId, Model model) {
		Vet vet = this.vetRepository.findById(vetId)
			.orElseThrow(() -> new IllegalArgumentException("Vet not found with id: " + vetId));
		model.addAttribute("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/{vetId}/edit")
	@CacheEvict(value = "vets", allEntries = true)
	public String processUpdateVetForm(@Valid Vet vet, BindingResult result, @PathVariable("vetId") int vetId,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}

		if (vet.getId() != vetId) {
			result.rejectValue("id", "mismatch", "The vet ID in the form does not match the URL.");
			redirectAttributes.addFlashAttribute("error", "Vet ID mismatch. Please try again.");
			return "redirect:/vets/{vetId}/edit";
		}

		vet.setId(vetId);
		this.vetRepository.save(vet);
		redirectAttributes.addFlashAttribute("message", "Vet Values Updated");
		return "redirect:/vets/{vetId}";
	}

	@GetMapping("/vets/{vetId}")
	public String showVet(@PathVariable("vetId") int vetId, Model model) {
		Vet vet = this.vetRepository.findById(vetId)
			.orElseThrow(() -> new IllegalArgumentException("Vet not found with id: " + vetId));
		model.addAttribute("vet", vet);
		return "vets/vetDetails";
	}

}
