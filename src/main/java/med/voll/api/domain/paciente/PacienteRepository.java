package med.voll.api.domain.paciente;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p.activo FROM Paciente p where p.id = :idPaciente")
    boolean findActivoById(Long idPaciente);
}
