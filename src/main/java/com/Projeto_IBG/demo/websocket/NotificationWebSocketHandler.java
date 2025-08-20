package com.Projeto_IBG.demo.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class NotificationWebSocketHandler implements WebSocketHandler {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationWebSocketHandler.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Set thread-safe para armazenar sessões ativas
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        LOGGER.info("Nova conexão WebSocket estabelecida: " + session.getId());
        
        // Enviar mensagem de boas-vindas
        String welcomeMessage = "{\"type\":\"system\",\"message\":\"Conectado com sucesso\"}";
        session.sendMessage(new TextMessage(welcomeMessage));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Processar mensagens recebidas do cliente se necessário
        LOGGER.info("Mensagem recebida: " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOGGER.log(Level.SEVERE, "Erro no transporte WebSocket: " + session.getId(), exception);
        sessions.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        LOGGER.info("Conexão WebSocket fechada: " + session.getId() + " - " + closeStatus.toString());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * Envia notificação para todos os clientes conectados
     */
    public void broadcastNotification(String type, String action, Object data) {
        if (sessions.isEmpty()) {
            LOGGER.info("Nenhuma sessão WebSocket ativa para broadcast");
            return;
        }

        try {
            NotificationMessage notification = new NotificationMessage(type, action, data);
            String jsonMessage = objectMapper.writeValueAsString(notification);
            
            // Enviar para todas as sessões ativas
            sessions.parallelStream().forEach(session -> {
                try {
                    if (session.isOpen()) {
                        synchronized (session) {
                            session.sendMessage(new TextMessage(jsonMessage));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Erro ao enviar mensagem para sessão: " + session.getId(), e);
                    sessions.remove(session);
                }
            });
            
            LOGGER.info("Notificação enviada para " + sessions.size() + " clientes: " + type + "." + action);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao criar/enviar notificação", e);
        }
    }

    /**
     * Classe para estruturar as mensagens de notificação
     */
    public static class NotificationMessage {
        private String type;
        private String action;
        private Object data;
        private long timestamp;

        public NotificationMessage(String type, String action, Object data) {
            this.type = type;
            this.action = action;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters e Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}