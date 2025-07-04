package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.fluid.MultiFluidTank;
import com.indref.industrial_reforged.content.recipes.WoodenBasinRecipe;
import com.indref.industrial_reforged.content.recipes.recipeInputs.ItemFluidRecipeInput;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Optional;

public class WoodenBasinBlockEntity extends IRContainerBlockEntity {
    private final MultiFluidTank multiFluidTank;
    private WoodenBasinRecipe recipe;
    private float duration;

    public WoodenBasinBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.WOODEN_BASIN.get(), blockPos, blockState);
        addItemHandler(1);
        this.multiFluidTank = new MultiFluidTank(2, t -> 4000) {
            @Override
            public boolean isFluidValid(int i, FluidStack fluidStack) {
                return i == 0;
            }

            @Override
            protected void onContentsChanged() {
                update();
                onFluidChanged();
            }
        };
    }

    public WoodenBasinRecipe getRecipe() {
        return this.recipe;
    }

    public float getDuration() {
        return duration;
    }

    public float getMaxDuration() {
        return this.recipe != null ? this.recipe.duration() : 0;
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return multiFluidTank;
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);

        calcCacheRecipe();
    }

    @Override
    protected void onFluidChanged() {
        super.onFluidChanged();

        calcCacheRecipe();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        calcCacheRecipe();
    }

    private void calcCacheRecipe() {
        RecipeManager manager = level.getRecipeManager();
        Optional<RecipeHolder<WoodenBasinRecipe>> recipeHolder = manager.getRecipeFor(WoodenBasinRecipe.TYPE, new ItemFluidRecipeInput(getItemHandler().getStackInSlot(0), getFluidHandler().getFluidInTank(0)), level);
        this.recipe = recipeHolder.map(RecipeHolder::value).orElse(null);
    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (this.level.isClientSide() && this.recipe != null) {
            FluidStack fluidStack = this.getFluidHandler().getFluidInTank(0);
            if (!fluidStack.isEmpty() && level.getGameTime() % 10 == 0) {
                IClientFluidTypeExtensions clientFluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
                int tintColor = clientFluidTypeExtensions.getTintColor();

                double x = this.worldPosition.getX() + (this.level.random.nextDouble() - 0.5) / 2 + 0.5;
                double y = this.worldPosition.getY() + ((float) 8 / 16);
                double z = this.worldPosition.getZ() + (this.level.random.nextDouble() - 0.5) / 2 + 0.5;

                double velocityX = (this.level.random.nextDouble() - 0) * 0.06;
                double velocityY = 0.05 + this.level.random.nextDouble() * 2;
                double velocityZ = (this.level.random.nextDouble() - 0) * 0.06;

                float r = FastColor.ARGB32.red(tintColor) / 255f - 0.2f;
                float g = FastColor.ARGB32.green(tintColor) / 255f - 0.2f;
                float b = FastColor.ARGB32.blue(tintColor) / 255f - 0.2f;
                this.level.addParticle(new DustParticleOptions(new Vector3f(r, g, b), 1), x, y, z, velocityX, velocityY, velocityZ);
            }
        }

        if (this.recipe != null) {
            this.duration++;
            setChanged();

            if (this.getDuration() >= this.getMaxDuration()) {
                processRecipe();
            }
        } else {
            this.duration = 0;
        }
    }

    private void processRecipe() {
        MultiFluidTank fluidTank = this.multiFluidTank;
        IItemHandler itemHandler = this.getItemHandler();

        FluidIngredientWithAmount fluidIngredient = this.recipe.fluidIngredient();
        IngredientWithCount ingredient = this.recipe.ingredient();
        FluidStack resultFluid = this.recipe.resultFluid().copy();

        fluidTank.drain(0, fluidIngredient.amount(), new MultiFluidTank.TransferContext(IFluidHandler.FluidAction.EXECUTE));

        // this.recipe is null from here on
        itemHandler.extractItem(0, ingredient.count(), false);

        fluidTank.fill(1, resultFluid, new MultiFluidTank.TransferContext());
        this.duration = 0;
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        if (this.multiFluidTank != null) {
            this.multiFluidTank.deserializeNBT(provider, tag.getCompound("fluid_tank"));
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        if (this.multiFluidTank != null) {
            tag.put("fluid_tank", this.multiFluidTank.serializeNBT(provider));
        }
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return Map.of();
    }
}
