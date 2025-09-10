package com.Projeto_IBG.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Projeto_IBG.demo.websocket.NotificationWebSocketHandler;
import java.util.List;
import java.util.logging.Logger;

@Service
public class NotificationService {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;
    
    // ================================================================
    // Notificações para Paciente
    // ================================================================
    
    public void notifyPacienteCreated(Integer pacienteId, Object paciente) {
        LOGGER.info("Enviando notificação de paciente criado: ID " + pacienteId);
        webSocketHandler.notifyPacienteChange("CREATED", pacienteId, paciente);
    }
    
    public void notifyPacienteUpdated(Integer pacienteId, Object paciente) {
        LOGGER.info("Enviando notificação de paciente atualizado: ID " + pacienteId);
        webSocketHandler.notifyPacienteChange("UPDATED", pacienteId, paciente);
    }
    
    public void notifyPacienteDeleted(Integer pacienteId) {
        LOGGER.info("Enviando notificação de paciente deletado: ID " + pacienteId);
        webSocketHandler.notifyPacienteChange("DELETED", pacienteId, null);
    }
    
    // ================================================================
    // ✅ NOVO: Notificações para PacienteEspecialidade COM DADOS
    // ================================================================
    
    /**
     * Notifica sobre uma associação específica criada
     */
    public void notifyPacienteEspecialidadeCreated(Integer pacienteId, Object pacienteEspecialidadeData) {
        LOGGER.info("Enviando notificação de associação criada: Paciente " + pacienteId);
        webSocketHandler.notifyPacienteEspecialidadeCreated(pacienteId, pacienteEspecialidadeData);
    }
    
    /**
     * Notifica sobre uma associação específica atualizada
     */
    public void notifyPacienteEspecialidadeUpdated(Integer pacienteId, Object pacienteEspecialidadeData) {
        LOGGER.info("Enviando notificação de associação atualizada: Paciente " + pacienteId);
        webSocketHandler.notifyPacienteEspecialidadeUpdated(pacienteId, pacienteEspecialidadeData);
    }
    
    /**
     * Notifica sobre uma associação específica deletada
     */
    public void notifyPacienteEspecialidadeDeleted(Integer pacienteId, Integer especialidadeId) {
        LOGGER.info("Enviando notificação de associação deletada: Paciente " + pacienteId + " - Especialidade " + especialidadeId);
        webSocketHandler.notifyPacienteEspecialidadeDeleted(pacienteId, especialidadeId);
    }
    
    /**
     * ✅ NOVO: Notifica sobre múltiplas associações (operação em batch)
     */
    public void notifyPacienteEspecialidadeBatchCreated(Integer pacienteId, List<?> associacoesList) {
        LOGGER.info("Enviando notificação de batch de associações criadas: Paciente " + pacienteId + 
                   " - " + (associacoesList != null ? associacoesList.size() : 0) + " associações");
        webSocketHandler.notifyPacienteEspecialidadeBatch("BATCH_CREATED", pacienteId, associacoesList);
    }
    
    public void notifyPacienteEspecialidadeBatchDeleted(Integer pacienteId, List<?> associacoesDeletadas) {
        LOGGER.info("Enviando notificação de batch de associações deletadas: Paciente " + pacienteId + 
                   " - " + (associacoesDeletadas != null ? associacoesDeletadas.size() : 0) + " associações");
        webSocketHandler.notifyPacienteEspecialidadeBatch("BATCH_DELETED", pacienteId, associacoesDeletadas);
    }
    
    /**
     * ✅ NOVO: Notifica sobre atualização completa das associações de um paciente
     */
    public void notifyPacienteEspecialidadeCompleteUpdate(Integer pacienteId, List<?> novasAssociacoes) {
        LOGGER.info("Enviando notificação de atualização completa de associações: Paciente " + pacienteId + 
                   " - " + (novasAssociacoes != null ? novasAssociacoes.size() : 0) + " associações");
        webSocketHandler.notifyPacienteEspecialidadeBatch("COMPLETE_UPDATE", pacienteId, novasAssociacoes);
    }
    
    // ================================================================
    // DEPRECATED: Mantido para compatibilidade
    // ================================================================
    
    @Deprecated
    public void notifyPacienteEspecialidadeCreated(Integer pacienteId) {
        LOGGER.warning("DEPRECATED: Use notifyPacienteEspecialidadeCreated(pacienteId, data)");
        webSocketHandler.notifyPacienteEspecialidadeChange("created", pacienteId);
    }
    
    @Deprecated
    public void notifyPacienteEspecialidadeUpdated(Integer pacienteId) {
        LOGGER.warning("DEPRECATED: Use notifyPacienteEspecialidadeUpdated(pacienteId, data)");
        webSocketHandler.notifyPacienteEspecialidadeChange("updated", pacienteId);
    }
    
    @Deprecated
    public void notifyPacienteEspecialidadeDeleted(Integer pacienteId) {
        LOGGER.warning("DEPRECATED: Use notifyPacienteEspecialidadeDeleted(pacienteId, especialidadeId)");
        webSocketHandler.notifyPacienteEspecialidadeChange("deleted", pacienteId);
    }
    
    // ================================================================
    // Notificações para Especialidade
    // ================================================================
    
    public void notifyEspecialidadeCreated(Integer especialidadeId, Object especialidade) {
        LOGGER.info("Enviando notificação de especialidade criada: ID " + especialidadeId);
        webSocketHandler.notifyEspecialidadeChange("CREATED", especialidadeId, especialidade);
    }
    
    public void notifyEspecialidadeUpdated(Integer especialidadeId, Object especialidade) {
        LOGGER.info("Enviando notificação de especialidade atualizada: ID " + especialidadeId);
        webSocketHandler.notifyEspecialidadeChange("UPDATED", especialidadeId, especialidade);
    }
    
    public void notifyEspecialidadeDeleted(Integer especialidadeId) {
        LOGGER.info("Enviando notificação de especialidade deletada: ID " + especialidadeId);
        webSocketHandler.notifyEspecialidadeChange("DELETED", especialidadeId, null);
    }
    
    // ================================================================
    // Notificação de Sistema
    // ================================================================
    
    public void notifySystemMessage(String message) {
        LOGGER.info("Enviando mensagem do sistema: " + message);
        webSocketHandler.notifySystem(message);
    }
}