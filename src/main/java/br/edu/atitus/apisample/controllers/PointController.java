package br.edu.atitus.apisample.controllers;

import br.edu.atitus.apisample.dtos.PointDTO;
import br.edu.atitus.apisample.entities.PointEntity;
import br.edu.atitus.apisample.services.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ws/point")
public class PointController {

    private final PointService service;

    public PointController(PointService service) { //injeção de dependência no construtor
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PointEntity>> findAll(){
        var lista = service.findAll();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PointEntity> save(@RequestBody PointDTO dto) throws Exception{
        PointEntity point = new PointEntity();
        BeanUtils.copyProperties(dto, point); //copia os campos do DTO para a entidade automaticamente
        service.save(point);
        return ResponseEntity.status(201).body(point);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PointEntity> update(@PathVariable UUID id, @RequestBody PointDTO dto) throws Exception{
        PointEntity point = new PointEntity();
        BeanUtils.copyProperties(dto, point);
        var updated = service.update(id, point);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) throws Exception{
        service.deleteById(id);
        return ResponseEntity.ok("Atendimento deletado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        String message = ex.getMessage().replace("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}