package mattjohns.minecraft.imagebook.common.video;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import mattjohns.common.math.*;
import mattjohns.minecraft.imagebook.common.video.*;
import net.minecraft.client.renderer.texture.TextureManager;

public class LayoutElementTexture {
	public enum AlignType {
		CENTER,
		LEFT,
		RIGHT,
		TOP,
		BOTTOM,
	}
	
	protected RectangleF position;
	protected RectangleF margin;
	protected AlignType align;
	protected LayoutElementTexture parent;

	public RectangleF positionGet() {
		return position;
	}

	public RectangleF marginGet() {
		return margin;
	}

	public void marginSet(RectangleF item) {
		margin = item;
	}

	public LayoutElementTexture parentGet() {
		return parent;
	}

	public void alignSet(AlignType item) {
		this.align = item;
	}

	public LayoutElementTexture() {
		parent = null;
		margin = new RectangleF();
		position = new RectangleF();
		align = AlignType.CENTER;
	}

	public LayoutElementTexture(LayoutElementTexture parent) {
		attach(parent);
		margin = new RectangleF();
		position = new RectangleF();
		align = AlignType.CENTER;
	}

	public LayoutElementTexture(LayoutElementTexture parent, RectangleF margin) {
		attach(parent);
		this.margin = margin;
		position = new RectangleF();
		align = AlignType.CENTER;
	}

	public static LayoutElementTexture createRoot() {
		LayoutElementTexture returnValue = new LayoutElementTexture();

		// takes up the whole screen (i.e. 0f is left edge, 1f is right edge)
		returnValue.position = new RectangleF(new Vector2F(0f, 0f), new Vector2F(1f, 1f));
		returnValue.margin = new RectangleF();
		returnValue.parent = null;

		return returnValue;
	}

	public void attach(LayoutElementTexture parent) {
		this.parent = parent;
	}

	public void positionDerive(Vector2I textureSize,
			Vector2F screenSizeIndependent,
			boolean isKeepAspectRatio) {

		RectangleF parentPosition = parent.positionGet();
		Vector2F parentPositionCenter = parentPosition.centerGet();
		Vector2F parentSize = parentPosition.sizeGet();

		RectangleF marginActual = margin.multiply(parentSize);
		Vector2F proposedPositionTopLeft = parentPosition.topLeftGet().add(marginActual.topLeftGet());
		Vector2F proposedPositionBottomRight = parentPosition.bottomRightGet().subtract(marginActual.bottomRightGet());
		RectangleF proposedPosition = new RectangleF(proposedPositionTopLeft, proposedPositionBottomRight);
		Vector2F proposedSize = proposedPosition.sizeGet();
		Vector2F proposedPositionCenter = proposedPosition.centerGet();

		if (isKeepAspectRatio) {
			float imageAspectRatio = textureSize.aspectRatioGet();
			float screenSizeAspectRatio = screenSizeIndependent.aspectRatioGet();

			// take screen aspect ratio into account because the 'proportional' sizes used in this method mean
			// a 1 x 1 square is really a 1.6 x 1 rectangle (depending on screen shape)
			Vector2F effectiveProposedSize = new Vector2F(proposedSize.xGet(), proposedSize.yGet() / screenSizeAspectRatio);

			float effectiveProposedAspectRatio = effectiveProposedSize.aspectRatioGet();

			if (effectiveProposedAspectRatio < imageAspectRatio) {
				// left and right will be on the margin, top and bottom will be smaller than the margin

				// make height is same as width, then scale down to correct image aspect ratio,
				// then allow for actual screen size aspect
				float newSizeY = (effectiveProposedSize.xGet() / imageAspectRatio)
						* screenSizeAspectRatio;

				// if element is docked on top or bottom then keep it touching on that margin,
				// rather than just shrinking both top and bottom towards the center of the element
				float newPositionYTop;
				float newPositionYBottom;

				switch (align) {
					case CENTER:
					case LEFT:
					case RIGHT: {
						// docking doesn't affect this resizing procedure, just shrink top and bottom towards center
						newPositionYTop = parentPositionCenter.yGet() - (newSizeY / 2f);
						newPositionYBottom = parentPositionCenter.yGet() + (newSizeY / 2f);
						break;
					}

					case TOP: {
						// docked on top, keep it touching that top margin while the bottom gets shrunk twice as much
						newPositionYTop = proposedPosition.topLeftGet().yGet();
						newPositionYBottom = proposedPosition.topLeftGet().yGet() + newSizeY;
						break;
					}

					case BOTTOM: {
						// docked on bottom, shrink top only
						newPositionYTop = proposedPosition.bottomRightGet().yGet() - newSizeY;
						newPositionYBottom = proposedPosition.bottomRightGet().yGet();
						break;
					}

					default: throw new IllegalArgumentException();
				}

				// update y position
				proposedPosition.topLeftSet(new Vector2F(proposedPosition.topLeftGet().xGet(), newPositionYTop));
				proposedPosition.bottomRightSet(new Vector2F(proposedPosition.bottomRightGet().xGet(), newPositionYBottom));
			}
			else {
				// top and bottom will be on the margin, left and right will be smaller than the margin

				// make width is same as height, then scale down to correct image aspect ratio,
				// then allow for actual screen size aspect
				float newSizeX = (proposedSize.yGet() * imageAspectRatio)
						/ screenSizeAspectRatio;

				// if element is docked on left or right then keep it touching on that margin,
				// rather than just shrinking both left and right towards the center of the element
				float newPositionXLeft;
				float newPositionXRight;

				switch (align) {
					case CENTER:
					case TOP:
					case BOTTOM: {
						// docking doesn't affect this resizing procedure, just shrink top and bottom towards center
						newPositionXLeft = parentPositionCenter.xGet() - (newSizeX / 2f);
						newPositionXRight = parentPositionCenter.xGet() + (newSizeX / 2f);
						break;
					}

					case LEFT: {
						// docked on top, keep it touching that top margin while the bottom gets shrunk twice as much
						newPositionXLeft = proposedPosition.topLeftGet().xGet();
						newPositionXRight = proposedPosition.topLeftGet().xGet() + newSizeX;
						break;
					}

					case RIGHT: {
						// docked on bottom, shrink top only
						newPositionXLeft = proposedPosition.bottomRightGet().xGet() - newSizeX;
						newPositionXRight = proposedPosition.bottomRightGet().xGet();
						break;
					}

					default: throw new IllegalArgumentException();
				}

				// update x position
				proposedPosition.topLeftSet(new Vector2F(newPositionXLeft, proposedPosition.topLeftGet().yGet()));
				proposedPosition.bottomRightSet(new Vector2F(newPositionXRight, proposedPosition.bottomRightGet().yGet()));
			}
		}

		position = new RectangleF(proposedPosition);
	}
}