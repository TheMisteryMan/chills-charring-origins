package misteryman.chillscharringorigins.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import misteryman.chillscharringorigins.common.ChillsCharringOrigins;
import misteryman.chillscharringorigins.config.ConfigFoodItem;
import misteryman.chillscharringorigins.config.ModConfig;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public abstract class ItemsMixin {
	@Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;")
	private static void modifyRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {

		if(!ChillsCharringOrigins.configRegistered) {
			AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
			ChillsCharringOrigins.configRegistered = true;
		}
		ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
		for(int i = 0; i < config.hot_food.size(); i++) {
			ConfigFoodItem currentItem = config.hot_food.get(i);
			if (id.equals(new Identifier(currentItem.itemId))) {
				((ItemAccessorMixin) item).setFoodComponent(new FoodComponent.Builder()
					.hunger(currentItem.hungerShanks)
					.saturationModifier(currentItem.saturation)
					.build());
			}
		}
	}
}
