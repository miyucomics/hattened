package miyucomics.hattened.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import miyucomics.hattened.abilities.ItemStackAbility;
import miyucomics.hattened.structure.HattenedHelper;
import miyucomics.hattened.structure.ServerPlayerEntityMinterface;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@WrapOperation(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
	boolean addToHat(PlayerInventory instance, ItemStack stack, Operation<Boolean> original) {
		if (!HattenedHelper.getHatData(instance.player).getUsingHat())
			return original.call(instance, stack);
		((ServerPlayerEntityMinterface) instance.player).hattened$proposeAbility(new ItemStackAbility(stack.copyAndEmpty()));
		return true;
	}
}