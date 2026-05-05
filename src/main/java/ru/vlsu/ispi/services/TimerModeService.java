package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.MusicMedia;
import ru.vlsu.ispi.beans.TimerMode;
import ru.vlsu.ispi.repositories.MusicMediaRepository;
import ru.vlsu.ispi.repositories.TimerModeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TimerModeService {
    private final TimerModeRepository timerModeRepository;

    @Autowired
    public TimerModeService(TimerModeRepository timerModeRepository) {
        this.timerModeRepository = timerModeRepository;
    }

//    public TimerMode createTimerMode(String name, String url) {
//        TimerMode timerMode = new TimerMode(name, url);
//        return timerModeRepository.save(timerMode);
//    }

    public Optional<TimerMode> getTimerModeById(int id) {
        return timerModeRepository.findById(id);
    }

//    public TimerMode updateTimerMode(int id, String name, String url) {
//        TimerMode musicMedia = timerModeRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//        musicMedia.setName(name);
//        musicMedia.setUrl(url);
//        return timerModeRepository.save(timerMode);
//    }

    public void deleteTimerMode(int id) {
        timerModeRepository.deleteById(id);
    }


    public List<TimerMode> getAllTimerModes() {
        return timerModeRepository.findAll();
    }
}
