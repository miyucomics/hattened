package miyucomics.hattened.mixin;

import miyucomics.hattened.structure.HattenedHelper;
import miyucomics.hattened.structure.ServerPlayerEntityMinterface;
import miyucomics.hattened.structure.UserInput;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityMinterface {
	@Unique private final Queue<UserInput> hattened$inputQueue = new ConcurrentLinkedQueue<>();
	@Override public void hattened$queueUserInput(@NotNull UserInput input) { hattened$inputQueue.add(input); }

	@Unique private final Queue<ItemStack> hattened$proposedAdditions = new ConcurrentLinkedQueue<>();
	@Override public void hattened$proposeItemStack(@NotNull ItemStack stack) { hattened$proposedAdditions.add(stack); }

	@Unique private Optional<Integer> hattened$cooldown = Optional.empty();
	@Override public void hattened$setCooldown(int cooldown) { hattened$cooldown = Optional.of(cooldown); }

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		HattenedHelper.updateHat((ServerPlayerEntity) (Object) this, hattened$inputQueue, hattened$proposedAdditions, hattened$cooldown);
		hattened$cooldown = Optional.empty();
	}
}