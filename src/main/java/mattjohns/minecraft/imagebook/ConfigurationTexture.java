package mattjohns.minecraft.imagebook;

import mattjohns.common.math.*;
import mattjohns.minecraft.imagebook.common.storage.TextureStorage;

public class ConfigurationTexture {
	public enum enumImageType {
		EXTERNAL_FILE,
		IMAGEBOOK_RESOURCE,
		INTERNAL,
		RESOURCE_PACK,
		SOLID_COLOR
	};

	private enumImageType imageType;
	private String externalPathRelative;
	private String internalFilename;
	private String packId;
	private int solidColor;
	private TextureStorage.GameObjectType internalGameObjectType;
	private int internalMetadata;
	private boolean isRepeat;
	private float repeatRatioX;
	private float repeatRatioY;

	public ConfigurationTexture() {
		imageType = enumImageType.EXTERNAL_FILE;
		externalPathRelative = "";
		internalFilename = "";
		packId = "";
		isRepeat = true;
		solidColor = 0xffffffff;
		repeatRatioX = 1f;
		repeatRatioY = 1f;
		internalMetadata = 0;
		internalGameObjectType = internalGameObjectType.ITEM;
	}

	public enumImageType imageTypeGet() {
		return imageType;
	}
	
	public void imageTypeSet(enumImageType item) {
		imageType = item;
	}

	public String externalPathRelativeGet() {
		return externalPathRelative;
	}

	public void externalPathRelativeSet(String item) {
		externalPathRelative = item;
	}

	public String internalFilenameGet() {
		return internalFilename;
	}

	public void internalFilenameSet(String item) {
		internalFilename = item;
	}

	public String packIdGet() {
		return packId;
	}

	public void packIdSet(String item) {
		packId = item;
	}

	public int solidColorGet() {
		return solidColor;
	}

	public void solidColorSet(int item) {
		solidColor = item;
	}

	public TextureStorage.GameObjectType internalGameObjectTypeGet() {
		return internalGameObjectType;
	}

	public void internalGameObjectTypeSet(TextureStorage.GameObjectType item) {
		internalGameObjectType = item;
	}

	public int internalMetadataGet() {
		return internalMetadata;
	}

	public void internalMetadataSet(int item) {
		internalMetadata = item;
	}

	public boolean isRepeatGet() {
		return isRepeat;
	}

	public void isRepeatSet(boolean item) {
		isRepeat = item;
	}

	public float repeatRatioXGet() {
		return repeatRatioX;
	}

	public void repeatRatioXGet(float item) {
		repeatRatioX = item;
	}

	public float repeatRatioYGet() {
		return repeatRatioY;
	}

	public void repeatRatioYGet(float item) {
		repeatRatioY = item;
	}
}
