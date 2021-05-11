package co.uk.isxander.skyclient.installer;

import co.uk.isxander.skyclient.installer.repo.RepositoryManager;

import java.util.logging.LogManager;
import java.util.logging.Logger;

public class SkyClient {

    public static final Logger LOGGER = LogManager.getLogManager().getLogger("SkyClient");

    private static SkyClient instance;

    private RepositoryManager repositoryManager;

    public SkyClient() {
        this.repositoryManager = new RepositoryManager();

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
