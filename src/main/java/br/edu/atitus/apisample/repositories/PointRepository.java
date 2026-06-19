package br.edu.atitus.apisample.repositories;

import br.edu.atitus.apisample.entities.PointEntity;
import br.edu.atitus.apisample.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PointRepository extends JpaRepository<PointEntity, UUID> {

    // SELECT * FROM tb_point WHERE id_user = ?
    List<PointEntity> findByUser(User user);

}
