package co.uk.isxander.skyclient.installer.repo;

import co.uk.isxander.skyclient.installer.SkyClient;
import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;
import co.uk.isxander.xanderlib.utils.HttpsUtils;
import co.uk.isxander.xanderlib.utils.Multithreading;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RepositoryManager {

    public static final String MODS_JSON_URL = "https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mods.json";
    public static final String PACKS_JSON_URL = "https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/packs.json";
    public static final String ICONS_DIR_URL = "https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/icons/";
    public static final String MC_DIR_URL = "https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mcdir/";

    public static final File CACHE_FOLDER = new File(new File(System.getProperty("user.home")), ".skyclient/");
    public static final long CACHE_TIME = TimeUnit.DAYS.toMillis(1);

    private JsonArray mods;
    private JsonArray packs;

    private List<ModEntry> modEntries;
    private List<PackEntry> packEntries;

    public RepositoryManager() {
        this.modEntries = new ArrayList<>();
        this.packEntries = new ArrayList<>();
    }

    public void fetchFiles(Runnable onIconUpdate) {
        Multithreading.runAsync(() -> {
            boolean refresh = shouldRefreshCache();
            if (refresh) {
                if (!CACHE_FOLDER.exists()) {
                    CACHE_FOLDER.mkdirs();
                }
            }

            mods = JsonParser.parseString(HttpsUtils.getString(MODS_JSON_URL)).getAsJsonArray();
            packs = JsonParser.parseString(HttpsUtils.getString(PACKS_JSON_URL)).getAsJsonArray();

            BufferedImage unknownImg = null;
            String iconFileName = "unknown.png";
            try {
                File iconFile = new File(CACHE_FOLDER, "icons/" + iconFileName);
                if (!iconFile.exists() || refresh) {
                    HttpsUtils.downloadFile(ICONS_DIR_URL + iconFileName, iconFile);
                }
                unknownImg = ImageIO.read(iconFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (JsonElement element : mods) {
                if (!element.isJsonObject()) {
                    SkyClient.LOGGER.warning("Mods JSON included non-json-object.");
                    continue;
                }
                BetterJsonObject modJson = new BetterJsonObject(element.getAsJsonObject());

                int i = 0;
                String[] packs = new String[0];
                if (modJson.has("packs")) {
                    JsonArray packArray = modJson.get("packs").getAsJsonArray();
                    packs = new String[packArray.size()];
                    for (JsonElement packIdElement : packArray) {
                        packs[i] = packIdElement.getAsString();
                        i++;
                    }
                }

                String[] mods = new String[0];
                if (modJson.has("mods")) {
                    JsonArray modArray = modJson.get("mods").getAsJsonArray();
                    mods = new String[modArray.size()];
                    i = 0;
                    for (JsonElement modIdElement : modArray) {
                        mods[i] = modIdElement.getAsString();
                        i++;
                    }
                }

                String[] files = new String[0];
                if (modJson.has("files")) {
                    JsonArray fileArray = modJson.get("files").getAsJsonArray();
                    files = new String[fileArray.size()];
                    i = 0;
                    for (JsonElement fileIdElement : fileArray) {
                        files[i] = fileIdElement.getAsString();
                        i++;
                    }
                }


                modEntries.add(new ModEntry(
                        modJson.optString("id"),
                        modJson.optBoolean("enabled", false),
                        modJson.optString("file"),
                        modJson.optString("url"),
                        modJson.optString("display"),
                        modJson.optString("description"),
                        iconFileName,
                        unknownImg,
                        modJson.optString("creator", "Unknown"),
                        packs,
                        mods,
                        files
                ));
            }

            for (JsonElement element : packs) {
                if (!element.isJsonObject()) {
                    SkyClient.LOGGER.warning("Packs JSON included non-json-object.");
                    continue;
                }
                BetterJsonObject packJson = new BetterJsonObject(element.getAsJsonObject());

                packEntries.add(new PackEntry(
                        packJson.optString("id"),
                        packJson.optBoolean("enabled", false),
                        packJson.optString("file"),
                        packJson.optString("url"),
                        packJson.optString("display"),
                        packJson.optString("description"),
                        iconFileName,
                        unknownImg,
                        packJson.optString("creator", "Unknown")
                ));
            }
        });

    }

    public boolean shouldRefreshCache() {
        return !CACHE_FOLDER.exists() || CACHE_FOLDER.lastModified() < System.currentTimeMillis() - CACHE_TIME;
    }

}
