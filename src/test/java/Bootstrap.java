import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFMLSidedHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/// Adapted and minimized from [GregTechCEu](https://github.com/GregTechCEu/GregTech/blob/master/src/test/java/gregtech/Bootstrap.java)
public final class Bootstrap {

    private static final MethodHandle CORE_MOD_MANAGER$DEOBFUSCATED_ENVIRONMENT_SETTER;
    private static final MethodHandle I18N$SET_LOCALE;
    private static final MethodHandle FML_COMMON_HANDLER$SIDED_DELEGATE_SETTER;

    private static boolean bootstrapped = false;

    static {
        try {
            var lookup = MethodHandles.lookup();

            CORE_MOD_MANAGER$DEOBFUSCATED_ENVIRONMENT_SETTER = MethodHandles.privateLookupIn(CoreModManager.class, lookup)
                    .findStaticSetter(CoreModManager.class, "deobfuscatedEnvironment", boolean.class);

            I18N$SET_LOCALE = MethodHandles.privateLookupIn(I18n.class, lookup)
                    .findStatic(I18n.class, "setLocale", MethodType.methodType(void.class, Locale.class));

            FML_COMMON_HANDLER$SIDED_DELEGATE_SETTER = MethodHandles.privateLookupIn(FMLCommonHandler.class, lookup)
                    .findSetter(FMLCommonHandler.class, "sidedDelegate", IFMLSidedHandler.class);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException("Test bootstrap failed!", e);
        }
    }

    public static void perform() {
        if (bootstrapped) return;

        try {
            CORE_MOD_MANAGER$DEOBFUSCATED_ENVIRONMENT_SETTER.invokeExact(true);
            I18N$SET_LOCALE.invokeExact(new Locale());
            FML_COMMON_HANDLER$SIDED_DELEGATE_SETTER.invokeExact(FMLCommonHandler.instance(), (IFMLSidedHandler) new TestSidedHandler());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        net.minecraft.init.Bootstrap.register();
        bootstrapped = true;
    }

    private static final class TestSidedHandler implements IFMLSidedHandler {

        @Override
        public List<String> getAdditionalBrandingInformation() {
            return Collections.emptyList();
        }

        @Override
        public Side getSide() {
            return Side.SERVER;
        }

        @Override
        public void haltGame(String message, Throwable exception) {
            throw new RuntimeException(message, exception);
        }

        @Override
        public void showGuiScreen(Object clientGuiElement) {
        }

        @Override
        public void queryUser(StartupQuery query) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void beginServerLoading(MinecraftServer server) {
        }

        @Override
        public void finishServerLoading() {
        }

        @Override
        public File getSavesDirectory() {
            throw new UnsupportedOperationException();
        }

        @Override
        public MinecraftServer getServer() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDisplayCloseRequested() {
            return false;
        }

        @Override
        public boolean shouldServerShouldBeKilledQuietly() {
            return false;
        }

        @Override
        public void addModAsResource(ModContainer container) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getCurrentLanguage() {
            return "en_US";
        }

        @Override
        public void serverStopped() {
        }

        @Override
        public NetworkManager getClientToServerNetworkManager() {
            throw new UnsupportedOperationException();
        }

        @Override
        public INetHandler getClientPlayHandler() {
            return null;
        }

        @Override
        public void fireNetRegistrationEvent(EventBus bus, NetworkManager manager, Set<String> channelSet,
                                             String channel, Side side) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean shouldAllowPlayerLogins() {
            return false;
        }

        @Override
        public void allowLogins() {
        }

        @Override
        public IThreadListener getWorldThread(INetHandler net) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void processWindowMessages() {
        }

        @Override
        public String stripSpecialChars(String message) {
            return message;
        }

        @Override
        public void reloadRenderers() {
        }

        @Override
        public void fireSidedRegistryEvents() {
        }

        @Override
        public CompoundDataFixer getDataFixer() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isDisplayVSyncForced() {
            return false;
        }
    }
}
