package br.com.futurehub.futurehubgs.application.service.impl;

import br.com.futurehub.futurehubgs.application.dto.IdeiaCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.IdeiaResponse;
import br.com.futurehub.futurehubgs.application.dto.IdeiaUpdateRequest;
import br.com.futurehub.futurehubgs.application.service.IdeiaService;
import br.com.futurehub.futurehubgs.domain.Ideia;
import br.com.futurehub.futurehubgs.domain.Missao;
import br.com.futurehub.futurehubgs.domain.Usuario;
import br.com.futurehub.futurehubgs.infrastructure.repository.IdeiaRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.MissaoRepository;
import br.com.futurehub.futurehubgs.infrastructure.repository.UsuarioRepository;
import br.com.futurehub.futurehubgs.messaging.IdeaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdeiaServiceImpl implements IdeiaService {

    private final IdeiaRepository ideiaRepo;
    private final UsuarioRepository usuarioRepo;
    private final MissaoRepository missaoRepo;
    private final IdeaEventPublisher publisher;

    private IdeiaResponse toResp(Ideia i) {
        return new IdeiaResponse(
                i.getId(),
                i.getTitulo(),
                i.getDescricao(),
                i.getAutor() != null ? i.getAutor().getId() : null,
                i.getAutor() != null ? i.getAutor().getNome() : null,
                i.getMissao() != null ? i.getMissao().getId() : null,
                i.getMediaNotas(),
                i.getTotalAvaliacoes(),
                i.getCreatedAt()
        );
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse criar(IdeiaCreateRequest req) {

        Usuario autor = usuarioRepo.findById(req.idUsuario())
                .orElseThrow(() -> new IllegalArgumentException("erro.usuario.nao.encontrado"));

        Missao missao = null;
        if (req.idMissao() != null) {
            missao = missaoRepo.findById(req.idMissao())
                    .orElseThrow(() -> new IllegalArgumentException("erro.missao.nao.encontrada"));
        }

        Ideia ideia = Ideia.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .autor(autor)
                .missao(missao)
                .mediaNotas(0.0)
                .totalAvaliacoes(0)
                .createdAt(LocalDateTime.now())
                .build();

        ideia = ideiaRepo.save(ideia);

        // evento assíncrono (ranking, cache, etc.)
        publisher.publishCreated(ideia);

        return toResp(ideia);
    }

    @Override
    @Cacheable(
            value = "ideiasPorArea",
            key = "#areaId + '-' + (#q == null ? '' : #q.trim()) + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public Page<IdeiaResponse> listar(Long areaId, String q, Pageable pageable) {

        Page<Ideia> page;
        String query = (q == null || q.isBlank()) ? null : q.trim();

        if (areaId != null && query != null) {
            page = ideiaRepo.findByAutor_AreaInteresse_IdAndTituloContainingIgnoreCase(
                    areaId, query, pageable
            );
        } else if (areaId != null) {
            page = ideiaRepo.findByAutor_AreaInteresse_Id(areaId, pageable);
        } else if (query != null) {
            page = ideiaRepo.findByTituloContainingIgnoreCase(query, pageable);
        } else {
            page = ideiaRepo.findAll(pageable);
        }

        return page.map(this::toResp);
    }

    @Override
    public IdeiaResponse buscar(Long id) {
        Ideia ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        return toResp(ideia);
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public IdeiaResponse atualizar(Long id, IdeiaUpdateRequest req) {
        Ideia ideia = ideiaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("erro.ideia.nao.encontrada"));

        ideia.setTitulo(req.titulo());
        ideia.setDescricao(req.descricao());
        // flush automático ao final da transação (JPA)

        return toResp(ideia);
    }

    @Override
    @CacheEvict(value = "ideiasPorArea", allEntries = true)
    public void deletar(String id) {

        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("erro.ideia.id.invalido");
        }

        try {
            ideiaRepo.deleteById(idLong);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("erro.ideia.nao.encontrada");
        }
    }
}






