package conditionalskin;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import conditionalskin.config.ConditionJudger;
import conditionalskin.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ConditionalSkinClient implements ClientModInitializer {

	public static final Path configPath = FabricLoader.getInstance().getConfigDir()
			.resolve(ConditionalSkin.MOD_ID + ".json");
	private static final Map<UUID, Identifier> playersSkin = new HashMap<>();

	private Config config;
	private ConditionJudger judger;

	private Identifier getConditionalSkin(ClientPlayerEntity player) {
		for (List<String> condition : config.getConditions()) {
			if (judger.judge(player, condition)) {
				return config.getSkinByCondition(condition);
			}
		}
		return null;
	}

	public static Identifier getCurrentSkinOfPlayer(UUID player) {
		return playersSkin.get(player);
	}

	@Override
	public void onInitializeClient() {
		config = Config.load(configPath);
		judger = new ConditionJudger();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player != null) {
				Identifier skin = getConditionalSkin(player);
				playersSkin.put(player.getUuid(), skin);
			}
		});
	}

}