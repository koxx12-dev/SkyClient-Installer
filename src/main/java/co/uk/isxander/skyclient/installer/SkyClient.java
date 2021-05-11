package co.uk.isxander.skyclient.installer;

import co.uk.isxander.skyclient.installer.gui.MainGui;
import co.uk.isxander.skyclient.installer.repo.RepositoryManager;
import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;
import co.uk.isxander.skyclient.installer.utils.OSChecker;
import co.uk.isxander.skyclient.installer.utils.UpdateHook;

import java.io.File;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SkyClient {

    public static final Logger LOGGER = LogManager.getLogManager().getLogger("SkyClient");

    private static SkyClient instance;

    private final File mcDir;
    private final File scDir;
    private final RepositoryManager repositoryManager;
    private final MainGui mainGui;

    public SkyClient() {
        OSChecker.OSType type = OSChecker.getOperatingSystemType();
        if (type == OSChecker.OSType.Windows) {
            mcDir = new File(new File(System.getProperty("APPDATA")), ".minecraft");
        } else if (type == OSChecker.OSType.Linux) {
            mcDir = new File("~/.minecraft");
        } else if (type == OSChecker.OSType.MacOS) {
            mcDir = new File("~/Library/Application Support/Minecraft");
        } else {
            throw new IllegalStateException("Could not detect OS type");
        }
        scDir = new File(mcDir, "skyclient");

        this.mainGui = null;
        this.repositoryManager = new RepositoryManager();
        this.repositoryManager.fetchFiles(new UpdateHook() {
            @Override
            public void updateMod(ModEntry mod) {
                if (mainGui != null) {

                }
            }

            @Override
            public void updatePack(PackEntry pack) {

            }
        });
    }

    public MainGui getMainGui() {
        return mainGui;
    }

    public File getMcDir() {
        return mcDir;
    }

    public File getScDir() {
        return scDir;
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
