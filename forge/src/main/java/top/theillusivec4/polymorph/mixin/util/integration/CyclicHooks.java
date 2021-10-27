package top.theillusivec4.polymorph.mixin.util.integration;

import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CyclicHooks {

  public static <T extends IRecipe<C>, C extends IInventory> Optional<T> getRecipe(
      ArrayList<ItemStack> stacks, World worldIn, TileEntity te) {

//    if (worldIn != null && worldIn.getServer() != null) {
//      CraftingInventory craftingInventory =
//          new AbstractCraftingRecipeProcessor.FalseCraftingInventory(3, 3);
//
//      for (int i = 0; i < 3; i++) {
//
//        for (int j = 0; j < 3; j++) {
//          int indexInArray = i + j * 3;
//          ItemStack stack = stacks.get(indexInArray);
//          craftingInventory.setInventorySlotContents(indexInArray, stack.copy());
//        }
//      }
//      return (Optional<T>) RecipeSelection.getTileEntityRecipe(IRecipeType.CRAFTING, craftingInventory, worldIn,
//          te);
//    } else {
    return Optional.empty();
//    }
  }
}
