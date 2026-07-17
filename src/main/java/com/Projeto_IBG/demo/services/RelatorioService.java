package com.Projeto_IBG.demo.services;

import com.Projeto_IBG.demo.repositories.AtendimentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RelatorioService {

    private final AtendimentoRepository atendimentoRepository;

    public RelatorioService(AtendimentoRepository atendimentoRepository) {
        this.atendimentoRepository = atendimentoRepository;
    }

    public Map<String, Object> relatorioDiario(LocalDate data) {
        Map<String, Object> relatorio = new LinkedHashMap<>();
        relatorio.put("data", data.toString());
        relatorio.put("total_atendimentos", atendimentoRepository.countByDataAtendimento(data));

        List<Map<String, Object>> porEspecialidade = new ArrayList<>();
        List<Object[]> resultados = atendimentoRepository.countByEspecialidade(data);
        for (Object[] row : resultados) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("especialidade", row[0]);
            item.put("total", row[1]);
            porEspecialidade.add(item);
        }
        relatorio.put("por_especialidade", porEspecialidade);
        return relatorio;
    }

    public List<Map<String, Object>> relatorioMensal(int ano) {
        LocalDate inicio = LocalDate.of(ano, 1, 1);
        LocalDate fim = LocalDate.of(ano, 12, 31);

        List<Map<String, Object>> relatorio = new ArrayList<>();
        List<Object[]> resultados = atendimentoRepository.countByMes(inicio, fim);
        for (Object[] row : resultados) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("ano", row[0]);
            item.put("mes", row[1]);
            item.put("total", row[2]);
            relatorio.add(item);
        }
        return relatorio;
    }

    public Map<String, Object> resumoGeral() {
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("total_pacientes", atendimentoRepository.count());
        resumo.put("total_hoje", atendimentoRepository.countByDataAtendimento(LocalDate.now()));
        resumo.put("aguardando_triagem",
                atendimentoRepository.countByStatus(com.Projeto_IBG.demo.model.Atendimento.StatusAtendimento.AGUARDANDO_TRIAGEM));
        resumo.put("aguardando_consulta",
                atendimentoRepository.countByStatus(com.Projeto_IBG.demo.model.Atendimento.StatusAtendimento.AGUARDANDO_CONSULTA));
        resumo.put("em_atendimento",
                atendimentoRepository.countByStatus(com.Projeto_IBG.demo.model.Atendimento.StatusAtendimento.EM_CONSULTA));
        return resumo;
    }
}
