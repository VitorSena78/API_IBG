package com.Projeto_IBG.demo.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Component
public class NotificationWebSocketHandler implements WebSocketHandler {

    private static final Logger LOGGER = Logger.getLogger(NotificationWebSocketHandler.class.getName());
    
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        LOGGER.info("Nova conexão WebSocket estabelecida: " + session.getId());
        sendMessageToSession(session, createSystemMessage("Conectado com sucesso"));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
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

    // ================================================================
    //  Métodos públicos para enviar diferentes tipos de notificações
    // ================================================================

    public void notifySystem(String message) {
        try {
            String json = createSystemMessage(message);
            broadcastRaw(json);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao enviar notificação do sistema", e);
        }
    }

    //  Notificação de alteração em Paciente 
    public void notifyPacienteChange(String action, Integer pacienteId, Object data) {
        NotificationMessage notification = new NotificationMessage(
                "paciente", action, pacienteId, data
        );
        broadcast(notification);
    }

    //  Notificação específica de associação com dados completos
    public void notifyPacienteEspecialidadeCreated(Integer pacienteId, Object pacienteEspecialidadeData) {
        NotificationMessage notification = new NotificationMessage(
                "paciente_especialidade", "CREATED", pacienteId, pacienteEspecialidadeData
        );
        broadcast(notification);
        LOGGER.info("Notificação de associação criada enviada: Paciente " + pacienteId);
    }

    public void notifyPacienteEspecialidadeUpdated(Integer pacienteId, Object pacienteEspecialidadeData) {
        NotificationMessage notification = new NotificationMessage(
                "paciente_especialidade", "UPDATED", pacienteId, pacienteEspecialidadeData
        );
        broadcast(notification);
        LOGGER.info("Notificação de associação atualizada enviada: Paciente " + pacienteId);
    }

    public void notifyPacienteEspecialidadeDeleted(Integer pacienteId, Integer especialidadeId) {
        // Para deleção, enviamos os IDs específicos
        Map<String, Object> deleteData = new HashMap<>();
        deleteData.put("pacienteId", pacienteId);
        deleteData.put("especialidadeId", especialidadeId);
        
        NotificationMessage notification = new NotificationMessage(
                "paciente_especialidade", "DELETED", pacienteId, deleteData
        );
        broadcast(notification);
        LOGGER.info("Notificação de associação deletada enviada: Paciente " + pacienteId + " - Especialidade " + especialidadeId);
    }

    // notify para a deleção em lote
    public void notifyPacienteEspecialidadeDeletedBatch(Integer pacienteId) {
        NotificationMessage notification = new NotificationMessage(
            "paciente_especialidade", "DELETED_BATCH", pacienteId, null
        );
        broadcast(notification);
        LOGGER.info("Notificação de deleção em lote enviada para paciente " + pacienteId);
    }

    // Notificação em batch para múltiplas associações
    public void notifyPacienteEspecialidadeBatch(String action, Integer pacienteId, List<?> associacoesList) {
        Map<String, Object> batchData = new HashMap<>();
        batchData.put("pacienteId", pacienteId);
        batchData.put("associacoes", associacoesList);
        batchData.put("count", associacoesList != null ? associacoesList.size() : 0);
        
        NotificationMessage notification = new NotificationMessage(
                "paciente_especialidade_batch", action, pacienteId, batchData
        );
        broadcast(notification);
        LOGGER.info("Notificação em batch enviada: " + action + " para paciente " + pacienteId + 
                   " com " + (associacoesList != null ? associacoesList.size() : 0) + " associações");
    }

    // Mantido para compatibilidade, mas DEPRECATED
    @Deprecated
    public void notifyPacienteEspecialidadeChange(String action, Integer pacienteId) {
        LOGGER.warning("DEPRECATED: Use os métodos específicos com dados completos");
        NotificationMessage notification = new NotificationMessage(
                "paciente_especialidade", action, pacienteId, null
        );
        broadcast(notification);
    }

    public void notifyEspecialidadeChange(String action, Integer especialidadeId, Object data) {
        NotificationMessage notification = new NotificationMessage(
                "especialidade", action, especialidadeId, data
        );
        broadcast(notification);
    }

    // ================================================================
    // Métodos internos de envio
    // ================================================================

    private void broadcast(NotificationMessage notification) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            LOGGER.info("JSON serializado: " + jsonMessage.substring(0, Math.min(200, jsonMessage.length())) + "...");
            broadcastRaw(jsonMessage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao serializar/enviar notificação", e);
        }
    }

    private void broadcastRaw(String jsonMessage) {
        if (sessions.isEmpty()) {
            LOGGER.info("Nenhuma sessão WebSocket ativa para broadcast");
            return;
        }

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

        LOGGER.info("Mensagem enviada para " + sessions.size() + " clientes");
    }

    private void sendMessageToSession(WebSocketSession session, String jsonMessage) {
        try {
            if (session.isOpen()) {
                synchronized (session) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            } else {
                sessions.remove(session);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao enviar mensagem para sessão: " + session.getId(), e);
            sessions.remove(session);
        }
    }

    private String createSystemMessage(String message) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        msg.put("type", "system");
        msg.put("message", message);
        msg.put("timestamp", System.currentTimeMillis());
        return objectMapper.writeValueAsString(msg);
    }

    // ================================================================
    // Classe interna de mensagens - ATUALIZADA
    // ================================================================

    public static class NotificationMessage {
        private String type;
        private String action;
        private Integer pacienteId;
        private Object data;
        private long timestamp;

        public NotificationMessage(String type, String action, Integer pacienteId, Object data) {
            this.type = type;
            this.action = action;
            this.pacienteId = pacienteId;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters e Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public Integer getPacienteId() { return pacienteId; }
        public void setPacienteId(Integer pacienteId) { this.pacienteId = pacienteId; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}