package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.validaciones.cancelamiento.ValidadorCancelamientoDeConsulta;
import med.voll.api.domain.consulta.validaciones.reserva.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaDeConsultas {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private List<ValidadorDeConsultas> validadores;

    @Autowired
    private List<ValidadorCancelamientoDeConsulta> validadoresCancelamiento;

    public DatosDetalleConsulta reservaConsulta(DatosReservaConsulta datosReservaConsulta) {

        if(!pacienteRepository.existsById(datosReservaConsulta.idPaciente())) {
            throw new ValidacionException("Paciente no encontrado");
        }
        if(datosReservaConsulta.idMedico() != null && !medicoRepository.existsById(datosReservaConsulta.idMedico())) {
            throw new ValidacionException("Medico no encontrado");
        }

        // Validaciones
        validadores.forEach(v -> v.validar(datosReservaConsulta));

        var medico = elegirMedico(datosReservaConsulta);
        if(medico == null) {
            throw new ValidacionException("No existe un medico disponible en el horario seleccionado");
        }

        var paciente = pacienteRepository.findById(datosReservaConsulta.idPaciente()).get();
        var consulta = new Consulta(null, medico, paciente, datosReservaConsulta.fecha(), null);

        consultaRepository.save(consulta);
        return new DatosDetalleConsulta(consulta);
    }

    private Medico elegirMedico(DatosReservaConsulta datosReservaConsulta) {
        if(datosReservaConsulta.idMedico() != null) {
            return medicoRepository.getReferenceById(datosReservaConsulta.idMedico());
        }
        if(datosReservaConsulta.especialidad() == null) {
            throw new ValidacionException("Medico no encontrado, debes elegir una especialidad");
        }

      return medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(datosReservaConsulta.especialidad(), datosReservaConsulta.fecha());


    }

    public void cancelar(DatosCancelamientoConsulta datos) {
        if (!consultaRepository.existsById(datos.idConsulta())) {
            throw new ValidacionException("Id de la consulta informado no existe!");
        }

        validadoresCancelamiento.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivo());
    }
}
