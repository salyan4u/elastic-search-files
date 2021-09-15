package ru.solyanin.elasticsearchfiles.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file")
public class TempFilesProperties {
    private final String tempPath;
    private final String pdfPath;

    public TempFilesProperties(@Value("${file.temp-path}") String tempPath,
                               @Value("${file.pdf-path}") String pdfPath) {
        this.tempPath = tempPath;
        this.pdfPath = pdfPath;
    }
}
