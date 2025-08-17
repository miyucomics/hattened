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
	@Unique private Queue<UserInput> inputQueue;
	@Unique private Queue<ItemStack> proposedAdditions;
	@Unique private Optional<Integer> cooldown;

	@Override public void queueUserInput(@NotNull UserInput input) { this.inputQueue.add(input); }
	@Override public void proposeItemStack(@NotNull ItemStack stack) { this.proposedAdditions.add(stack); }
	@Override public void setCooldown(int cooldown) { this.cooldown = Optional.of(cooldown); }

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(CallbackInfo ci) {
		this.inputQueue = new ConcurrentLinkedQueue<>();
		this.proposedAdditions = new ConcurrentLinkedQueue<>();
		this.cooldown = Optional.empty();
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {
		HattenedHelper.updateHat((ServerPlayerEntity) (Object) this, this.inputQueue, this.proposedAdditions, this.cooldown);
		this.cooldown = Optional.empty();
	}
}