package co.uk.isxander.skyclient.installer.utils;

import co.uk.isxander.skyclient.installer.repo.entry.ModEntry;
import co.uk.isxander.skyclient.installer.repo.entry.PackEntry;

public interface UpdateHook {

    void updateMod(ModEntry mod);

    void updatePack(PackEntry pack);

}
