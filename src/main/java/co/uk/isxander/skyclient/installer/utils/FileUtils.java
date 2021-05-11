package co.uk.isxander.skyclient.installer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;

public class FileUtils {

    // https://stackoverflow.com/a/13379744/15301449
    public static void exportResource(String resourceName, File output, CopyOption... options) {
        try (InputStream is = FileUtils.class.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("Cannot get resource \"" + resourceName + "\"");
            }

            Files.copy(is, output.toPath(), options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
