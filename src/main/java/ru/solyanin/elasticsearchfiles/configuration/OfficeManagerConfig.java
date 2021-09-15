package ru.solyanin.elasticsearchfiles.configuration;

import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OfficeManagerConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${converter.startport}")
    int startPort;

    @Value("${converter.portscount}")
    int portsCount;

    @Value("${libreoffice.path}")
    String officeHome;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        int[] portNumbers = new int[portsCount];
        for (int i = 0; i < portsCount; i++) {
            portNumbers[i] = startPort + i;
        }
        OfficeManager officeManager = LocalOfficeManager.builder()
                .officeHome(officeHome)
                .install()
                .disableOpengl(true)
                .portNumbers(portNumbers)
                .build();
        try {
            officeManager.start();
        } catch (OfficeException e) {
            e.printStackTrace();
        }
    }
}