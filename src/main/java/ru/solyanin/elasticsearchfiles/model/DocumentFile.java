package ru.solyanin.elasticsearchfiles.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "document")
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.StandardRandomStrategy"
                    )
            }
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @Column(nullable = false)
    private String downloadUrl;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String data;
}