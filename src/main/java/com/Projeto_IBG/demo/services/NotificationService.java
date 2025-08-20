package com.Projeto_IBG.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Projeto_IBG.demo.websocket.NotificationWebSocketHandler;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;
    
    // Notificações para Paciente
    public void notifyPacienteCreated(Object paciente) {
        webSocketHandler.broadcastNotification("paciente", "created", paciente);
    }
    
    public void notifyPacienteUpdated(Object paciente) {
        webSocketHandler.broadcastNotification("paciente", "updated", paciente);
    }
    
    public void notifyPacienteDeleted(Integer pacienteId) {
        webSocketHandler.broadcastNotification("paciente", "deleted", 
            java.util.Map.of("id", pacienteId));
    }
    
    // Notificações para PacienteEspecialidade
    public void notifyPacienteEspecialidadeCreated(Object pacienteEspecialidade) {
        webSocketHandler.broadcastNotification("paciente_especialidade", "created", pacienteEspecialidade);
    }
    
    public void notifyPacienteEspecialidadeUpdated(Object pacienteEspecialidade) {
        webSocketHandler.broadcastNotification("paciente_especialidade", "updated", pacienteEspecialidade);
    }
    
    public void notifyPacienteEspecialidadeDeleted(Integer pacienteId, Integer especialidadeId) {
        webSocketHandler.broadcastNotification("paciente_especialidade", "deleted",
            java.util.Map.of("pacienteId", pacienteId, "especialidadeId", especialidadeId));
    }
    
    // ADICIONADO: Notificações para Especialidade
    public void notifyEspecialidadeCreated(Object especialidade) {
        webSocketHandler.broadcastNotification("especialidade", "created", especialidade);
    }
    
    public void notifyEspecialidadeUpdated(Object especialidade) {
        webSocketHandler.broadcastNotification("especialidade", "updated", especialidade);
    }
    
    public void notifyEspecialidadeDeleted(Integer especialidadeId) {
        webSocketHandler.broadcastNotification("especialidade", "deleted",
            java.util.Map.of("id", especialidadeId));
    }
}