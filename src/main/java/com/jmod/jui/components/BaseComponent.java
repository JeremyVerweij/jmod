package com.jmod.jui.components;

import com.jmod.jui.ui.JUIScreen;
import com.jmod.jui.ui.interfaces.ITranslatorProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseComponent {
    protected int dummyX;
    protected int dummyY;
    protected int width;
    protected int height;
    protected int backgroundColor;
    protected int foregroundColor;
    protected int highlightBackgroundColor;
    protected int highlightForegroundColor;
    protected TextureAtlasSprite backgroundSprite;
    protected ITranslatorProvider translatorProvider;
    protected String translationKey;

    protected BaseComponent parent;
    protected final String id;
    protected final List<BaseComponent> children;
    protected JUIScreen owner;
    protected final Minecraft mc;

    public BaseComponent(String id, Minecraft mc) {
        this.id = id;
        this.mc = mc;

        this.children = new ArrayList<>();
    }

    protected abstract void drawBackground(int x, int y, boolean isHover);
    protected abstract void drawForeground(int x, int y, boolean isHover);

    protected boolean isHover(int left, int top, int mouseX, int mouseY){
        return this.isInBoundingBox(left, top, mouseX, mouseY);
    }

    public void onMouseClick(int button, int offsetX, int offsetY, int mouseX, int mouseY){
        if (this.grabFocus()){
            if (this.owner.getFocussed() == null){
                this.owner.setFocussed(this);
                return;
            }
        }

        for (BaseComponent child : this.children) {
            if (child.isInBoundingBox(offsetX, offsetY, mouseX, mouseY)){
                child.onMouseClick(button, offsetX, offsetY, mouseX, mouseY);
            }
        }
    }

    public void onMouseScroll(int amount, int offsetX, int offsetY, int mouseX, int mouseY){
        for (BaseComponent child : this.children) {
            if (child.isInBoundingBox(offsetX, offsetY, mouseX, mouseY)){
                child.onMouseScroll(amount, offsetX, offsetY, mouseX, mouseY);
            }
        }
    }

    public void onMouseDrag(int dragX, int dragY, int offsetX, int offsetY, int mouseX, int mouseY){
        for (BaseComponent child : this.children) {
            if (child.isInBoundingBox(offsetX, offsetY, mouseX, mouseY)){
                child.onMouseDrag(dragX, dragY, offsetX, offsetY, mouseX, mouseY);
            }
        }
    }

    /**
     * @param key keycode
     * @param shift shift key
     * @param ctrl ctrl key
     * @param alt alt key
     * @return True if event is captured
     */
    public boolean onKeyType(char character, int key, boolean shift, boolean ctrl, boolean alt, int offsetX, int offsetY, int mouseX, int mouseY){
        for (BaseComponent child : this.children) {
            if (child.isInBoundingBox(offsetX, offsetY, mouseX, mouseY)){
                if(child.onKeyType(character, key, shift, ctrl, alt, offsetX, offsetY, mouseX, mouseY)) return true;
            }
        }

        return false;
    }

    /**
     * @return Whether to keep catching key events after the component is clicked
     */
    public abstract boolean grabFocus();

    public void onFocusGained(){}
    public void onFocusLost(){}

    public void addChildExtraAttrib(BaseComponent child, String key, String value){}

    public void addExtraAttribute(String key, String value){
        if (key.startsWith("_") && parent != null){
            this.parent.addChildExtraAttrib(this, key.substring(1), value);
        }
    }

    public void draw(int left, int top, int mouseX, int mouseY){
        int x = this.getX(left);
        int y = this.getY(top);

        this.drawBackground(x, y, isHover(left, top, mouseX, mouseY));
        this.drawForeground(x, y, isHover(left, top, mouseX, mouseY));

        for (BaseComponent child : this.children) {
            child.draw(left, top, mouseX, mouseY);
        }
    }

    public void addChild(BaseComponent component){
        if (component.parent == null){
            this.children.add(component);
            component.parent = this;
        }
    }

    public void removeChild(BaseComponent component){
        if (this.children.contains(component)){
            this.children.remove(component);
            component.parent = null;
        }
    }

    public boolean isInBoundingBox(int offsetX, int offsetY, int mouseX, int mouseY){
        return mouseX >= (this.getX(offsetX) - 1) && mouseX < (this.getX(offsetX) + this.getWidth()) &&
                mouseY > (this.getY(offsetY)) && mouseY < (this.getY(offsetY) + this.getHeight());
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public void setTranslatorProvider(ITranslatorProvider translatorProvider) {
        this.translatorProvider = translatorProvider;
    }

    public void setBackgroundSprite(TextureAtlasSprite backgroundSprite) {
        this.backgroundSprite = backgroundSprite;
    }

    public void setHighlightForegroundColor(int highlightForegroundColor) {
        this.highlightForegroundColor = highlightForegroundColor;
    }

    public void setHighlightBackgroundColor(int highlightBackgroundColor) {
        this.highlightBackgroundColor = highlightBackgroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setHeight(int height) {
        this.height = height;
        if(this.parent != null)
            this.parent.updateChildSize(this);
    }

    public void setWidth(int width) {
        this.width = width;
        if(this.parent != null)
            this.parent.updateChildSize(this);
    }

    public void setOwner(JUIScreen owner) {
        this.owner = owner;
    }

    public void setDummyY(int dummyY) {
        this.dummyY = dummyY;
        if(this.parent != null)
            this.parent.updateChildSize(this);
    }

    public void setDummyX(int dummyX) {
        this.dummyX = dummyX;
        if(this.parent != null)
            this.parent.updateChildSize(this);
    }

    public int getDummyX() {
        return dummyX;
    }

    public int getDummyY() {
        return dummyY;
    }

    public int getChildOffsetX(BaseComponent child){
        return (this.parent == null ? 0 : this.parent.getChildOffsetX(this));
    }

    public int getChildOffsetY(BaseComponent child){
        return (this.parent == null ? 0 : this.parent.getChildOffsetY(this));
    }

    public int getX(int left){
        return this.getDummyX() + (this.parent == null ? 0 : this.parent.getChildOffsetX(this)) + left;
    }

    public int getY(int top){
        return this.getDummyY() + (this.parent == null ? 0 : this.parent.getChildOffsetY(this)) + top;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BaseComponent getParent() {
        return parent;
    }

    public List<BaseComponent> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public boolean hasFocus(){
        return this.owner.getFocussed() == this;
    }

    protected void updateChildSize(BaseComponent child){

    }

    /**
     * Draws a thin horizontal line between two points.
     */
    protected void drawHorizontalLine(int startX, int endX, int y, int color)
    {
        if (endX < startX)
        {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRect(startX, y, endX + 1, y + 1, color);
    }

    /**
     * Draw a 1 pixel wide vertical line. Args : x, y1, y2, color
     */
    protected void drawVerticalLine(int x, int startY, int endY, int color)
    {
        if (endY < startY)
        {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRect(x, startY + 1, x + 1, endY, color);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    protected void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors (ARGB format). Args : x1, y1, x2, y2,
     * topColor, bottomColor
     */
    protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, double zLevel)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)right, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double)left, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    protected void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    protected void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color)
    {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }

    /**
     * Draws a textured rectangle at the current z-value.
     */
    protected void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, double zLevel)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle using the texture currently bound to the TextureManager
     */
    protected void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV, double zLevel)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(xCoord + 0.0F), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + 0) * 0.00390625F), (double)((float)(minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + (float)maxU), (double)(yCoord + (float)maxV), (double)zLevel).tex((double)((float)(minU + maxU) * 0.00390625F), (double)((float)(minV + maxV) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + (float)maxU), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + maxU) * 0.00390625F), (double)((float)(minV + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(xCoord + 0.0F), (double)(yCoord + 0.0F), (double)zLevel).tex((double)((float)(minU + 0) * 0.00390625F), (double)((float)(minV + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    protected void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int widthIn, int heightIn, double zLevel)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(xCoord + 0), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + heightIn), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos((double)(xCoord + widthIn), (double)(yCoord + 0), (double)zLevel).tex((double)textureSprite.getMaxU(), (double)textureSprite.getMinV()).endVertex();
        bufferbuilder.pos((double)(xCoord + 0), (double)(yCoord + 0), (double)zLevel).tex((double)textureSprite.getMinU(), (double)textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a textured rectangle at z = 0. Args: x, y, u, v, width, height, textureWidth, textureHeight
     */
    protected void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Draws a scaled, textured, tiled modal rect at z = 0. This method isn't used anywhere in vanilla code.
     *
     * @param u Texture U (or x) coordinate, in pixels
     * @param v Texture V (or y) coordinate, in pixels
     * @param uWidth Width of the rendered part of the texture, in pixels. Parts of the texture outside of it will wrap
     * around
     * @param vHeight Height of the rendered part of the texture, in pixels. Parts of the texture outside of it will
     * wrap around
     * @param tileWidth total width of the texture
     * @param tileHeight total height of the texture
     */
    protected void drawScaledCustomSizeModalRect(int x, int y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)uWidth) * f), (double)((v + (float)vHeight) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)uWidth) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

    protected void enableScissors(int x, int y, int width, int height){
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        int scale = scaledResolution.getScaleFactor();

        int scissorX = x * scale;
        int scissorY = (this.mc.displayHeight) - ((y + height) * scale);
        int scissorWidth = width * scale;
        int scissorHeight = height * scale;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    protected void disableScissors(){
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
