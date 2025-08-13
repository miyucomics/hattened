package miyucomics.hattened.mixin;

import miyucomics.hattened.HattenedHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	void tick(CallbackInfo ci) {
		HattenedHelper.getHatData((ServerPlayerEntity) (Object) this).tick((ServerPlayerEntity) (Object) this);
	}
}
