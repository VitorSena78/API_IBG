package com.Projeto_IBG.demo.mappers;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;

@Mapper(componentModel = "spring")
public interface PacienteEspecialidadeMapper {
    
    @Mapping(source = "pacienteId", target = "pacienteServerId")
    @Mapping(target = "pacienteLocalId", expression = "java(null)")
    @Mapping(source = "especialidadeId", target = "especialidadeServerId")
    @Mapping(target = "especialidadeLocalId", expression = "java(null)")
    @Mapping(source = "syncStatus", target = "action", qualifiedByName = "syncStatusToString")
    @Mapping(source = "updatedAt", target = "lastSyncTimestamp", qualifiedByName = "dateTimeToTimestamp")

    // Mapeamentos para as propriedades que estavam causando erro:
    @Mapping(target = "localPacienteEspecialidadeId", ignore = true) // ignorado por enquanto
    @Mapping(target = "serverPacienteEspecialidadeId", ignore = true) // ignorado por enquanto
    @Mapping(target = "isDeleted", constant = "false") // assumi que entidades existentes não estão deletadas

    PacienteEspecialidadeDTO toDTO(PacienteEspecialidade entity);

    
    
    @Named("integerToString")
    default String integerToString(Integer value) {
        return value != null ? value.toString() : null;
    }
    
    @Named("syncStatusToString")
    default String syncStatusToString(SyncStatus syncStatus) {
        return syncStatus != null ? syncStatus.name() : "PENDING";
    }
    
    @Named("dateTimeToTimestamp")
    default Long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toEpochSecond(java.time.ZoneOffset.UTC) : null;
    }
}
