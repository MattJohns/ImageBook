package mattjohns.minecraft.imagebook;

import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public class BookItemUseParameters {
	public String bookNameInternal;
	public World world;
	public EntityPlayer player;
	
	public BookItemUseParameters(String bookNameInternal, World world, EntityPlayer player) {
		this.bookNameInternal = bookNameInternal;
		this.world = world;
		this.player = player;
	}
}
