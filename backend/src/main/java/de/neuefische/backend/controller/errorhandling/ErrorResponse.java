package de.neuefische.backend.controller.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ErrorResponse {

    private HttpStatus error;
    private String errorMessage;
    private LocalDateTime timestamp;
    private String requestUri;
}
