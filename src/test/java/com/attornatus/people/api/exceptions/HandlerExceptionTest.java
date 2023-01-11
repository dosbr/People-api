package com.attornatus.people.api.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HandlerExceptionTest {
    public static final String PERSON_NOT_FOUND = "Person not found";

    @InjectMocks
    private HandlerException handlerException;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whanObjectNotFoundThenReturnAResponseEntity() {
        ResponseEntity<StandardError> response = handlerException
                .objectNotFound(
                        new ObjectNotFoundException(PERSON_NOT_FOUND),
                        new MockHttpServletRequest()
                );
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals(PERSON_NOT_FOUND, response.getBody().getError());
        assertEquals(404, response.getBody().getStatus());

    }
}
