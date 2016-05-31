package mattjohns.minecraft.imagebook;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mattjohns.minecraft.imagebook.common.event.EventReceive;
import mattjohns.minecraft.imagebook.common.event.EventSend;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

// the actual minecraft in-game item
public class BookItem extends Item {
	private static final String ICON_NAME = "bookDefault";
	
	public EventSend<BookItemUseParameters> eventBookItemUse = new EventSend<BookItemUseParameters>();

	public BookItem(String unlocalizedName) {
		super();
		
		setUnlocalizedName(unlocalizedName);
        setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		eventBookItemUse.send(new BookItemUseParameters(
				bookNameInternalGet(),
				world, player));

		return stack;
	}

	public String bookNameInternalGet() {
		// for some reason the unlocalized name gets automatically prefixed with "item."
		return getUnlocalizedName().replace("item.", "");
	}
}