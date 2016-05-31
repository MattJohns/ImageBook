package mattjohns.minecraft.imagebook;

import java.util.*;

public class BookTextureList extends ArrayList<BookTexture> {
	public BookTexture getByBookName(String item) {
		for (BookTexture texture : this) {
			if (texture.bookNameGet().equals(item))
				return texture;
		}

		return null;
	}
}