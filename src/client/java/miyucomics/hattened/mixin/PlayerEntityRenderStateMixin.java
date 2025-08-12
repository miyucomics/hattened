package miyucomics.hattened.mixin;

import miyucomics.hattened.attach.HatData;
import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
@Mixin(PlayerEntityRenderState.class)
class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateMinterface {
	@Unique HatData hat = HatData.DEFAULT;

	@Override
	public void setHat(@NotNull HatData hat) {
		this.hat = hat;
	}

	@Override
	public @NotNull HatData getHat() {
		return this.hat;
	}
}