package com.Projeto_IBG.demo.mappers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.Projeto_IBG.demo.dto.EspecialidadeDTO;
import com.Projeto_IBG.demo.model.Especialidade;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper {
    
    // Entity para DTO
    EspecialidadeDTO toDTO(Especialidade entity);
    
    // DTO para Entity  
    Especialidade toEntity(EspecialidadeDTO dto);
    
    @Named("dateTimeToTimestamp")
    default Long dateTimeToTimestamp(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli() : null;
    }
}
