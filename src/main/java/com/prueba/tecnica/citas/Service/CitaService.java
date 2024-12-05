package com.prueba.tecnica.citas.Service;

import com.prueba.tecnica.citas.Model.Cita;
import com.prueba.tecnica.citas.Model.Usuario;

import java.util.List;

public interface CitaService {
    Cita crearCitaCliente(Cita cita, Usuario usuario);
    String asignarCitaAAgente(Cita cita, Usuario usuarioAdmin, Usuario idusuarioAgente);
    Cita actualizarCita(Cita cita);
    Cita atenderCita(Integer idcita, Usuario usuario);
    Cita abrirCitaCompletadaPorId(Integer cita, String estado);
    List<Cita> listarPorEstado(String estado, Usuario usuarioagente, Usuario usuarioPermisos);
    Cita finalizarCita(Cita cita);
}
