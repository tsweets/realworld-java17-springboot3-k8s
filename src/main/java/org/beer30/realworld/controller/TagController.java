package org.beer30.realworld.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.beer30.realworld.domain.TagDTO;
import org.beer30.realworld.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tags")
@Tag(name = "comments api", description = "API for managing Tags")
public class TagController {

    @Autowired
    TagRepository tagRepository;


    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "Get all of the Tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag List")
    })
    public TagDTO listTags() {
        log.info("REST (get): /api/tags");

        List<org.beer30.realworld.model.Tag> tagList = tagRepository.findAll();
        TagDTO dto = TagDTO.builder().build();
        List<String> tagArray = new ArrayList<>();

        for (org.beer30.realworld.model.Tag tag : tagList) {
            tagArray.add(tag.getTag());
        }

        dto.setTags(tagArray.toArray(new String[0]));
        return dto;

    }
}



