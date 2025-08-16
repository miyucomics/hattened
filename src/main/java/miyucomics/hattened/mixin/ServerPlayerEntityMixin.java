package miyucomics.hattened.mixin;

import miyucomics.hattened.abilities.Ability;
import miyucomics.hattened.structure.HattenedHelper;
import miyucomics.hattened.structure.ServerPlayerEntityMinterface;
import miyucomics.hattened.structure.UserInput;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityMinterface {
	@Unique
	private final Queue<UserInput> hattened$inputQueue = new ConcurrentLinkedQueue<>();
	@Unique
	private final Queue<Ability> hattened$proposedAdditions = new ConcurrentLinkedQueue<>();

	@Override
	public void hattened$queueUserInput(@NotNull UserInput input) {
		hattened$inputQueue.add(input);
	}

	@Override
	public void hattened$proposeAbility(@NotNull Ability ability) {
		hattened$proposedAdditions.add(ability);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		HattenedHelper.mutateHat((ServerPlayerEntity) (Object) this, hattened$inputQueue, hattened$proposedAdditions);
	}
}