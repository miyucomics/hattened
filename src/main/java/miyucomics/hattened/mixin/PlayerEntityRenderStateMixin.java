package miyucomics.hattened.mixin;

import miyucomics.hattened.misc.PlayerEntityRenderStateMinterface;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntityRenderState.class)
class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateMinterface {
	@Unique
	boolean isUsingHat = false;

	@Override
	public void setIsUsingHat(boolean using) {
		isUsingHat = using;
	}

	@Override
	public boolean isUsingHat() {
		return isUsingHat;
	}
}