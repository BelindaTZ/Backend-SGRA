package com.CLMTZ.Backend.dto.academic;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeriodDTO {
    private Integer periodId;
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean state;
    public String getNombrePeriodo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNombrePeriodo'");
    }
    public String getFechaInicio() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFechaInicio'");
    }
    public String getFechaFin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFechaFin'");
    }
}
