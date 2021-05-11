package co.uk.isxander.skyclient.installer.repo.entry;

import java.awt.image.BufferedImage;

public class ModEntry {

    private final String id;
    private boolean enabled;
    private final String fileName;
    private final String downloadUrl;
    private final String displayName;
    private final String description;
    private final String iconFile;
    private BufferedImage iconImage;
    private final String creator;
    private final String[] packRequirements;
    private final String[] modRequirements;
    private final String[] files;

    public ModEntry(String id, boolean enabled, String fileName, String downloadUrl, String displayName, String description, String iconFile, BufferedImage iconImage, String creator, String[] packRequirements, String[] modRequirements, String[] files) {
        this.id = id;
        this.enabled = enabled;
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.displayName = displayName;
        this.description = description;
        this.iconFile = iconFile;
        this.iconImage = iconImage;
        this.creator = creator;
        this.packRequirements = packRequirements;
        this.modRequirements = modRequirements;
        this.files = files;
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

    public String[] getPackRequirements() {
        return packRequirements;
    }

    public String[] getModRequirements() {
        return modRequirements;
    }

    public String[] getFiles() {
        return files;
    }
}
