package ru.vlsu.ispi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vlsu.ispi.beans.VisualMedia;
import ru.vlsu.ispi.repositories.VisualMediaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VisualMediaService {
    private final VisualMediaRepository visualMediaRepository;

    @Autowired
    public VisualMediaService(VisualMediaRepository visualMediaRepository) {
        this.visualMediaRepository = visualMediaRepository;
    }

    public VisualMedia createVisualMedia(String name, String url) {
        VisualMedia visualMedia = new VisualMedia(name, url);
        return visualMediaRepository.save(visualMedia);
    }

    public Optional<VisualMedia> getVisualMediaById(int id) {
        return visualMediaRepository.findById(id);
    }

    public VisualMedia updateVisualMedia(int id, String name, String url) {
        VisualMedia visualMedia = visualMediaRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        visualMedia.setName(name);
        visualMedia.setUrl(url);
        return visualMediaRepository.save(visualMedia);
    }

    public void deleteVisualMedia(int id) {
        visualMediaRepository.deleteById(id);
    }


    public List<VisualMedia> getAllVisualMedia() {
        return visualMediaRepository.findAll();
    }
}
