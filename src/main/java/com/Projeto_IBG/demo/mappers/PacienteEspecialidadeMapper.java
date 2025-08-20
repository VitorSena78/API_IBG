package com.Projeto_IBG.demo.mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.Projeto_IBG.demo.dto.PacienteEspecialidadeDTO;
import com.Projeto_IBG.demo.dto.sync.SyncStatus;
import com.Projeto_IBG.demo.model.PacienteEspecialidade;

@Mapper(componentModel = "spring")
public interface PacienteEspecialidadeMapper {

    @Mapping(target = "pacienteServerId", expression = "java(entity.getPacienteId())")
    @Mapping(target = "especialidadeServerId", expression = "java(entity.getEspecialidadeId())")
    @Mapping(target = "pacienteLocalId", constant = "null")
    @Mapping(target = "especialidadeLocalId", constant = "null")
    @Mapping(source = "updatedAt", target = "lastSyncTimestamp", qualifiedByName = "dateTimeToTimestamp")
    @Mapping(target = "localPacienteEspecialidadeId", ignore = true)
    @Mapping(target = "serverPacienteEspecialidadeId", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "action", ignore = true)
    PacienteEspecialidadeDTO toDTO(PacienteEspecialidade entity);

    @Named("syncStatusToString")
    default String syncStatusToString(SyncStatus syncStatus) {
        return syncStatus != null ? syncStatus.name() : "PENDING";
    }

    @Named("dateTimeToTimestamp") 
    default Long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() : null;
    }
}