package com.example.demo.controller;

import javax.sql.DataSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.sql.Connection;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/status/infra")
public class StatusController {

    private final DataSource dataSource;

    public StatusController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse getInfraStatus() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname == null || hostname.isBlank()) {
            hostname = "unknown";
        }

        String dbStatus = "DISCONNECTED";
        String dbError = null;

        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                dbStatus = "CONNECTED";
            }
        } catch (Exception ex) {
            dbError = ex.getMessage();
        }

        String storageStatus = checkStorageStatus();

        return new StatusResponse(
                "HEALTHY",
                hostname,
                dbStatus,
                dbError,
                storageStatus
        );
    }

    private String checkStorageStatus() {
        File logsDirectory = new File("/app/logs");

        if (!logsDirectory.exists()) {
            return "MOUNT ERROR: /app/logs does not exist";
        }

        if (!logsDirectory.isDirectory()) {
            return "MOUNT ERROR: /app/logs is not a directory";
        }

        if (!logsDirectory.canWrite()) {
            return "MOUNT ERROR: /app/logs is not writable";
        }

        return "MOUNTED (RW)";
    }

    public record StatusResponse(
            String app_status,
            String server_ip,
            String db_status,
            String db_error,
            String storage_status
    ) {
    }
}
