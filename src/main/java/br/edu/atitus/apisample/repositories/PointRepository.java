package br.edu.atitus.apisample.repositories;

import br.edu.atitus.apisample.entities.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointRepository extends JpaRepository<PointEntity, UUID> {
}
