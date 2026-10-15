
package com.ai.procurement.controller;

import com.ai.procurement.model.database.request.DatabaseConnectionRequest;
import com.ai.procurement.model.database.request.DatabaseOperationRequest;
import com.ai.procurement.service.DatabaseConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/database-connections")
public class DatabaseConnectionController {

    private final DatabaseConnectionService connectionService;

    public DatabaseConnectionController(
            DatabaseConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testConnection(
            @RequestBody DatabaseConnectionRequest request) {

        boolean connected = connectionService.testConnection(request);

        if (connected) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Database connection successful"
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "status", "failed",
                "message", "Unable to connect. Check the connection details."
        ));
    }

    @PostMapping("/connect")
    public ResponseEntity<?> createConnection(
            @RequestBody DatabaseConnectionRequest request) {
        try {
            String connectionId =
                    connectionService.createManagedConnection(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "Database connection created successfully",
                    "connectionId", connectionId
            ));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", exception.getMessage()
            ));

        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Unable to create database connection"
            ));
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executeOperation(
            @RequestBody DatabaseOperationRequest request) {
        try {
            Object result = connectionService.executeOperation(request);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "operation", request.operation(),
                    "result", result
            ));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", exception.getMessage()
            ));

        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "failed",
                    "message", "Unable to execute database operation"
            ));
        }
    }
}
