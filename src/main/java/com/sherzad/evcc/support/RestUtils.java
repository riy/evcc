package com.sherzad.evcc.support;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RestUtils {

    public <TYPE> ResponseEntity<TYPE> response(final TYPE responseEntity) {
        return Optional.ofNullable(responseEntity)
                .map(response -> ResponseEntity.ok().body(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Void> emptyResponse() {
        return ResponseEntity.noContent().build();
    }
}
