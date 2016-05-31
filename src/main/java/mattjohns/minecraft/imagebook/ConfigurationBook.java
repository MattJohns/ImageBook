package mattjohns.minecraft.imagebook;

import mattjohns.common.math.Vector2F;
import mattjohns.minecraft.imagebook.ConfigurationTexture.enumImageType;

public class ConfigurationBook {
	private String nameInternal;
	private String nameDisplay;
	private float bookContainerMarginLeft;
	private float bookContainerMarginRight;
	private float bookContainerMarginTop;
	private float bookContainerMarginBottom;
	private float pageMarginInside;
	private float pageMarginOutside;
	private float pageMarginTop;
	private float pageMarginBottom;
	private boolean isBookContainerKeepAspectRatio;
	private boolean isPageKeepAspectRatio;
	private ConfigurationTexture icon;
	private ConfigurationTexture bookContainerImage;
	private ConfigurationTexture [] pageImageList = new ConfigurationTexture[0];

	public String nameInternalGet() {
		return nameInternal;
	}

	public void nameInternalSet(String item) {
		nameInternal = item;
	}

	public String nameDisplayGet() {
		return nameDisplay;
	}

	public void nameDisplaySet(String item) {
		nameDisplay = item;
	}

	public float bookContainerMarginLeftGet() {
		return bookContainerMarginLeft;
	}

	public void bookContainerMarginLeftSet(float item) {
		bookContainerMarginLeft = item;
	}

	public float bookContainerMarginRightGet() {
		return bookContainerMarginRight;
	}

	public void bookContainerMarginRightSet(float item) {
		bookContainerMarginRight = item;
	}

	public float bookContainerMarginTopGet() {
		return bookContainerMarginTop;
	}

	public void bookContainerMarginTopSet(float item) {
		bookContainerMarginTop = item;
	}

	public float bookContainerMarginBottomGet() {
		return bookContainerMarginBottom;
	}

	public void bookContainerMarginBottomSet(float item) {
		bookContainerMarginBottom = item;
	}

	public float pageMarginInsideGet() {
		return pageMarginInside;
	}

	public void pageMarginInsideSet(float item) {
		pageMarginInside = item;
	}

	public float pageMarginOutsideGet() {
		return pageMarginOutside;
	}

	public void pageMarginOutsideSet(float item) {
		pageMarginOutside = item;
	}

	public float pageMarginTopGet() {
		return pageMarginTop;
	}

	public void pageMarginTopSet(float item) {
		pageMarginTop = item;
	}

	public float pageMarginBottomGet() {
		return pageMarginBottom;
	}

	public void pageMarginBottomSet(float item) {
		pageMarginBottom = item;
	}

	public boolean isBookContainerKeepAspectRatioGet() {
		return isBookContainerKeepAspectRatio;
	}

	public void isBookContainerKeepAspectRatioSet(boolean item) {
		isBookContainerKeepAspectRatio = item;
	}

	public boolean isPageKeepAspectRatioGet() {
		return isPageKeepAspectRatio;
	}

	public void isPageKeepAspectRatioSet(boolean item) {
		isPageKeepAspectRatio = item;
	}

	public ConfigurationTexture iconGet() {
		return icon;
	}

	public void iconSet(ConfigurationTexture item) {
		icon = item;
	}

	public ConfigurationTexture bookContainerImageGet() {
		return bookContainerImage;
	}

	public void bookContainerImageSet(ConfigurationTexture item) {
		bookContainerImage = item;
	}

	public ConfigurationTexture [] pageImageListGet() {
		return pageImageList;
	}

	public void pageImageListSet(ConfigurationTexture [] item) {
		pageImageList = item;
	}

	public Vector2F bookContainerMarginTopLeftGet() {
		return new Vector2F(bookContainerMarginLeft, bookContainerMarginTop);
	}

	public Vector2F bookContainerMarginBottomRightGet() {
		return new Vector2F(bookContainerMarginRight, bookContainerMarginBottom);
	}

	public Vector2F pageMarginTopOutsideGet() {
		return new Vector2F(pageMarginOutside, pageMarginTop);
	}

	public Vector2F pageMarginBottomInsideGet() {
		return new Vector2F(pageMarginInside, pageMarginBottom);
	}

	public void validate() throws ConfigurationException {
		if (nameInternal.trim().length() <= 0)
			throw new ConfigurationException("Internal name is empty.");

		if (nameInternal.trim().length() <= 0)
			throw new ConfigurationException("Display name is empty.");

		// container margin
		if (bookContainerMarginLeft < 0f)
			throw new ConfigurationException("Container left margin is negative.");

		if (bookContainerMarginRight < 0f)
			throw new ConfigurationException("Container right margin is negative.");

		if (bookContainerMarginTop < 0f)
			throw new ConfigurationException("Container top margin is negative.");

		if (bookContainerMarginBottom < 0f)
			throw new ConfigurationException("Container bottom margin is negative.");

		if (bookContainerMarginLeft + bookContainerMarginRight > 1.0f)
		throw new ConfigurationException("Container left and right margins are too large (sum is greater than 1).");

		if (bookContainerMarginLeft + bookContainerMarginRight > 1.0f)
			throw new ConfigurationException("Container top and bottom margins are too large (sum is greater than 1).");

		// book margin
		if (pageMarginInside < 0f)
			throw new ConfigurationException("Book inside margin is negative.");

		if (pageMarginOutside < 0f)
			throw new ConfigurationException("Book outside margin is negative.");

		if (pageMarginTop < 0f)
			throw new ConfigurationException("Book top margin is negative.");

		if (pageMarginBottom < 0f)
			throw new ConfigurationException("Book bottom margin is negative.");

		if (pageMarginOutside + pageMarginInside > 1.0f)
		throw new ConfigurationException("Book inside and outside margins are too large (sum is greater than 1).");

		if (pageMarginTop + pageMarginBottom > 1.0f)
			throw new ConfigurationException("Book top and bottom margins are too large (sum is greater than 1).");

		if (icon == null) {
			throw new ConfigurationException("Icon is empty.");
		}

		if (bookContainerImage == null) {
			throw new ConfigurationException("Container image is empty.");
		}
	}
	
	public void setImpliedDefault() {
		if (icon == null) {
			icon = new ConfigurationTexture();
			icon.imageTypeSet(enumImageType.INTERNAL);
			icon.internalFilenameSet("minecraft:book");
		}
	}
}