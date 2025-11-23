package br.com.futurehub.futurehubgs.application.service;

import br.com.futurehub.futurehubgs.application.dto.UsuarioCreateRequest;
import br.com.futurehub.futurehubgs.application.dto.UsuarioResponse;

public interface UsuarioService {

    UsuarioResponse criar(UsuarioCreateRequest request);
}
