package mattjohns.minecraft.imagebook.common.storage;

import mattjohns.common.math.*;
import net.minecraft.util.ResourceLocation;

public class TextureResourceLocation {
	private ResourceLocation location;

	private boolean isSizeValid = false;
	private Vector2I size = Vector2I.zero();
	private boolean isRepeat = false;
	private Vector2F repeatFrequency = new Vector2F(1f, 1f);
	
	public TextureResourceLocation(ResourceLocation location) {
		this.location = location;
	}

	public TextureResourceLocation(ResourceLocation location, Vector2I size,
			boolean isRepeat, Vector2F repeatFrequency) {
		this.location = location;
		
		isSizeValid = true;
		this.size = size;
		
		this.isRepeat = isRepeat;
		this.repeatFrequency = repeatFrequency;
	}

	public ResourceLocation locationGet() {
		return location;
	}

	public boolean isSizeValidGet() {
		return isSizeValid;
	}

	public Vector2I sizeGet() {
		return size;
	}
	
	public boolean isRepeatGet() {
		return isRepeat;
	}
	
	public Vector2F repeatFrequencyGet() {
		return repeatFrequency;
	}
}