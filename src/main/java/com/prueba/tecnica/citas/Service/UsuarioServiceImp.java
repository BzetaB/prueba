package com.prueba.tecnica.citas.Service;

import com.prueba.tecnica.citas.Model.Usuario;
import com.prueba.tecnica.citas.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    public UsuarioServiceImp(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Integer idusuario) {
        return usuarioRepository.findById(idusuario).orElse(null);
    }
}
