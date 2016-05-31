package mattjohns.minecraft.imagebook;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import mattjohns.common.math.RectangleF;
import mattjohns.common.math.RectangleI;
import mattjohns.common.math.Vector2F;
import mattjohns.common.math.Vector2I;
import mattjohns.minecraft.imagebook.common.forge.ForgeApi;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.video.LayoutElementTexture;
import mattjohns.minecraft.imagebook.common.video.Render2d;

public class BookGuiScreen extends GuiScreen {
	private class ButtonId {
		public static final int CLOSE = 1;
		public static final int PAGE_PREVIOUS = 2;
		public static final int PAGE_NEXT = 3;
	}

	private int visiblePagePairIndex = 0;

	private LayoutElementTexture elementScreen;
	private LayoutElementTexture elementBookContainer;
	private LayoutElementTexture elementPageLeftContainer;
	private LayoutElementTexture elementPageLeft;
	private LayoutElementTexture elementPageRightContainer;
	private LayoutElementTexture elementPageRight;

	private Render2d render2d;
	private String openBookNameInternal;
	private BookTexture bookTexture;
	private ConfigurationBook bookConfiguration;
	private ForgeApi forgeApi;

    public BookGuiScreen(Render2d render2d,
    		String openBookNameInternal,
    		BookTexture bookTexture,
    		ConfigurationBook bookConfiguration,
    		ForgeApi forgeApi) {
    	this.render2d = render2d;
    	this.openBookNameInternal = openBookNameInternal;
    	this.bookTexture = bookTexture;
    	this.bookConfiguration = bookConfiguration;
    	this.forgeApi = forgeApi;
	}

    @Override
	public void initGui() {
		super.initGui();

		visiblePagePairIndex = 0;

		elementCreate(bookConfiguration);
		elementLayout(bookConfiguration);

		buttonAdd();
	}

    private void buttonAdd() {
 ////// button positions need to be updated wherever elementLayout() is called, so need to find an update method for them
 		Vector2F buttonSizeMinecraftScale;
 		Vector2F buttonSizeRelativeToScreen;
 		Vector2F buttonPositionMinecraftScale;
 		RectangleF buttonRectangleMinecraftScale;

 		// close button
 		buttonSizeMinecraftScale = new Vector2F(20f, 20f);
 		buttonSizeRelativeToScreen = render2d.sizeConvertMinecraftScaleToRelativeToScreen(buttonSizeMinecraftScale);

 		buttonPositionMinecraftScale = render2d.positionConvertRelativeToScreenToMinecraftScale(
 				new Vector2F(elementBookContainer.positionGet().bottomRightGet().xGet() - buttonSizeRelativeToScreen.xGet(),
 				elementBookContainer.positionGet().topLeftGet().yGet()));
 		
 		buttonRectangleMinecraftScale = RectangleF.createFromPositionAndSize(buttonPositionMinecraftScale,
 				buttonSizeMinecraftScale); 

 		buttonAdd(ButtonId.CLOSE,
 				buttonRectangleMinecraftScale,
 				"x");

 		// navigation buttons
 		
 		// previous
 		buttonSizeMinecraftScale = new Vector2F(60f, 20f);
 		buttonSizeRelativeToScreen = render2d.sizeConvertMinecraftScaleToRelativeToScreen(buttonSizeMinecraftScale);

 		buttonPositionMinecraftScale = render2d.positionConvertRelativeToScreenToMinecraftScale(
 				new Vector2F(elementBookContainer.positionGet().topLeftGet().xGet(),
 						elementBookContainer.positionGet().bottomRightGet().yGet() - buttonSizeRelativeToScreen.yGet()));

 		buttonRectangleMinecraftScale = RectangleF.createFromPositionAndSize(buttonPositionMinecraftScale,
 				buttonSizeMinecraftScale); 

 		buttonAdd(ButtonId.PAGE_PREVIOUS,
 				buttonRectangleMinecraftScale,
 				"Previous");

 		// next
 		buttonSizeMinecraftScale = new Vector2F(60f, 20f);
 		buttonSizeRelativeToScreen = render2d.sizeConvertMinecraftScaleToRelativeToScreen(buttonSizeMinecraftScale);

 		buttonPositionMinecraftScale = render2d.positionConvertRelativeToScreenToMinecraftScale(
 				new Vector2F(elementBookContainer.positionGet().bottomRightGet().xGet() - buttonSizeRelativeToScreen.xGet(),
 				elementBookContainer.positionGet().bottomRightGet().yGet() - buttonSizeRelativeToScreen.yGet()));

 		buttonRectangleMinecraftScale = RectangleF.createFromPositionAndSize(buttonPositionMinecraftScale,
 				buttonSizeMinecraftScale); 

 		buttonAdd(ButtonId.PAGE_NEXT,
 				buttonRectangleMinecraftScale,
 				"Next");
    }
    
	// buttonList is not properly typed so causes an unchecked type warning
	@SuppressWarnings( "unchecked" )
    private void buttonAdd(int id,
    		RectangleF positionMinecraftScaleF,
    		String text) {
		
		RectangleI positionMinecraftScale = RectangleI.convertFrom(positionMinecraftScaleF);
		
		buttonList.add(new GuiButton(id,
				positionMinecraftScale.topLeftGet().xGet(),
				positionMinecraftScale.topLeftGet().yGet(),
				positionMinecraftScale.sizeGet().xGet(),
				positionMinecraftScale.sizeGet().yGet(),
				text));
    }

	private void elementCreate(ConfigurationBook configuration) {
		elementScreen = LayoutElementTexture.createRoot();

		// book background
		elementBookContainer = new LayoutElementTexture(elementScreen,
				new RectangleF(configuration.bookContainerMarginTopLeftGet(),
						configuration.bookContainerMarginBottomRightGet()));

		// left page
		elementPageLeftContainer = new LayoutElementTexture(elementBookContainer,
				new RectangleF(new Vector2F(0f, 0f), new Vector2F(0.5f, 0f)));

		elementPageLeft = new LayoutElementTexture(elementPageLeftContainer,
				new RectangleF(
						new Vector2F(configuration.pageMarginOutsideGet(),
								configuration.pageMarginTopGet()),
						new Vector2F(configuration.pageMarginInsideGet(),
								configuration.pageMarginBottomGet())));

		elementPageLeft.alignSet(LayoutElementTexture.AlignType.RIGHT);

		// right page
		elementPageRightContainer = new LayoutElementTexture(elementBookContainer,
				new RectangleF(new Vector2F(0.5f, 0f), new Vector2F(0f, 0f)));

		elementPageRight = new LayoutElementTexture(elementPageRightContainer,
				new RectangleF(
						new Vector2F(configuration.pageMarginInsideGet(),
								configuration.pageMarginTopGet()),
						new Vector2F(configuration.pageMarginOutsideGet(),
								configuration.pageMarginBottomGet())));

		elementPageRight.alignSet(LayoutElementTexture.AlignType.LEFT);
	}

	private void elementLayout(ConfigurationBook configuration) {
		TextureResourceLocation bookContainer = bookTexture.bookContainerGet();

		Vector2F screenSizeMinecraftScale = render2d.screenGetSizeMinecraftScale();

		int pageIndexLeft = pageGetCurrentIndexLeft();
		if (isPageIndexValid(pageIndexLeft)) {

			TextureResourceLocation pageLeft = bookTexture.pageGet(pageIndexLeft);

			elementBookContainer.positionDerive(bookContainer.sizeGet(), screenSizeMinecraftScale,
					configuration.isBookContainerKeepAspectRatioGet());

			elementPageLeftContainer.positionDerive(Vector2I.zero(), screenSizeMinecraftScale,
					false);

			elementPageLeft.positionDerive(pageLeft.sizeGet(), screenSizeMinecraftScale,
					configuration.isPageKeepAspectRatioGet());
		}

		int pageIndexRight = pageGetCurrentIndexRight();
		if (isPageIndexValid(pageIndexRight)) {
			TextureResourceLocation pageRight = bookTexture.pageGet(pageIndexRight);

			elementPageRightContainer.positionDerive(Vector2I.zero(), screenSizeMinecraftScale,
					false);

			elementPageRight.positionDerive(pageRight.sizeGet(), screenSizeMinecraftScale,
					configuration.isPageKeepAspectRatioGet());
		}
	}

	private boolean isPageIndexValid(int item) {
		if (item < 0)
			return false;

		int numberOfPages = bookTexture.pageListGetSize();

		if (numberOfPages <= 0)
			return false;

		if (item >= numberOfPages)
			return false;

		return true;
	}

    @Override
	protected void actionPerformed(GuiButton guiButton) {
		switch(guiButton.id) {
			case ButtonId.CLOSE: close(); break;
			case ButtonId.PAGE_PREVIOUS: visiblePagePairMove(-1); break;
			case ButtonId.PAGE_NEXT: visiblePagePairMove(1); break;
		}
	}

    // silently fails if page index is invalid
	private void visiblePagePairMove(int deltaIndex) {
		int newIndex = visiblePagePairIndex + deltaIndex;

		if (isVisiblePagePairIndexValid(newIndex))
		{
			visiblePagePairIndex = newIndex;

			elementLayout(bookConfiguration);
		}
	}

	private boolean isVisiblePagePairIndexValid(int item) {
		return isPageIndexValid(item * 2);
	}

	private int pageGetCurrentIndexLeft() {
		return visiblePagePairIndex * 2;
	}

	private int pageGetCurrentIndexRight() {
		return pageGetCurrentIndexLeft() + 1;
	}

	@Override
	public void drawScreen(int cursorPositionX, int cursorPositionY, float numberOfGameTicksSinceLastRender) {
        drawDefaultBackground();

		TextureResourceLocation bookContainer = bookTexture.bookContainerGet();

		//// move to configuration file
		boolean isBlendAlpha = true;
		RectangleF texturePositon = new RectangleF(
				Vector2F.zero(), new Vector2F(1f, 1f));
		
		boolean isLightingEnabled = false;
		
		render2d.rectangleRelativeToScreen(bookContainer,
				elementBookContainer.positionGet(),
				isBlendAlpha,
				isLightingEnabled,
				texturePositon,
				0f);

		int pageIndexLeft = pageGetCurrentIndexLeft();
		if (isPageIndexValid(pageIndexLeft)) {
			TextureResourceLocation pageLeft = bookTexture.pageGet(pageGetCurrentIndexLeft());

			render2d.rectangleRelativeToScreen(pageLeft,
					elementPageLeft.positionGet(),
					isBlendAlpha,
					isLightingEnabled,
					texturePositon,
					0f);
		}

		int pageIndexRight = pageGetCurrentIndexRight();
		if (isPageIndexValid(pageIndexRight)) {
			TextureResourceLocation pageRight = bookTexture.pageGet(pageGetCurrentIndexRight());

			render2d.rectangleRelativeToScreen(pageRight,
					elementPageRight.positionGet(),
					isBlendAlpha,
					isLightingEnabled,
					texturePositon,
					0f);
		}

		// need to do this last so buttons are rendered on top
		super.drawScreen(cursorPositionX, cursorPositionY, numberOfGameTicksSinceLastRender);
	}
	
	public void close() {
		forgeApi.guiClose();
	}
}