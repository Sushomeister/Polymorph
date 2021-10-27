package top.theillusivec4.polymorph.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.polymorph.api.PolymorphApi;
import top.theillusivec4.polymorph.api.common.capability.IPlayerRecipeData;
import top.theillusivec4.polymorph.api.common.capability.ITileEntityRecipeData;
import top.theillusivec4.polymorph.common.capability.PlayerRecipeData;
import top.theillusivec4.polymorph.common.capability.PolymorphCapabilities;
import top.theillusivec4.polymorph.common.integration.AbstractCompatibilityModule;

@SuppressWarnings("unused")
public class CommonEventsListener {

  @SubscribeEvent
  public void openContainer(final PlayerContainerEvent.Open pEvent) {
    PlayerEntity player = pEvent.getPlayer();

    if (!player.world.isRemote() && player instanceof ServerPlayerEntity) {
      ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
      Container container = pEvent.getContainer();

      for (AbstractCompatibilityModule integration : PolymorphMod.getIntegrations()) {

        if (integration.openContainer(container, serverPlayerEntity)) {
          return;
        }
      }
    }
  }

  @SubscribeEvent
  public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> pEvent) {
    TileEntity te = pEvent.getObject();
    PolymorphApi.common().tryCreateRecipeData(te).ifPresent(
        recipeData -> pEvent.addCapability(PolymorphCapabilities.TILE_ENTITY_RECIPE_DATA_ID,
            new TileEntityRecipeDataProvider(recipeData)));
  }

  @SubscribeEvent
  public void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> pEvent) {
    Entity entity = pEvent.getObject();

    if (entity instanceof PlayerEntity) {
      PlayerRecipeData data = new PlayerRecipeData((PlayerEntity) entity);
      pEvent.addCapability(PolymorphCapabilities.PLAYER_RECIPE_DATA_ID,
          new PlayerRecipeDataProvider(data));
    }
  }

  private static class PlayerRecipeDataProvider implements ICapabilitySerializable<INBT> {

    final IPlayerRecipeData recipeData;
    final LazyOptional<IPlayerRecipeData> capability;

    public PlayerRecipeDataProvider(IPlayerRecipeData pRecipeData) {
      this.recipeData = pRecipeData;
      this.capability = LazyOptional.of(() -> this.recipeData);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> pCapability,
                                             @Nullable Direction pDirection) {
      return PolymorphCapabilities.PLAYER_RECIPE_DATA.orEmpty(pCapability, this.capability);
    }

    @Override
    public INBT serializeNBT() {
      return PolymorphCapabilities.PLAYER_RECIPE_DATA.writeNBT(this.recipeData, null);
    }

    @Override
    public void deserializeNBT(INBT pNbt) {
      PolymorphCapabilities.PLAYER_RECIPE_DATA.readNBT(this.recipeData, null, pNbt);
    }
  }

  private static class TileEntityRecipeDataProvider implements ICapabilitySerializable<INBT> {

    final ITileEntityRecipeData recipeData;
    final LazyOptional<ITileEntityRecipeData> capability;

    public TileEntityRecipeDataProvider(ITileEntityRecipeData pRecipeData) {
      this.recipeData = pRecipeData;
      this.capability = LazyOptional.of(() -> this.recipeData);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> pCapability,
                                             @Nullable Direction pDirection) {
      return PolymorphCapabilities.TILE_ENTITY_RECIPE_DATA.orEmpty(pCapability, this.capability);
    }

    @Override
    public INBT serializeNBT() {
      return PolymorphCapabilities.TILE_ENTITY_RECIPE_DATA.writeNBT(this.recipeData, null);
    }

    @Override
    public void deserializeNBT(INBT pNbt) {
      PolymorphCapabilities.TILE_ENTITY_RECIPE_DATA.readNBT(this.recipeData, null, pNbt);
    }
  }
}
