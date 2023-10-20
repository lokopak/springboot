package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.NotFoundException;


@RestController
@RequestMapping("/api/foo")

public class FooController {

    public static final List<FooResponse> list = new ArrayList<>();

    public record FooRequest(String name) {

    }

    public record FooResponse(UUID id, String name) {
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody FooRequest foo) {
        var response = new FooResponse(UUID.randomUUID(), foo.name());
        list.add(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public FooResponse[] getList(@RequestParam(required = false) String name, Principal principal) {
        FooResponse[] response = { new FooResponse(UUID.randomUUID(), name) };

        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public FooResponse update(@PathVariable UUID id, @RequestBody FooRequest foo) {
        var response = new FooResponse(id, foo.name());
        return response;

    }

    @GetMapping("/{id}")
    public FooResponse getId(@PathVariable UUID id, @RequestHeader("x-name") String name) {
        return list.stream().filter(foo -> foo.id.equals(id)).findFirst()
                .orElseThrow(() -> new NotFoundException("Not found " + id));

    }

}
