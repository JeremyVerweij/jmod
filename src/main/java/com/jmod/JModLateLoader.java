package com.jmod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("JMod Core")
public class JModLateLoader implements IFMLLoadingPlugin, ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        // This MUST match the name of your json file in resources
        return Collections.singletonList("jmod.default.mixin.json");
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
