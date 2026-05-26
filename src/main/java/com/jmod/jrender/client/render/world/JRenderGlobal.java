package com.jmod.jrender.client.render.world;

import com.jmod.jrender.client.render.opengl.ChunkBufferBuilder;
import com.jmod.jrender.client.render.opengl.shader.ShaderLoader;
import com.jmod.jrender.client.render.opengl.shader.ShaderProgram;
import com.jmod.jrender.client.render.opengl.vao.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.jspecify.annotations.Nullable;

import java.nio.FloatBuffer;

import static org.lwjglx.opengl.GL11.*;
import static org.lwjglx.opengl.GL13.GL_TEXTURE0;

public class JRenderGlobal extends RenderGlobal {
    private static final ResourceLocation chunkVertexShader = new ResourceLocation("jrender", "shaders/chunk.vert");
    private static final ResourceLocation chunkFragmentShader = new ResourceLocation("jrender", "shaders/chunk.frag");

    protected final FloatBuffer projectionBuffer;
    protected final FloatBuffer modelViewBuffer;

    protected final Matrix4f projectionMatrix;
    protected final Matrix4f modelViewMatrix;

    private final VertexArrayObject vao;
    private ShaderProgram shaderProgram;

    private boolean reloadTextureSizes = true;
    private float atlasWidth, atlasHeight;

    public JRenderGlobal(Minecraft mcIn) {
        super(mcIn);
        reloadTextureSizes();

        if(this.viewFrustum != null) this.viewFrustum.deleteGlResources();
        this.viewFrustum = null;
        this.renderChunkFactory = null;
        this.renderContainer = null;
        this.renderDispatcher = null;

        VertexArrayObject.loadMinecraftDefaults();

        this.projectionBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.modelViewBuffer = GLAllocation.createDirectFloatBuffer(16);
        this.projectionMatrix = new Matrix4f();
        this.modelViewMatrix = new Matrix4f();

        this.loadShader();

        this.vao = new VertexArrayObject(new AttributePointersBuilder()
                .addAttribute(VertexType.FLOAT, 3, false)
                .addAttribute(VertexType.UNSIGNED_BYTE, 4, true)
                .addAttribute(VertexType.SHORT, 2, false)
                .build());

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
        int u1 = sprite.getOriginX();
        int v1 = sprite.getOriginY();
        int u2 = u1 + sprite.getIconWidth();
        int v2 = v1 + sprite.getIconHeight();

        ChunkBufferBuilder builder = new ChunkBufferBuilder();
        builder.putPos(-5, 10, 5).putColor(0xFFFFFFFF).putUV(u1, v2).endVertex()//tl
                .putPos(5, 10, 5).putColor(0xFFFFFFFF).putUV(u2, v2).endVertex()//tr
                .putPos(5, 10, -5).putColor(0xFFFFFFFF).putUV(u2, v1).endVertex()//br
                .putPos(-5, 10, -5).putColor(0xFFFFFFFF).putUV(u1, v1).endVertex()//bl
                .putPos(-5, 0, 5).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                .putPos(5, 0, 5).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                .putPos(5, 10, 5).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                .putPos(-5, 10, 5).putColor(0xFFFF00FF).putUV(0, 0).endVertex()
                .putPos(-5, 10, -5).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                .putPos(5, 10, -5).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                .putPos(5, 0, -5).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                .putPos(-5, 0, -5).putColor(0xFFFFFF00).putUV(0, 0).endVertex()
                .putPos(-5, 0, -5).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                .putPos(-5, 0, 5).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                .putPos(-5, 10, 5).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                .putPos(-5, 10, -5).putColor(0xFF0000FF).putUV(0, 0).endVertex()
                .putPos(5, 10, -5).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                .putPos(5, 10, 5).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                .putPos(5, 0, 5).putColor(0xFF00FF00).putUV(0, 0).endVertex()
                .putPos(5, 0, -5).putColor(0xFF00FF00).putUV(0, 0).endVertex();

        this.vao.upload(builder);
    }

    protected void loadShader(){
        this.shaderProgram = ShaderLoader.loadShaderProgram(chunkVertexShader, chunkFragmentShader)
                .registerUniform("projectionMatrix")
                .registerUniform("modelViewMatrix")
                .registerUniform("textureAtlas")
                .registerUniform("atlasSize");
    }

    protected void updateProjectionMatrix(){
        this.projectionBuffer.rewind();
        this.modelViewBuffer.rewind();

        GlStateManager.getFloat(GL_PROJECTION_MATRIX, this.projectionBuffer);
        GlStateManager.getFloat(GL_MODELVIEW_MATRIX, this.modelViewBuffer);

        this.projectionMatrix.set(this.projectionBuffer);
        this.modelViewMatrix.set(this.modelViewBuffer);

        RenderManager rm = this.mc.getRenderManager();

        this.modelViewMatrix.translate(
                (float) -rm.viewerPosX,
                (float) -rm.viewerPosY,
                (float) -rm.viewerPosZ
        );
    }

    protected int getAtlasTextureId(ResourceLocation location) {
        ITextureObject atlasTexture = Minecraft.getMinecraft().getTextureManager().getTexture(location);

        if (atlasTexture != null) {
            return atlasTexture.getGlTextureId();
        }

        return -1;
    }

    protected void reloadTextureSizes(){
        int atlasId = getAtlasTextureId(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (atlasId > 0) {
            int previousTexture = glGetInteger(GL_TEXTURE_BINDING_2D);

            glBindTexture(GL_TEXTURE_2D, atlasId);
            this.atlasWidth = (float) glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
            this.atlasHeight = (float) glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);

            glBindTexture(GL_TEXTURE_2D, previousTexture);
        }

        this.reloadTextureSizes = false;
    }

    @Override
    protected void renderBlockLayer(BlockRenderLayer blockLayerIn) {
        if (this.shaderProgram == null) return;

        if (blockLayerIn == BlockRenderLayer.CUTOUT){
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();

            GlStateManager.setActiveTexture(GL_TEXTURE0);
            GlStateManager.bindTexture(getAtlasTextureId(TextureMap.LOCATION_BLOCKS_TEXTURE));

            this.updateProjectionMatrix();

            this.shaderProgram.bind();

            this.shaderProgram.uploadUniform("projectionMatrix", this.projectionMatrix);
            this.shaderProgram.uploadUniform("modelViewMatrix", this.modelViewMatrix);
            this.shaderProgram.uploadUniform("textureAtlas", 0);
            this.shaderProgram.uploadUniform("atlasSize", this.atlasWidth, this.atlasHeight);

            this.vao.draw(GL_QUADS);
            this.shaderProgram.unbind();
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);

        if(this.shaderProgram != null) this.shaderProgram.delete();
        this.loadShader();

        this.reloadTextureSizes = true;
    }

    @Override
    public void setWorldAndLoadRenderers(@Nullable WorldClient worldClientIn) {
        if (this.world != null) {
            this.world.removeEventListener(this);
        }

        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.setWorld(worldClientIn);
        this.world = worldClientIn;
        if (worldClientIn != null) {
            worldClientIn.addEventListener(this);
            this.loadRenderers();
        }
    }

    @Override
    public void loadRenderers() {
        if (this.world != null) {
            this.displayListEntitiesDirty = true;
            Blocks.LEAVES.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            Blocks.LEAVES2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();

            if (flag != this.vboEnabled) {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }

            synchronized(this.setTileEntities) {
                this.setTileEntities.clear();
            }

            this.renderEntitiesStartupCounter = 2;
        }
    }

    @Override
    protected void stopChunkUpdates() {
    }

    @Override
    public String getDebugInfoRenders() {
        int i = 0;
        int j = this.getRenderedChunks();
        return String.format("C: %d/%d %sD: %d, L: %d, %s", j, i,
                this.mc.renderChunksMany ? "(s) " : "", this.renderDistanceChunks, this.setLightUpdates.size(),
                this.renderDispatcher == null ? "null" : this.renderDispatcher.getDebugInfo());
    }

    @Override
    protected int getRenderedChunks() {
        return 0;
    }

    @Override
    public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
            this.loadRenderers();
        }

        if (this.reloadTextureSizes){
            this.reloadTextureSizes();
        }
    }

    @Override
    public int renderBlockLayer(BlockRenderLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
        RenderHelper.disableStandardItemLighting();
        this.mc.profiler.func_194339_b(() -> "render_" + blockLayerIn);
        this.renderBlockLayer(blockLayerIn);
        this.mc.profiler.endSection();
        return 0;
    }

    @Override
    public void updateChunks(long finishTimeNano) {
    }

    @Override
    protected void markBlocksForUpdate(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean updateImmediately) {
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }


    @Override
    public boolean hasNoChunkUpdates() {
        return true;
    }
}
