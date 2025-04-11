# GitHub Copilot Hands-On Lab: PetShop Edition 🐾🤖

Welcome to the **GitHub Copilot Hands-On Lab** using the Spring Boot Petclinic application.  
This workshop is designed to help you learn how to guide GitHub Copilot with clear, specific prompts so it can assist you in writing and modifying code efficiently.

---

## 🚀 General Tips

1. **Be Explicit:** Don’t assume Copilot understands your intent perfectly. Include all relevant information in your prompts.
2. **Use Copilot Chat:** For anything that goes beyond simple inline autocompletion—like debugging, explaining code, or suggesting new tests—open Copilot Chat and ask for help.
3. **Show It the Files:** When you want Copilot to reference existing code (e.g., entities, controller classes, or tests), open those files so Copilot can see them and provide more context-aware suggestions.
4. **Iterate on Prompts:** If you don’t like what Copilot suggests, refine your prompt. The more detail you provide, the better Copilot’s output can be.

---

## 🧭 Chapters Overview

1. **🧮 Sorting Tables** – Enable sorting on the list of veterinarians by last name.  
2. **🩺 Editable Veterinarians** – Create and edit veterinarians using forms and CRUD operations.  
3. **🧪 Refactoring & Tests** – Refactor existing code and write comprehensive tests.  
4. **🌳 Relationship Visualization** – Generate an ASCII tree representing relationships among veterinarians, their specialties, pets, and owners.

Each chapter builds on the previous one, introducing new ways to leverage Copilot’s capabilities.

---

## 🐣 Chapter 1: Sorting Tables

**Goal:** Add sorting to the list of veterinarians by their last name.

### Files to Modify
- `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`  
  Modify the controller method that retrieves the list of vets to support sorting.
- `src/main/resources/templates/vets/vetList.html`  
  Update the Thymeleaf template to include clickable column headers or links that trigger sorting by last name.
- `src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java`  
  Add a method like `List<Vet> findAllByOrderByLastNameAsc()` (or similar).
- `src/test/java/org/springframework/samples/petclinic/vet/VetControllerTests.java`  
  Write tests that confirm the sorted order is displayed in the view.

### Suggested Copilot Prompts

```java
// In VetController, add sorting by last name to the list of vets
```

```html
<!-- In vetList.html, create a table header link that passes a sorting parameter -->
```

### Testing Hints
- Ensure the sorting parameter is correctly passed from the UI to the controller.
- Verify that the repository method returns data sorted by last name.
- Use existing pagination tests (if any) as a reference to see how sorting might be tested similarly.

---

## 🛠️ Chapter 2: Editable Veterinarians

**Goal:** Add the ability to create and edit veterinarians with dedicated form pages.

### Files to Modify
- `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`
  - Add new CRUD endpoints (GET/POST for creating, GET/POST for updating).
- `src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java`
  - Ensure you have `save(Vet vet)` and `findById(int id)` or equivalent methods.
- **Create New Template**: `src/main/resources/templates/vets/createOrUpdateVetForm.html`
  - A Thymeleaf form for capturing veterinarian data (first name, last name, specialties).
- **Create New File**: `src/main/java/org/springframework/samples/petclinic/vet/VetValidator.java`
  - Add validation logic for veterinarian data (e.g., mandatory fields, etc.).
- `src/test/java/org/springframework/samples/petclinic/vet/VetControllerTests.java`
  - Add tests covering the new CRUD operations (creation, update, error handling).

### Suggested Copilot Prompts

```java
// In VetController, add a GET endpoint for the create form and a POST endpoint to save a new Vet
// Validate form inputs, handle errors, and redirect on success
```

```html
<!-- createOrUpdateVetForm.html: a Thymeleaf form binding Vet object fields (firstName, lastName, specialties) -->
```

### Testing Hints
- Use `PetControllerTests` or `OwnerControllerTests` as references for testing form submissions.
- Include tests for:
  1. Successful creation (correct input).
  2. Validation errors (invalid or missing fields).
  3. Editing existing vets (including loading the form with existing data).
  4. Repository calls (mocking or verifying interactions).

---

## 🧹 Chapter 3: Refactor + Test Generation

**Goal:** Clean up existing methods in `VetController` and `VetRepository`, then write comprehensive tests.

### Files to Modify
- `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`
  - Simplify or shorten methods, extract repeated logic, and improve readability.
- `src/main/java/org/springframework/samples/petclinic/vet/VetRepository.java`
  - If you have outdated or unused methods, remove or refactor them.
- `src/test/java/org/springframework/samples/petclinic/vet/VetControllerTests.java`
  - Add or improve tests for the refactored methods (consider mocking to isolate units under test).
- **Create/Update**: `src/test/java/org/springframework/samples/petclinic/vet/VetValidatorTests.java`
  - Write unit tests for your `VetValidator` logic.

### Suggested Copilot Prompts

```java
// Refactor VetController methods to reduce duplication
// Extract specialty validation into a dedicated helper method
// Write tests for VetValidator to ensure it rejects invalid data
```

### Testing Hints
1. **Mocking:** Use frameworks like Mockito or similar to isolate the code being tested.  
2. **Nested Tests:** `@Nested` classes help group test methods (e.g., creation vs. update tests).  
3. **Edge Cases:** Check boundary conditions (e.g., blank last names, too many specialties, etc.).  
4. **Coverage:** Aim for high code coverage to ensure your refactoring didn’t break anything.

---

## 🌳 Chapter 4: Relationship Visualization

**Goal:** Create a feature that displays an ASCII tree of relationships among veterinarians, their specialties, pets, and owners.

### Files to Modify
- `src/main/java/org/springframework/samples/petclinic/vet/VetController.java`  
  - Add a new method to generate or retrieve the data structure needed for the ASCII tree.
- `src/main/java/org/springframework/samples/petclinic/vet/VetService.java` (or similar service layer)  
  - Consolidate veterinarian, specialty, pet, and owner data to form a cohesive relationship model.
- **Create New File**: `src/main/resources/templates/vets/relationshipTree.html`  
  - Provide a simple UI or page that, when requested, prints out or displays the ASCII tree.
- `src/test/java/org/springframework/samples/petclinic/vet/VetRelationshipTests.java`  
  - Write tests verifying the logic that constructs the tree (e.g., each vet node, specialties as children, pets, and owners).

### Implementation Outline
1. **Data Gathering:** Fetch all Vets, including their specialties, and link each Vet’s associated pets (through existing relationships) to each Pet’s owner.  
2. **ASCII Tree Construction:** Build a text-based tree structure. For example:
   ```
   Dr. Smith (Vet)
   ├── Specialty: Radiology
   ├── Specialty: Surgery
   └── Pets:
       ├── Buddy (Dog) -> Owner: John Doe
       └── Princess (Cat) -> Owner: Jane Roe
   ```
3. **Presentation:** Display this ASCII output in the `relationshipTree.html` page, or generate it as a text block that can be viewed in the browser or console.

### Suggested Copilot Prompts

```java
// In VetService, gather vets with their specialties, pets, and owners to build a hierarchical structure
// Format the data as an ASCII tree for display
```

```html
<!-- relationshipTree.html: A page with a <pre> block showing the ASCII relationship tree -->
```

### Testing Hints
- Test that all vets appear in the tree.
- Test that each specialty is linked to the correct vet.
- Test that pets (with owners) appear under the correct vet node.
- Consider edge cases (e.g., a vet with no specialties, a pet with no owner in the data set).

---

## 🎓 What You’ve Learned

- **Prompt Engineering:** You’ve seen how Copilot’s suggestions improve with detailed instructions and the right context.
- **Incremental Development:** Each chapter builds on the previous one, illustrating how to structure your feature work.
- **Testing & Validation:** Thorough tests are crucial to ensure new features and refactoring don’t break existing functionality.
- **Copilot Chat:** It can explain code, offer refactoring strategies, and assist with debugging—just ask!

With these chapters complete, you’ll have a fully functional (and more understandable) Petclinic application featuring sorting, editable veterinarians, clean refactored code, and a helpful ASCII tree to visualize relationships. Happy coding!
