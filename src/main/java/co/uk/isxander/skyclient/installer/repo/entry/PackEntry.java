package co.uk.isxander.skyclient.installer.repo.entry;

import java.awt.image.BufferedImage;

public class PackEntry {

    private final String id;
    private boolean enabled;
    private final String fileName;
    private final String downloadUrl;
    private final String displayName;
    private final String description;
    private final String iconFile;
    private BufferedImage iconImage;
    private final String creator;

    public PackEntry(String id, boolean enabled, String fileName, String downloadUrl, String displayName, String description, String iconFile, BufferedImage iconImage, String creator) {
        this.id = id;
        this.enabled = enabled;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.displayName = displayName;
        this.description = description;
        this.iconFile = iconFile;
        this.iconImage = iconImage;
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getIconFile() {
        return iconFile;
    }

    public BufferedImage getIconImage() {
        return iconImage;
    }

    public void setIconImage(BufferedImage img) {
        this.iconImage = img;
    }

    public String getCreator() {
        return creator;
    }
}
