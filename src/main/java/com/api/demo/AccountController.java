package com.api.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    List<Account> accounts = new ArrayList<>(Arrays.asList(new Account("name0", 0L), new Account("name1", 1L), new Account("name2", 2L), new Account("name3", 3L), new Account("name4", 4L), new Account("name5", 5L), new Account("name6", 6L)));

    public record Account(String name, Long id) {
    }

    @GetMapping()
    public Page<Account> getAccounts(@RequestParam(required = false) Long limit, @RequestParam(defaultValue = "0") Long page, @RequestParam(required = false) String[] search) {
        return new Page<>(accounts.stream().filter(account -> search == null || account.name.contains(search[0])).toList(), limit, page);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get an account by its id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the account",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountController.Account.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content)})
    public Account getAccount(@PathVariable(name = "accountId") int id) {
        return accounts.get(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        accounts.add(account);
        return account;
    }

    @PutMapping()
    public ResponseEntity<Account> replaceOrCreateAccount(@RequestBody Account account,
                                                          HttpServletResponse response
    ) {
        ResponseEntity.BodyBuilder bodyBuilder;
        System.out.println(response);
        if (account.id != null) {
            Optional<Account> first = accounts.stream().filter(account1 -> account1.id.equals(account.id)).findFirst();
            first.ifPresent(value -> accounts.remove(value));
            bodyBuilder = ResponseEntity.status(HttpStatus.OK);
        } else {
            bodyBuilder = ResponseEntity.status(HttpStatus.CREATED);
        }
        accounts.add(account);

        return bodyBuilder.body(account);
    }

}
