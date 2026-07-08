package com.jmod.jrender.client.debug;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Keyboard;

public class ExtendedDebug {
    //A B D F G H N P Q T I C
//    public static final F3Debugger CHUNK_WIRE_FRAME_MODE = new F3Debugger(Keyboard.KEY_I, "debug.chunk_wireframe");
//    public static final F3Debugger DISABLE_CHUNK_FRUSTUM_CAMERA_MODE = new F3Debugger(Keyboard.KEY_C, "debug.disable_chunk_frustum_camere");

    private static final ExtendedDebug instance = new ExtendedDebug();
    public static ExtendedDebug getInstance() {
        return instance;
    }

    private final Int2ObjectMap<F3Debugger> debuggers;

    private ExtendedDebug(){
        this.debuggers = new Int2ObjectOpenHashMap<>();

//        addDebugger(CHUNK_WIRE_FRAME_MODE);
//        addDebugger(DISABLE_CHUNK_FRUSTUM_CAMERA_MODE);
    }

    public void addDebugger(F3Debugger debugger){
        this.debuggers.put(debugger.getKey(), debugger);
    }

    public boolean processF3Key(int auxKey){
        if (this.debuggers.containsKey(auxKey)){
            this.debuggers.get(auxKey).toggle();

            return true;
        }

        return false;
    }

    public void addHelpInfo(GuiNewChat chat){
        for (F3Debugger debugger : this.debuggers.values()) {
            chat.printChatMessage(new TextComponentTranslation(debugger.getTranslationKey()));
        }
    }

    public static class F3Debugger{
        private final int key;
        private final String translationKey;
        private boolean enabled;

        public F3Debugger(int key, String translationKey){
            this.key = key;
            this.translationKey = translationKey;
            this.enabled = false;
        }

        public int getKey() {
            return key;
        }

        public String getTranslationKey() {
            return translationKey;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void toggle(){
            this.enabled = !this.enabled;
        }
    }
}
