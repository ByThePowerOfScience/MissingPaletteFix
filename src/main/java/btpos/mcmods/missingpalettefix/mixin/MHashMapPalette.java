package btpos.mcmods.missingpalettefix.mixin;

import btpos.mcmods.missingpalettefix.MissingPaletteFix;
import net.minecraft.core.IdMap;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.HashMapPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(HashMapPalette.class)
public abstract class MHashMapPalette<T> {
	
	@Shadow @Final private IdMap<T> registry;
	
	@Unique private boolean missingPaletteFix$isBlockStatePalette = false;
	
	@Inject(
			method={
					"<init>(Lnet/minecraft/core/IdMap;ILnet/minecraft/world/level/chunk/PaletteResize;)V",
					"<init>(Lnet/minecraft/core/IdMap;ILnet/minecraft/world/level/chunk/PaletteResize;Ljava/util/List;)V",
					"<init>(Lnet/minecraft/core/IdMap;ILnet/minecraft/world/level/chunk/PaletteResize;Lnet/minecraft/util/CrudeIncrementalIntIdentityHashBiMap;)V"
			},
			at=@At("RETURN"),
			locals = LocalCapture.NO_CAPTURE
	)
	private void getType(CallbackInfo ci) {
		Iterator<T> iterator = registry.iterator();
		if (iterator.hasNext()) {
			T next = iterator.next();
			missingPaletteFix$isBlockStatePalette = next instanceof BlockState;
		}
		
	}
	
	@Redirect(
			method = "valueFor",
			at = @At(
					target = "Lnet/minecraft/util/CrudeIncrementalIntIdentityHashBiMap;byId(I)Ljava/lang/Object;",
					value = "INVOKE"
			)
	)
	private T foo(CrudeIncrementalIntIdentityHashBiMap<T> instance, int id) {
		T t = instance.byId(id);
		if (t == null && missingPaletteFix$isBlockStatePalette) {
			MissingPaletteFix.LOGGER.warn("Found missing palette entry for ID {}!", id);
			return (T) Blocks.DRAGON_EGG.defaultBlockState();
		}
		return t;
	}
}

