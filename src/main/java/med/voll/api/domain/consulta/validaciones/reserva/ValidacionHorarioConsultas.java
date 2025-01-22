package med.voll.api.domain.consulta.validaciones.reserva;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;


@Component
public class ValidacionHorarioConsultas implements ValidadorDeConsultas {
    public void validar(DatosReservaConsulta datos){
        var fechaConsulta = datos.fecha();
        var domingo = fechaConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var horarioAntesCierre = fechaConsulta.getHour() < 7;
        var horarioDespuesCierre = fechaConsulta.getHour() > 18;
        if(domingo || horarioAntesCierre || horarioDespuesCierre){
            throw new ValidacionException("Horario no disponible, clinica sin servicio en ese rango");
        }
    }
}
