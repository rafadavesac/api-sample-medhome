package br.edu.atitus.apisample.services;

import br.edu.atitus.apisample.entities.PointEntity;
import br.edu.atitus.apisample.entities.User;
import br.edu.atitus.apisample.repositories.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service //Bean do tipo service
public class PointService {

    private final PointRepository repository;

    public PointService(PointRepository repository) { //injeção de dependência no construtor
        this.repository = repository;
    }

    @Transactional
    public PointEntity save(PointEntity point) throws Exception {
        if (point == null)
            throw new Exception("Objeto nulo");

        if (point.getPatientName() == null || point.getPatientName().isBlank())
            throw new Exception("Nome do paciente inválido");

        if (point.getServiceType() == null || point.getServiceType().isBlank())
            throw new Exception("Tipo de atendimento inválido");

        if (point.getAppointmentDate() == null)
            throw new Exception("Data do atendimento inválida");

        if (point.getAppointmentTime() == null)
            throw new Exception("Horário do atendimento inválido");

        if (point.getLatitude() < -90 || point.getLatitude() > 90)
            throw new Exception("Latitude inválida");

        if (point.getLongitude() < -180 || point.getLongitude() > 180)
            throw new Exception("Longitude inválida");

        //É como uma memória da requisição atual.
        // Quando o token JWT chega, o filtro de segurança (no AuthTokenFilter) lê o token,
        // identifica o usuário e coloca esse usuário no SecurityContextHolder.
        // A partir daí, qualquer ponto do código pode perguntar "quem está logado agora?" — e é exatamente isso que essa linha faz.
        // O getPrincipal() retorna um Object, por isso o cast (User) — estamos dizendo ao Java: "pode tratar isso como User".
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        point.setUser(userAuth); //define esse usuário como dono do atendimento

        return repository.save(point);
    }

    @Transactional
    public void deleteById(UUID id) throws Exception {
        var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Atendimento não localizado"));
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!pointInBD.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Você não tem permissão para essa ação");

        repository.deleteById(id);
    }

    @Transactional
    public PointEntity update(UUID id, PointEntity point) throws Exception {
        // busca o atendimento — se não existe, lança exceção
        var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Atendimento não localizado"));

        //É como uma memória da requisição atual.
        // Quando o token JWT chega, o filtro de segurança (no AuthTokenFilter) lê o token,
        // identifica o usuário e coloca esse usuário no SecurityContextHolder.
        // A partir daí, qualquer ponto do código pode perguntar "quem está logado agora?" — e é exatamente isso que essa linha faz.
        // O getPrincipal() retorna um Object, por isso o cast (User) — estamos dizendo ao Java: "pode tratar isso como User".
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // verifica se o atendimento pertence ao usuário logado — se não, lança exceção
        if (!pointInBD.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Você não tem permissão para essa ação");

        // atualiza os campos com os novos valores recebidos
        pointInBD.setPatientName(point.getPatientName());
        pointInBD.setServiceType(point.getServiceType());
        pointInBD.setAppointmentDate(point.getAppointmentDate());
        pointInBD.setAppointmentTime(point.getAppointmentTime());
        pointInBD.setLatitude(point.getLatitude());
        pointInBD.setLongitude(point.getLongitude());

        return repository.save(pointInBD);
    }

    public List<PointEntity> findAll() {
        // retorna apenas os atendimentos do profissional logado
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findByUser(userAuth);
    }
}