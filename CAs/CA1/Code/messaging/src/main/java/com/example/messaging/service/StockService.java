package com.example.messaging.service;

import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue outQueue;

    private final Map<String, Integer> portfolio = new HashMap<>();

    @JmsListener(destination = "INQ")
    public void receiveMessage(String message) {
        // System.out.println("Received Message: " + message);

        String response = processMessage(message);

        jmsTemplate.send(outQueue, session -> session.createTextMessage(response));
        // System.out.println("Sent Response to OUTQ: " + response);
    }

    private String processMessage(String message) {
        String[] parts = message.split(" ");
        if (parts.length == 0) return "Invalid command";

        String command = parts[0];

        switch (command) {
            case "BUY":
                return handleBuy(parts);
            case "SELL":
                return handleSell(parts);
            case "ADD":
                return handleAdd(parts);
            case "PORTFOLIO":
                return handlePortfolio();
            default:
                return "Unknown command: " + message;
        }
    }

    private String handleBuy(String[] parts) {
        if (parts.length != 3) return "Invalid BUY command";

        String security = parts[1];
        int amount;

        try {
            amount = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "Invalid amount";
        }

        if (!portfolio.containsKey(security)) {
            return "1 Unknown security";
        }

        portfolio.put(security, portfolio.get(security) + amount);
        return "0 Trade successful";
    }

    private String handleSell(String[] parts) {
        if (parts.length != 3) return "Invalid SELL command";

        String security = parts[1];
        int amount;

        try {
            amount = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return "Invalid amount";
        }

        if (!portfolio.containsKey(security)) {
            return "1 Unknown security";
        }

        int currentAmount = portfolio.get(security);
        if (currentAmount < amount) {
            return "2 Not enough positions";
        }

        portfolio.put(security, currentAmount - amount);
        return "0 Trade successful";
    }

    private String handleAdd(String[] parts) {
        if (parts.length != 2) return "Invalid ADD command";

        String security = parts[1];
        portfolio.putIfAbsent(security, 0);
        return "0 Success";
    }

    private String handlePortfolio() {
        if (portfolio.isEmpty()) {
            return "0"; // Empty portfolio
        }

        StringBuilder result = new StringBuilder("0 ");
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            result.append(entry.getKey()).append(" ").append(entry.getValue()).append(" | ");
        }

        return result.substring(0, result.length() - 3);
    }
}
