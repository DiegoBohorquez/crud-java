package med.voll.api.domain.consulta.validaciones.reserva;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacionMedicoActivo implements ValidadorDeConsultas {

    @Autowired
    private MedicoRepository medicoRepository;
    public void validar(DatosReservaConsulta datos){
        // Eleccion del medico ocional

        if(datos.idMedico() == null) {
            return;
        }
        var medicoEstaActivo = medicoRepository.findActivoById(datos.idMedico());
        if(!medicoEstaActivo) {
            throw new ValidacionException("El medico no se encuentra activo");
        }
    }
}
