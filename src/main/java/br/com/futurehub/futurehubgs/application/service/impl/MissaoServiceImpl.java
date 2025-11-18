package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.MissaoResponse;
import br.com.futurehub.futurehubgs.application.service.MissaoService;
import br.com.futurehub.futurehubgs.domain.Area;
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
    @CacheEvict(value = "missoesPorArea", allEntries = true)
    public MissaoResponse gerarMissaoPorArea(String areaId) {
        if (areaId == null || areaId.isBlank()) {
            throw new IllegalArgumentException("erro.area.id.invalido");
        }

        Long areaIdLong;
        try {
            areaIdLong = Long.valueOf(areaId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("erro.area.id.invalido");
        }

        Area area = areaRepo.findById(areaIdLong)
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

        for (String l : content.split("\\r?\\n")) {
            String line = l.trim();
            String up = line.toUpperCase();
            if (up.startsWith("DESCRICAO:")) {
                descricao = line.substring("DESCRICAO:".length()).trim();
            } else if (up.startsWith("OBJETIVO:")) {
                objetivo = line.substring("OBJETIVO:".length()).trim();
            } else if (up.startsWith("MORAL:")) {
                moral = line.substring("MORAL:".length()).trim();
            }
        }

        Missao missao = Missao.builder()
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
    public Page<MissaoResponse> listarPorArea(String areaId, Pageable pageable) {
        Page<Missao> page;

        if (areaId != null && !areaId.isBlank()) {
            Long areaIdLong;
            try {
                areaIdLong = Long.valueOf(areaId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("erro.area.id.invalido");
            }
            page = missaoRepo.findByArea_Id(areaIdLong, pageable);
        } else {
            page = missaoRepo.findAll(pageable);
        }

        return page.map(this::toResponse);
    }
}





