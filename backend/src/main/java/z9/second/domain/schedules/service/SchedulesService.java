package z9.second.domain.schedules.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import z9.second.model.classes.ClassesRepository;
import z9.second.model.schedules.SchedulesRepository;

@Service
@RequiredArgsConstructor
public class SchedulesService {
    private final SchedulesRepository schedulesRepository;
    private final ClassesRepository classesRepository;

//    public void create(SchedulesRequestDto schedulesRequestDto) {
//        SchedulesEntity schedules = SchedulesRequestDto.RequestData();
//    }

}
