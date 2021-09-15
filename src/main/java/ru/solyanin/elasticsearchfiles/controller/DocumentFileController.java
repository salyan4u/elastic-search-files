package ru.solyanin.elasticsearchfiles.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.OfficeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.solyanin.elasticsearchfiles.model.DocumentFile;
import ru.solyanin.elasticsearchfiles.service.DocumentFileService;
import ru.solyanin.elasticsearchfiles.view.SearchResponseDocument;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("api/v1/elastic-search-files")
@Tag(name = "ElasticSearchService", description = "Elastic search files service")
@RestController
public class DocumentFileController {
    private final DocumentFileService documentFileService;

    public DocumentFileController(DocumentFileService documentFileService) {
        this.documentFileService = documentFileService;
    }

    @Operation(summary = "Search hits in documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = "application/json", value = "/search")
    @ResponseBody
    public ResponseEntity<Iterable<SearchResponseDocument>> getList(@RequestParam String search,
                                                                    @RequestParam(required = false, defaultValue = "0") int number,
                                                                    @RequestParam(required = false, defaultValue = "10") int size) {
        try {
            return ResponseEntity.ok(documentFileService.findByData(search, number, size));
        } catch (IOException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Page of documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(produces = "application/json", value = "/page")
    @ResponseBody
    public ResponseEntity<Iterable<DocumentFile>> getPage(@RequestParam(required = false, defaultValue = "0") int number,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(documentFileService.getPage(number, size));
    }

    @Operation(summary = "Add new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<DocumentFile> create(@Parameter(description = "URL of document") @RequestParam String downloadUrl) {
        try {
            return ResponseEntity.ok(documentFileService.create(downloadUrl));
        } catch (EntityExistsException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (OfficeException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Add list of new documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(produces = "application/json", value = "/list")
    @ResponseBody
    public ResponseEntity<Iterable<DocumentFile>> createList(@RequestParam List<String> downloadUrls) {
        try {
            return ResponseEntity.ok(documentFileService.createList(downloadUrls));
        } catch (EntityExistsException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (IOException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (OfficeException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> delete(@Parameter(description = "UUID of document") @RequestParam String id) {
        try {
            documentFileService.delete(id);
            return ResponseEntity.ok("Document deleted!");
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}