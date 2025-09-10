package com.Projeto_IBG.demo.mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.Projeto_IBG.demo.dto.PacienteDTO;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.Paciente;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    // DTO para Entity
    @Mapping(source = "serverId", target = "id")
    @Mapping(source = "endereco", target = "endereço")
    @Mapping(source = "syncStatus", target = "syncStatus", qualifiedByName = "stringToSyncStatus")
    @Mapping(target = "especialidades", ignore = true) // Ignora o mapeamento das especialidades
    Paciente toEntity(PacienteDTO dto);

    // Entity para DTO
    @Mapping(source = "id", target = "serverId")
    @Mapping(source = "endereço", target = "endereco")
    @Mapping(source = "syncStatus", target = "syncStatus", qualifiedByName = "syncStatusToString")
    @Mapping(source = "updatedAt", target = "lastSyncTimestamp", qualifiedByName = "dateTimeToTimestamp")
    PacienteDTO toDTO(Paciente entity);

    @Named("stringToSyncStatus")
    default SyncStatus stringToSyncStatus(String syncStatus) {
        if (syncStatus == null) {
            return SyncStatus.PENDING;
        }
        try {
            return SyncStatus.valueOf(syncStatus);
        } catch (IllegalArgumentException e) {
            return SyncStatus.PENDING;
        }
    }

    @Named("syncStatusToString")
    default String syncStatusToString(SyncStatus syncStatus) {
        return syncStatus != null ? syncStatus.name() : "PENDING";
    }

    @Named("dateTimeToTimestamp")
    default Long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() : null;
    }
}