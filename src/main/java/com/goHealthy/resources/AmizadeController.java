package com.goHealthy.resources;

import com.goHealthy.domain.Aspirante;
import com.goHealthy.domain.Evento;
import com.goHealthy.services.AmizadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/amizades")
public class AmizadeController {

    @Autowired
    private AmizadeService amizadeService;


    @GetMapping("/{id}")
    public ResponseEntity<List<Aspirante>> getAmigos(@PathVariable Integer id){
        Optional<List<Aspirante>> amizades=amizadeService.getAmigos(id);
        if (amizades.isPresent()){
            return ResponseEntity.ok(amizades.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/pendentes")
    public ResponseEntity<List<Aspirante>> getPedidosAmizade(@PathVariable Integer id) {
        return ResponseEntity.ok(amizadeService.getPedidosAmizade(id));
    }

    @PostMapping("/{id}/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAmigo (@PathVariable Integer id,@RequestParam Integer asp_id){
        Optional<Aspirante> aspirante=amizadeService.findById(id);
        Optional<Aspirante> aspiranteAmigo=amizadeService.findById(asp_id);
        List<Aspirante> listaAmigos = aspirante.get().getAmigos();
        if(aspirante.isPresent() && aspiranteAmigo.isPresent() && !(listaAmigos.contains(aspiranteAmigo))){
            aspirante.get().addAmigo(aspiranteAmigo.get());
            amizadeService.postAspirante(aspirante.get());
            amizadeService.postAspirante(aspiranteAmigo.get());
        }
    }

    //essa é a logica inversa de /{id}/add, precisa verificar se o aspiranteAmigo contém o pedido de amizade.
    @PostMapping("/{id}/accept")
    @ResponseStatus(HttpStatus.CREATED)
    public void aceitarPedido (@PathVariable Integer id,@RequestParam Integer request){
        Optional<Aspirante> aspirante=amizadeService.findById(id);
        Optional<Aspirante> aspiranteAmigo=amizadeService.findById(request);
        if(aspirante.isPresent() && aspiranteAmigo.isPresent() && amizadeService.checarPedido(aspiranteAmigo.get(),aspirante.get())){
            aspiranteAmigo.get().aceitarAmizade(aspirante.get());
            amizadeService.postAspirante(aspirante.get());
            amizadeService.postAspirante(aspiranteAmigo.get());
        }
    }



    @DeleteMapping("/{id}/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,@RequestParam Integer asp_id){
        Optional<Aspirante> aspirante=amizadeService.findById(id);
        Optional<Aspirante> aspiranteAmigo=amizadeService.findById(asp_id);
        List<Aspirante> listaAmigos = aspirante.get().getAmigos();
        if(aspirante.isPresent() && aspiranteAmigo.isPresent() && (!(listaAmigos.contains(aspiranteAmigo)))) {
            aspirante.get().delAmigo(aspiranteAmigo.get());
            amizadeService.postAspirante(aspirante.get());
            amizadeService.postAspirante(aspiranteAmigo.get());
        }
    }
}




