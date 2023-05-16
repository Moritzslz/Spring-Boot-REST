package de.tum.in.ase.eist.rest;

import de.tum.in.ase.eist.model.Note;
import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.service.PersonService;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PersonResource {

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    // TODO Part 1: Implement the specified endpoints here
    @PostMapping("persons")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (person.getId() == null) {
            return ResponseEntity.ok(personService.savePerson(person));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("persons/{personId}")
    public ResponseEntity<Person> updatePerson(@RequestBody Person updatedPerson, @PathVariable("personId") UUID personId) {
        if (updatedPerson.getId().equals(personId)) {
            return ResponseEntity.ok(personService.savePerson(updatedPerson));
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("persons/{personId}")
    public ResponseEntity<Void> deletePerson(@PathVariable("personId") UUID personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("persons")
    public ResponseEntity<List<Person>> getAllPersons(@RequestParam("sortingOptions") PersonSortingOptions sortingOptions) {
        if ((sortingOptions.getSortingOrder().equals(PersonSortingOptions.SortingOrder.ASCENDING) || sortingOptions.getSortingOrder().equals(PersonSortingOptions.SortingOrder.DESCENDING)) &&
                (sortingOptions.getSortField().equals(PersonSortingOptions.SortField.BIRTHDAY) ||  sortingOptions.getSortField().equals(PersonSortingOptions.SortField.ID) ||
                sortingOptions.getSortField().equals(PersonSortingOptions.SortField.FIRST_NAME) ||  sortingOptions.getSortField().equals(PersonSortingOptions.SortField.LAST_NAME)))
            return ResponseEntity.ok(personService.getAllPersons(sortingOptions));
        return ResponseEntity.badRequest().build();
    }
}
