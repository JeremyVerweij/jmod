package com.jmod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("JMod Core")
public class JModEarlyLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        System.out.println("TEST JMOD");

        // This MUST match the name of your json file in resources
        return Collections.singletonList("jmod.default.mixins.json");
    }

    // Boilerplate for IFMLLoadingPlugin
    @Override
    public String[] getASMTransformerClass() { return new String[0]; }
    @Override
    public String getModContainerClass() { return null; }
    @Nullable
    @Override
    public String getSetupClass() { return null; }
    @Override
    public void injectData(Map<String, Object> data) { }
    @Override
    public String getAccessTransformerClass() { return null; }
}
