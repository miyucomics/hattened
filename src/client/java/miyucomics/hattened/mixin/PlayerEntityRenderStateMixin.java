package miyucomics.hattened.mixin;

import miyucomics.hattened.attach.HatDataAttachment;
import miyucomics.hattened.misc.HatDataSmuggler;
import miyucomics.hattened.structure.HatPose;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
@Mixin(PlayerEntityRenderState.class)
class PlayerEntityRenderStateMixin implements HatDataSmuggler {
	@Unique HatDataAttachment hat = HatDataAttachment.DEFAULT;
	@Override public void setHat(@NotNull HatDataAttachment hat) {this.hat = hat;}
	@Override public @NotNull HatDataAttachment getHat() {return this.hat;}

	@Unique HatPose pose = HatPose.OnHead;
	@Override public void setPose(@NotNull HatPose hat) {this.pose = hat;}
	@Override public @NotNull HatPose getPose() {return this.pose;}
}