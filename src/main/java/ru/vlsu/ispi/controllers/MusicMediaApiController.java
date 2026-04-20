package ru.vlsu.ispi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.vlsu.ispi.beans.MusicMedia;
import ru.vlsu.ispi.services.MusicMediaService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/music")
public class MusicMediaApiController {

    @Autowired
    private MusicMediaService musicMediaService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getMusicMedia(@PathVariable Integer id) {
        MusicMedia musicMedia = musicMediaService.getMusicMediaById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, String> response = new HashMap<>();
        response.put("name", musicMedia.getName());
        response.put("url", musicMedia.getUrl());

        return ResponseEntity.ok(response);
    }
}
