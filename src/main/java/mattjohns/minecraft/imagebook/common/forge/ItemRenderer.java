package mattjohns.minecraft.imagebook.common.forge;

import org.lwjgl.opengl.GL11;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraftforge.client.IItemRenderer;
import mattjohns.common.math.RectangleF;
import mattjohns.common.math.Vector2F;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.video.Render2d;

public class ItemRenderer implements IItemRenderer {
	private TextureResourceLocation texture;
	private Render2d render2d;
	
	public ItemRenderer(TextureResourceLocation texture,
			Render2d render2d) {
		this.texture = texture;
		this.render2d = render2d;
	}

	public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
		switch (type) {
			case ENTITY:
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			case INVENTORY: return true;
			
			default: return false;
		}
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
		switch (type) {
			case ENTITY: {
				if (helper == ItemRendererHelper.ENTITY_BOBBING)
					return true;
				
				if (helper == ItemRendererHelper.ENTITY_ROTATION)
					return true;
				
				return false;
	        }
			
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			case INVENTORY:
			default: return false;
		}
	}

	private Vector2F referenceFrameGet(ItemRenderType type) {
        switch (type) {
			case ENTITY: {
				if (RenderItem.renderInFrame) {
					// picture frame
					return new Vector2F(-0.5f, -0.3f);
				}
				else {
					// dropped item
					return new Vector2F(-0.5f, 0f);
				}
			}
			
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			case INVENTORY:
			default: {
				return Vector2F.zero();
			}
        }
	}
        
	private RectangleF renderPositionGet(ItemRenderType type) {
        switch (type) {
			case INVENTORY: {
				return new RectangleF(Vector2F.zero(), new Vector2F(16f, 16f));
			}
				
			case ENTITY:
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			default: {
				return new RectangleF(Vector2F.zero(), new Vector2F(1f, 1f));
			}
        }
	}

	private RectangleF texturePositionGet(ItemRenderType type) {
        switch (type) {
			case INVENTORY: {
				// inventory gets drawn without any axis flip
				return new RectangleF(Vector2F.zero(), new Vector2F(1f, 1f));
			}
				
			case ENTITY:
			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			default: {
			    // for some reason the texture needs to be flipped along both x and y
				return new RectangleF(new Vector2F(1f, 1f), Vector2F.zero());
			}
        }
	}

	private boolean isRender2SidedGet(ItemRenderType type) {
        switch (type) {
			case ENTITY:
			case EQUIPPED: {
				return true;
			}
				
			case INVENTORY:
			case EQUIPPED_FIRST_PERSON:
			default: {
				return false;
			}
        }
	}

	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		RectangleF positionMinecraftScale = renderPositionGet(type);
		RectangleF texturePosition = texturePositionGet(type);
		Vector2F referenceFrameTranslation = referenceFrameGet(type);
		boolean isCullFaceEnable = !isRender2SidedGet(type);

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		
		// need to draw both sides of the item if it's on the ground
        if (isCullFaceEnable)
			GL11.glEnable(GL11.GL_CULL_FACE);
        else
			GL11.glDisable(GL11.GL_CULL_FACE);
        	
        GL11.glPushMatrix();

        // allow for different render positioning required by the game
        GL11.glTranslatef(referenceFrameTranslation.xGet(),
        		referenceFrameTranslation.yGet(),
        		0f);

        // render
		render2d.rectangleMinecraftScale(texture,
				positionMinecraftScale,
				true,
				false,
				texturePosition,
				0f);
		
		GL11.glPopMatrix();
		
		GL11.glPopAttrib();
	}    
}
