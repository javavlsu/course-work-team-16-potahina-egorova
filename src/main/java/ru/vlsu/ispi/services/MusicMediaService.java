package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.MusicMedia;
import ru.vlsu.ispi.beans.User;
import ru.vlsu.ispi.repositories.MusicMediaRepository;
import ru.vlsu.ispi.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MusicMediaService {
    private final MusicMediaRepository musicMediaRepository;

    @Autowired
    public MusicMediaService(MusicMediaRepository musicMediaRepository) {
        this.musicMediaRepository = musicMediaRepository;
    }

    public MusicMedia createMusicMedia(String name, String url) {
        MusicMedia musicMedia = new MusicMedia(name, url);
        return musicMediaRepository.save(musicMedia);
    }

    public Optional<MusicMedia> getMusicMediaById(int id) {
        return musicMediaRepository.findById(id);
    }

    public MusicMedia updateMusicMedia(int id, String name, String url) {
        MusicMedia musicMedia = musicMediaRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        musicMedia.setName(name);
        musicMedia.setUrl(url);
        return musicMediaRepository.save(musicMedia);
    }

    public void deleteMusicMedia(int id) {
        musicMediaRepository.deleteById(id);
    }


    public List<MusicMedia> getAllMusicMedia() {
        return musicMediaRepository.findAll();
    }
}
