package com.prueba.tecnica.citas.Controller;

import com.prueba.tecnica.citas.Model.Cita;
import com.prueba.tecnica.citas.Model.Usuario;
import com.prueba.tecnica.citas.Service.CitaService;
import com.prueba.tecnica.citas.Service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cita")
public class CitaController {

    @Autowired
    private CitaService citaService;
    @Autowired
    private UsuarioService usuarioService;


    @GetMapping("/{idusuarioAgente}")
    public ResponseEntity<List<Cita>>listarCitaPorEstadoYUsuario(@PathVariable int idusuarioAgente,
                                                                 @RequestParam String estado, @RequestParam Integer usuarioPermisos) {
        Usuario permisos = usuarioService.obtenerUsuarioPorId(usuarioPermisos);
        Usuario usuarioagente = usuarioService.obtenerUsuarioPorId(idusuarioAgente);
        List<Cita> lista = citaService.listarPorEstado(estado, usuarioagente, permisos);
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{idusuario}")
    public ResponseEntity<Cita> creacionDeCita(@PathVariable("idusuario")Integer idusuario, @RequestBody Cita cita){
        Usuario usuario = usuarioService.obtenerUsuarioPorId(idusuario);
        cita = citaService.crearCitaCliente(cita,usuario);
        return ResponseEntity.ok(cita);
    }

    @PutMapping("/{idcita}")
    public ResponseEntity<String> asignarCita(@PathVariable("idcita")Integer idcita,@RequestParam Integer idusuarioAdmin,  @RequestBody Cita cita){
        Usuario admin = usuarioService.obtenerUsuarioPorId(idusuarioAdmin);
        Usuario agente = usuarioService.obtenerUsuarioPorId(cita.getUsuario().getIdusuario());
        String rptcita = citaService.asignarCitaAAgente(cita,admin,agente);
        return ResponseEntity.ok(rptcita);
    }

    @GetMapping("/atender")
    public  ResponseEntity<Cita> citaatendida(@RequestParam Integer idcita, @RequestParam Integer idusuario){
        Usuario agente = usuarioService.obtenerUsuarioPorId(idusuario);
        Cita cita = citaService.atenderCita(idcita,agente);
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/completado")
    public ResponseEntity<Cita> listarCitasPorEstadoCompletado(@RequestParam String estado, @RequestParam Integer idcita) {
        Cita cita = citaService.abrirCitaCompletadaPorId(idcita, estado);
        return ResponseEntity.ok(cita);
    }

    @PutMapping("/cita-finalizada/{idcita}")
    public ResponseEntity<Cita> citafinalizar(@PathVariable("idcita") Integer idcita, @RequestBody Cita cita){
        Cita citafinal = citaService.finalizarCita(cita);
        return ResponseEntity.ok(citafinal);
    }



}
