package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ha")
public class HaMonitorController {

    @GetMapping("/status")
    public Map<String, Object> getDbStatus() {
        Map<String, Object> status = new HashMap<>();

        // DB 1번 (마스터) 체크
        status.put("db1_status", checkDatabase("192.168.20.27"));
        // DB 2번 (슬레이브) 체크
        status.put("db2_status", checkDatabase("192.168.20.12"));

        // 현재 활성화된 메인 DB 정보
        status.put("active_db", "192.168.20.27 (Master)");
        status.put("server_time", LocalDateTime.now().toString());
        return status;
    }

    private String checkDatabase(String ip) {
        // 해당 IP의 3306 포트가 열려있는지 1초 동안 타임아웃 체크
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 3306), 1000);
            return "ONLINE";
        } catch (Exception e) {
            return "OFFLINE";
        }
    }
}
