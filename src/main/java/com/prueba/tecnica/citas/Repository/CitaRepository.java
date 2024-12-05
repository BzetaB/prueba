package com.prueba.tecnica.citas.Repository;

import com.prueba.tecnica.citas.Model.Cita;
import com.prueba.tecnica.citas.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findAllByEstadoAndUsuario_IdusuarioAndUsuario_Rol_Nombrerol(String estado, Integer idusuario, String usuariorol);
    Cita findByIdcitaAndEstado(Integer idcita, String estado);
}
