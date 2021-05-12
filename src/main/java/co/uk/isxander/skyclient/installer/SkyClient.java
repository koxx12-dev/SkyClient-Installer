package co.uk.isxander.skyclient.installer;

import co.uk.isxander.skyclient.installer.gui.MainGui;
import co.uk.isxander.skyclient.installer.repo.RepositoryManager;
import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;
import co.uk.isxander.skyclient.installer.utils.OSChecker;
import co.uk.isxander.skyclient.installer.utils.UpdateHook;

import javax.swing.*;
import java.io.File;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SkyClient {

    public static final Logger LOGGER = LogManager.getLogManager().getLogger("SkyClient");

    private static SkyClient instance;

    private File mcDir;
    private String scPath;
    private final RepositoryManager repositoryManager;
    private MainGui mainGui;

    public SkyClient() {
        OSChecker.OSType type = OSChecker.getOperatingSystemType();
        if (type == OSChecker.OSType.Windows) {
            mcDir = new File(new File(System.getenv("APPDATA")), ".minecraft");
        } else if (type == OSChecker.OSType.Linux) {
            mcDir = new File("~/.minecraft");
        } else if (type == OSChecker.OSType.MacOS) {
            mcDir = new File("~/Library/Application Support/Minecraft");
        } else {
            LOGGER.severe("OS type is not supported. Cannot continue.");
            JOptionPane.showMessageDialog(null, "Your OS type is not supported by SkyClient (Java Edition).", "Fatal Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("OS type is not supported.");
        }
        scPath = "skyclient";

        this.repositoryManager = new RepositoryManager();
        mainGui = new MainGui(this);
    }

    public void install() {

    }

    public void setMcDir(File newMc) {
        this.mcDir = newMc;
    }

    public File getScDir() {
        return new File(mcDir, scPath);
    }

    public MainGui getMainGui() {
        return mainGui;
    }

    public File getMcDir() {
        return mcDir;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public static SkyClient getInstance() {
        if (instance == null)
            instance = new SkyClient();

        return instance;
    }

}
