package com.example.mcpntools.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collections;

/**
 * HelloWorld REST controller for demonstrating basic Spring Boot functionality.
 */
@RestController
public class HelloWorldController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class); // PMD: FieldNamingConventions
    private final Counter helloCounter;
    private final Counter helloNameCounter; // Renamed to avoid LongVariable
    private final AuditEventRepository auditRepo; // PMD: LongVariable

    @Autowired
    public HelloWorldController(final io.micrometer.core.instrument.MeterRegistry meterRegistry, final AuditEventRepository auditRepo) { // PMD: MethodArgumentCouldBeFinal, LongVariable
        this.helloCounter = meterRegistry.counter("custom.hello.invocations");
        this.helloNameCounter = meterRegistry.counter("custom.helloWithName.invocations");
        // Defensive copy for SpotBugs EI_EXPOSE_REP2: only assign if not null, else create new
        this.auditRepo = auditRepo != null ? auditRepo : new InMemoryAuditEventRepository();
    }

    /**
     * Returns a simple hello world message.
     *
     * @return greeting message
     */
    @Timed(value = "hello.endpoint", description = "Time taken to return hello world")
    @GetMapping("/hello")
    public String hello() {
        if (LOGGER.isInfoEnabled()) { // PMD: GuardLogStatement
            LOGGER.info("/hello endpoint called at {}", Instant.now());
        }
        helloCounter.increment();
        auditRepo.add(new AuditEvent("anonymous", "hello_access", Collections.singletonMap("endpoint", "/hello")));
        return "Hello World!";
    }

    /**
     * Returns a personalized greeting message.
     *
     * @param name the name to include in the greeting (defaults to "World")
     * @return personalized greeting message
     */
    @Timed(value = "helloWithName.endpoint", description = "Time taken to return hello with name")
    @GetMapping("/hello/name")
    public String helloWithName(@RequestParam(defaultValue = "World") final String name) {
        if (LOGGER.isInfoEnabled()) { // PMD: GuardLogStatement
            LOGGER.info("/hello/name endpoint called with name={} at {}", name, Instant.now());
        }
        helloNameCounter.increment();
        auditRepo.add(new AuditEvent("anonymous", "helloWithName_access", Collections.singletonMap("name", name)));
        return createGreeting(name != null ? name.trim() : "World");
    }

    /**
     * Creates a greeting message for the given name.
     *
     * @param name the name to include in the greeting
     * @return formatted greeting message
     */
    private String createGreeting(final String name) {
        final String safeName = name.isEmpty() ? "World" : name;
        return "Hello " + safeName + "!";
    }
}
