package ru.solyanin.elasticsearchfiles.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ru.solyanin.elasticsearchfiles.model.DocumentFile;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentFileRepository extends ElasticsearchRepository<DocumentFile, String> {
    List<DocumentFile> findByData(String data);

    Optional<DocumentFile> findById(String id);

    Optional<DocumentFile> findByDownloadUrl(String downloadUrl);
}