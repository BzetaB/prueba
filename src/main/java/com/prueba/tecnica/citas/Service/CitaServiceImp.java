package com.prueba.tecnica.citas.Service;

import com.prueba.tecnica.citas.Model.Cita;
import com.prueba.tecnica.citas.Model.Usuario;
import com.prueba.tecnica.citas.Repository.CitaRepository;
import com.prueba.tecnica.citas.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CitaServiceImp implements CitaService{

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    public CitaServiceImp(CitaRepository citaRepository, UsuarioRepository usuarioRepository){
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Cita crearCitaCliente(Cita cita, Usuario usuario) {

        usuario = usuarioRepository.findById(usuario.getIdusuario()).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getRol().getNombrerol().equalsIgnoreCase("CLIENTE")) {
            throw new IllegalArgumentException("No eres cliente");
        }
        cita.setFechacreacion(LocalDate.now());
        cita.setEstado("CREADO");
        return citaRepository.save(cita);
    }

    @Override
    public String asignarCitaAAgente(Cita cita, Usuario usuarioAdmin, Usuario usuarioAgente) {
        if (!usuarioAdmin.getRol().getNombrerol().equalsIgnoreCase("ADMIN")) {
            return "No eres ADMIN";
        }
        Cita citaAsignada = citaRepository.findById(cita.getIdcita()).orElseThrow(()-> new IllegalArgumentException("Cita no encontrada"));
        if(!citaAsignada.getEstado().equalsIgnoreCase("CREADO")){
            return "La cita no se encuentra disponible para asignar";
        }else if (citaAsignada.getUsuario() != null){
            return "La cita ya se encuentra asignada a " + cita.getUsuario().getNombre();
        }
        if (!usuarioAgente.getRol().getNombrerol().equalsIgnoreCase("AGENTE")) {
            return "El usuario no es un agente";
        }
        citaAsignada.setEstado("PENDIENTE");
        citaAsignada.setUsuario(usuarioAgente);
        citaAsignada.setFechaasignacion(cita.getFechaasignacion());
        this.actualizarCita(citaAsignada);
        return "CITA ASIGANADA CORRECTAMENTE AL AGENTE -> " +cita.getUsuario().getNombre()+".";
    }
    @Override
    public Cita actualizarCita(Cita cita) {
        Cita updateCita = citaRepository.findById(cita.getIdcita()).orElseThrow(()-> new IllegalArgumentException("Cita no encontrada"));
        updateCita.setFechaasignacion(cita.getFechaasignacion());
        updateCita.setFechafinal(cita.getFechafinal());
        updateCita.setUsuario(cita.getUsuario());
        return citaRepository.save(updateCita);
    }

    @Override
    public Cita atenderCita(Integer idcita, Usuario usuario) {
        Cita cita = citaRepository.findById(idcita).orElseThrow(()-> new IllegalArgumentException("Cita no encontrada"));
        if (cita.getUsuario() != usuario){
            throw new IllegalArgumentException("Cita no asignada a Agente " + usuario.getNombre());
        }
        cita.setEstado("COMPLETADO");
        return citaRepository.save(cita);
    }

    @Override
    public Cita abrirCitaCompletadaPorId(Integer cita, String estado) {

        if (!estado.equalsIgnoreCase("COMPLETADO"))
            throw new IllegalArgumentException("Estado invalido");
        Cita citacompletada = citaRepository.findById(cita).orElseThrow(()-> new IllegalArgumentException("Cita no encontrada"));
        if (!citacompletada.getEstado().equalsIgnoreCase(estado))
            throw new IllegalArgumentException("Estado invalido");
        return citaRepository.findByIdcitaAndEstado(cita,estado);
    }

    @Override
    public List<Cita> listarPorEstado(String estado, Usuario usuarioagente, Usuario usuarioPermisos) {
        usuarioagente = usuarioRepository.findById(usuarioagente.getIdusuario()).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
        usuarioPermisos = usuarioRepository.findById(usuarioPermisos.getIdusuario()).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));
        if (!usuarioPermisos.getRol().getNombrerol().equalsIgnoreCase("AGENTE") && !usuarioPermisos.getRol().getNombrerol().equalsIgnoreCase("ADMIN")) {
            throw new IllegalArgumentException("No eres agente ni admin");
        }else if (!usuarioagente.getRol().getNombrerol().equalsIgnoreCase("AGENTE")){
            throw new IllegalArgumentException("No eres agente");
        }
        return citaRepository.findAllByEstadoAndUsuario_IdusuarioAndUsuario_Rol_Nombrerol(estado,usuarioagente.getIdusuario(), usuarioagente.getRol().getNombrerol());
    }

    @Override
    public Cita finalizarCita(Cita cita) {
        Cita citafinal = citaRepository.findById(cita.getIdcita()).
                orElseThrow(()-> new IllegalArgumentException("Cita no encontrada"));
        if (!citafinal.getEstado().equalsIgnoreCase("COMPLETADO")){
            throw new IllegalArgumentException("CITA NO COMPLETADA");
        }
        citafinal.setEstado("FINALIZADO");
        citafinal.setFechafinal(cita.getFechafinal());
        this.actualizarCita(citafinal);
        return citaRepository.save(citafinal);
    }
}

