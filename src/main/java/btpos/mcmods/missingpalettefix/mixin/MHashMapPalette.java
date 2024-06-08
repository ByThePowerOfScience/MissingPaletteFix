package btpos.mcmods.missingpalettefix.mixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HashMapPalette.class)
public abstract class MHashMapPalette {
    @Redirect(
        method="",
        at=@At(
            target="",
            value=""
        )
    )
    private void foo() {
        
    }
}

