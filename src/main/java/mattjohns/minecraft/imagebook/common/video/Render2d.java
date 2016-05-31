package mattjohns.minecraft.imagebook.common.video;

import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import com.sun.prism.impl.TextureResourcePool;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import mattjohns.common.math.*;
import mattjohns.minecraft.imagebook.common.forge.ForgeApi;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation; 

public class Render2d{
	private ForgeApi forgeApi;

	public Render2d(ForgeApi forgeApi) {
		this.forgeApi = forgeApi;
	}
	
	// minecraft won't render textures larger than 256 x 256 so need to use tessellated rendering
	// for them
	private static final Vector2I MAXIMUM_QUAD_TEXTURE_SIZE_WITHOUT_TESSELATION = new Vector2I(256, 256);

	public Vector2F screenGetSizeMinecraftScale() {
		return forgeApi.screenGetSizeMinecraftScale();
	}

	// RelativeToScreen means (0, 0) is the top left of the screen and (1, 1) is the bottom right.
	// MinecraftScale means the coordinate system used for gui rendering which changes depending
	// on the window size.
	public Vector2F positionConvertRelativeToScreenToMinecraftScale(Vector2F item) {
		Vector2F screenSizeMinecraftScale = forgeApi.screenGetSizeMinecraftScale();

		return item.multiply(screenSizeMinecraftScale);
	}

	public Vector2F sizeConvertRelativeToScreenToMinecraftScale(Vector2F item) {
		return positionConvertRelativeToScreenToMinecraftScale(item);
	}

	public Vector2F positionConvertMinecraftScaleToRelativeToScreen(Vector2F item) {
		Vector2F screenSizeMinecraftScale = forgeApi.screenGetSizeMinecraftScale();

		return item.divide(screenSizeMinecraftScale);
	}

	public Vector2F sizeConvertMinecraftScaleToRelativeToScreen(Vector2F item) {
		return positionConvertMinecraftScaleToRelativeToScreen(item);
	}

	public void rectangleRelativeToScreen(TextureResourceLocation textureResourceLocation,
			RectangleF positionRelativeToScreen,
			boolean isBlendAlphaEnabled,
			boolean isLightingEnabled,
			RectangleF texturePosition,
			float positionZ)
	{
		Vector2F screenSizeMinecraftScale = screenGetSizeMinecraftScale();
		RectangleF positionMinecraftScale = positionRelativeToScreen.multiply(screenSizeMinecraftScale);

		rectangleMinecraftScale(textureResourceLocation,
				positionMinecraftScale,
				isBlendAlphaEnabled,
				isLightingEnabled,
				texturePosition,
				positionZ);
	}

	public void rectangleMinecraftScale(TextureResourceLocation textureResourceLocation,
				RectangleF positionMinecraftScale,
				boolean isBlendAlphaEnabled,
				boolean isLightingEnabled,
				RectangleF texturePosition,
				float positionZ) {
		
		TextureManager textureManager = forgeApi.textureManagerGet();
		Vector2F sizeMinecraftScale = positionMinecraftScale.sizeGet();

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glPushMatrix();
		
		if (isLightingEnabled)
			GL11.glEnable(GL11.GL_LIGHTING);
		else
			GL11.glDisable(GL11.GL_LIGHTING);
		
		if (isBlendAlphaEnabled) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		else {
			GL11.glDisable(GL11.GL_BLEND);
		}

		textureManager.bindTexture(textureResourceLocation.locationGet());

		rectangle(positionMinecraftScale.topLeftGet(),
				sizeMinecraftScale,
				texturePosition,
				textureResourceLocation.sizeGet(),
				positionZ);

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	// texture frequency is how many times it repeats across the quad
	public void rectangle(Vector2F positionMinecraftScale, Vector2F size,
			RectangleF texturePosition, Vector2I textureSize, float positionZ) {
		if (textureSize.xGet() > MAXIMUM_QUAD_TEXTURE_SIZE_WITHOUT_TESSELATION.xGet()
				|| textureSize.yGet() > MAXIMUM_QUAD_TEXTURE_SIZE_WITHOUT_TESSELATION.yGet()) {
			// texture too big, need to tessellate
			rectangleTessellate(positionMinecraftScale, size, texturePosition, positionZ);
		}
		else {
			rectangleNoTessellate(positionMinecraftScale, size, texturePosition, positionZ);
		}
	}
	
	public static void rectangleTessellate(Vector2F positionMinecraftScale, Vector2F size,
			RectangleF texturePosition, float positionZ) {
		double sizeX = (double)size.xGet();
		double sizeY = (double)size.yGet();
		double positionX1 = (double)positionMinecraftScale.xGet();
		double positionY1 = (double)positionMinecraftScale.yGet();
		double positionX2 = positionX1 + sizeX;
		double positionY2 = positionY1 + sizeY;
		
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();    
		tessellator.addVertexWithUV(positionX1, positionY2, positionZ,
				texturePosition.topLeftGet().xGet(), texturePosition.bottomRightGet().yGet());
		tessellator.addVertexWithUV(positionX2, positionY2, positionZ,
				texturePosition.bottomRightGet().xGet(), texturePosition.bottomRightGet().yGet());
		tessellator.addVertexWithUV(positionX2, positionY1, positionZ,
				texturePosition.bottomRightGet().xGet(), texturePosition.topLeftGet().yGet());
		tessellator.addVertexWithUV(positionX1, positionY1, positionZ,
				texturePosition.topLeftGet().xGet(), texturePosition.topLeftGet().yGet());

		tessellator.draw();
	}

    private static void rectangleNoTessellate(Vector2F positionMinecraftScale,
    		Vector2F size, RectangleF texturePosition, float positionZ)
    {
    	// anti clockwise from bottom left
    	GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(texturePosition.topLeftGet().xGet(), texturePosition.topLeftGet().yGet());
    	GL11.glVertex3f(0f, 0f, positionZ);
		GL11.glTexCoord2f(texturePosition.topLeftGet().xGet(), texturePosition.bottomRightGet().yGet());
    	GL11.glVertex3f(0f, size.yGet(), positionZ);
		GL11.glTexCoord2f(texturePosition.bottomRightGet().xGet(), texturePosition.bottomRightGet().yGet());
    	GL11.glVertex3f(size.xGet(), size.yGet(), positionZ);
		GL11.glTexCoord2f(texturePosition.bottomRightGet().xGet(), texturePosition.topLeftGet().yGet());
    	GL11.glVertex3f(size.xGet(), 0f, positionZ);
    	GL11.glEnd();
    }

	public static void textureSetClamp(boolean state) {
		int clampType;
		if (state)
			clampType = GL11.GL_CLAMP;
		else
			clampType = GL11.GL_REPEAT;

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, clampType);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, clampType);
	}
}