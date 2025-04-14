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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

	public VetController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "lastName") String sort, @RequestParam(defaultValue = "asc") String direction,
			Model model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		Page<Vet> paginated = findPaginated(page, sort, direction);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model, sort, direction);
	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model, String sort, String direction) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		model.addAttribute("sortField", sort);
		model.addAttribute("sortDirection", direction);
		model.addAttribute("reverseSortDirection", direction.equals("asc") ? "desc" : "asc");
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page, String sortField, String sortDirection) {
		int pageSize = 5;
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
				: Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(page - 1, pageSize, sort);
		return vetRepository.findAll(pageable);
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetRepository.findAll());
		return vets;
	}

	@GetMapping("/vets/new")
	public String initCreationForm() {
		return "vets/createOrUpdateVetForm";
	}

	@PostMapping("/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the vet.");
			return "vets/createOrUpdateVetForm";
		}

		this.vetRepository.save(vet);
		redirectAttributes.addFlashAttribute("message", "New Vet Created");
		return "redirect:/vets";
	}

}
