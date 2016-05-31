package mattjohns.minecraft.imagebook;

import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocation;
import mattjohns.minecraft.imagebook.common.storage.TextureResourceLocationList;

public class BookTexture {
	private String bookName;
	private TextureResourceLocation itemIconTextureResourceLocation;
	private TextureResourceLocation bookContainerTextureResourceLocation;
	private TextureResourceLocationList pageTextureResourceLocationList = new TextureResourceLocationList();

	public BookTexture(String bookName) {
		this.bookName = bookName;
	}

	public String bookNameGet() {
		return bookName;
	}

	public TextureResourceLocation itemIconGet() {
		return itemIconTextureResourceLocation;
	}

	public void itemIconSet(TextureResourceLocation item) {
		itemIconTextureResourceLocation = item;
	}

	public TextureResourceLocation bookContainerGet() {
		return bookContainerTextureResourceLocation;
	}

	public void bookContainerSet(TextureResourceLocation item) {
		bookContainerTextureResourceLocation = item;
	}

	public void pageAdd(TextureResourceLocation item) {
		pageTextureResourceLocationList.add(item);
	}

	public void pageListSet(TextureResourceLocationList item) {
		pageTextureResourceLocationList = item;
	}

	public int pageListGetSize() {
		return pageTextureResourceLocationList.size();
	}

	public TextureResourceLocation pageGet(int pageIndex) {
		return pageTextureResourceLocationList.get(pageIndex);
	}
}