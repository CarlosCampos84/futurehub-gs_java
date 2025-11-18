package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import br.com.futurehub.futurehubgs.application.service.MissaoService;
import br.com.futurehub.futurehubgs.domain.Missao;
import br.com.futurehub.futurehubgs.infrastructure.repository.AreaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.MissaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MissaoServiceImpl implements MissaoService {

    private final MissaoRepository missaoRepo;
    private final AreaRepository areaRepo;
    private final ChatClient.Builder chatClientBuilder;

    private MissaoResponse toResponse(Missao m) {
        return new MissaoResponse(
                m.getId(),
                m.getDescricao(),
                m.getObjetivo(),
                m.getMoral(),
                m.getArea() != null ? m.getArea().getId() : null,
                m.getArea() != null ? m.getArea().getNome() : null,
                m.isGeradaPorIa(),
                m.getDataCriacao()
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "missoesPorArea", allEntries = true)
    public MissaoResponse gerarMissaoPorArea(Long areaId) {
        var area = areaRepo.findById(areaId)
                .orElseThrow(() -> new IllegalArgumentException("erro.area.nao.encontrada"));

        ChatClient chatClient = chatClientBuilder.build();

        String prompt = """
                Você é um assistente educacional.
                Crie uma missão curta ligada à área: %s.
                Responda em três linhas, exatamente neste formato:
                DESCRICAO: ...
                OBJETIVO: ...
                MORAL: ...
                """.formatted(area.getNome());

        String content = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        String descricao = content;
        String objetivo = "Aprender algo novo na área de " + area.getNome();
        String moral = "Compartilhar conhecimento gera impacto positivo.";

        for (String line : content.split("\\r?\\n")) {
            String l = line.trim();
            String upper = l.toUpperCase();
            if (upper.startsWith("DESCRICAO:")) {
                descricao = l.substring("DESCRICAO:".length()).trim();
            } else if (upper.startsWith("OBJETIVO:")) {
                objetivo = l.substring("OBJETIVO:".length()).trim();
            } else if (upper.startsWith("MORAL:")) {
                moral = l.substring("MORAL:".length()).trim();
            }
        }

        var missao = Missao.builder()
                .descricao(descricao)
                .objetivo(objetivo)
                .moral(moral)
                .area(area)
                .dataCriacao(LocalDateTime.now())
                .status("ATIVA")
                .geradaPorIa(true)
                .build();

        missao = missaoRepo.save(missao);
        return toResponse(missao);
    }

    @Override
    @Cacheable(
            value = "missoesPorArea",
            key = "#areaId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public Page<MissaoResponse> listarPorArea(Long areaId, Pageable pageable) {
        Page<Missao> page;

        if (areaId != null) {
            page = missaoRepo.findByArea_Id(areaId, pageable);
        } else {
            page = missaoRepo.findAll(pageable);
        }

        return page.map(this::toResponse);
    }
}


