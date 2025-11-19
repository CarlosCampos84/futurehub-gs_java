package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.AvaliacaoCreateRequest;
import br.com.futurehub.futurehubgs.application.service.AvaliacaoService;
import br.com.futurehub.futurehubgs.domain.Avaliacao;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.infrastructure.repository.AvaliacaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.messaging.AvaliacaoEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoServiceImpl implements AvaliacaoService {

    private final IdeiaRepository ideiaRepo;
    private final AvaliacaoRepository avaliacaoRepo;
    private final AvaliacaoEventPublisher publisher;

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void avaliar(AvaliacaoCreateRequest req) {

        // ID da ideia já é String no DTO
        String ideiaId = req.idIdeia();

        Ideia ideia = ideiaRepo.findById(ideiaId)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        // Cria a avaliação (comentário opcional, se existir na entidade)
        Avaliacao avaliacao = Avaliacao.builder()
                .ideiaId(ideia.getId())
                .nota(req.nota())
                .createdAt(LocalDateTime.now())
                .build();

        avaliacaoRepo.save(avaliacao);

        // Atualiza média e total de avaliações da ideia
        int totalAnterior = ideia.getTotalAvaliacoes() != null ? ideia.getTotalAvaliacoes() : 0;
        double mediaAnterior = ideia.getMediaNotas() != null ? ideia.getMediaNotas() : 0.0;

        int totalNovo = totalAnterior + 1;
        double somaAnterior = mediaAnterior * totalAnterior;
        double mediaNova = (somaAnterior + req.nota()) / totalNovo;

        ideia.setTotalAvaliacoes(totalNovo);
        ideia.setMediaNotas(mediaNova);
        ideiaRepo.save(ideia);

        // Dispara evento para o ranking (consumido pelo AvaliacaoEventListener)
        publisher.publishAvaliacao(ideia.getId(), req.nota());
    }
}





