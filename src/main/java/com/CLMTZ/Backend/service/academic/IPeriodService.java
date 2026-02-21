package com.CLMTZ.Backend.service.academic;

import java.util.List;

import com.CLMTZ.Backend.dto.academic.PeriodDTO;

public interface IPeriodService {
    List<PeriodDTO> findAll();
    PeriodDTO findById(Integer id);
    PeriodDTO save(PeriodDTO dto);
    PeriodDTO update(Integer id, PeriodDTO dto);
    void deleteById(Integer id);
    List<String> Period(List<PeriodDTO> dtos);
}
