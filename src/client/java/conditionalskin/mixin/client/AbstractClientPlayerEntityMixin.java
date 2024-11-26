package conditionalskin.mixin.client;

import conditionalskin.ConditionalSkinClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(AbstractClientPlayerEntity.class)
@Environment(EnvType.CLIENT)
public class AbstractClientPlayerEntityMixin {
    @Inject(method = "getSkinTexture", at = @At("RETURN"), cancellable = true)
    public void getSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        Identifier skin = ConditionalSkinClient.getCurrentSkinOfPlayer(player.getUuid());
        if (skin != null) {
            cir.setReturnValue(skin);
        }
    }
}
