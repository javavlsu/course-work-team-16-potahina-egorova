package ru.vlsu.ispi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.vlsu.ispi.beans.VisualMedia;
import ru.vlsu.ispi.services.VisualMediaService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
public class VisualMediaApiController {

    @Autowired
    private VisualMediaService visualMediaService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getVisualMedia(@PathVariable Integer id) {
        VisualMedia visualMedia = visualMediaService.getVisualMediaById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, String> response = new HashMap<>();
        response.put("name", visualMedia.getName());
        response.put("url", visualMedia.getUrl());

        return ResponseEntity.ok(response);
    }
}
