package conditionalskin.config;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gson.Gson;

import conditionalskin.ConditionalSkin;
import net.minecraft.util.Identifier;

public class Config {
    private Map<String, Identifier> skins = new HashMap<>();
    private Map<List<String>, String> skinConditions = new LinkedHashMap<>();
    private Set<List<String>> conditions;

    public void putSkin(String skinName, String skinPath) {
        try {
            skins.put(skinName, new Identifier(skinPath.toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<List<String>> getConditions() {
        return conditions;
    }

    public Identifier getSkinByCondition(List<String> condition) {
        return skins.get(skinConditions.get(condition));
    }

    public static Config load(Path path) {
        Config config = new Config();
        try {
            Mapping mapping = Mapping.load();
            FileReader reader = new FileReader(path.toFile());
            Gson gson = new Gson();
            RawConfig raw = gson.fromJson(reader, RawConfig.class);

            raw.skins.forEach((skinName, skinPath) -> config.putSkin(skinName, skinPath));
            raw.skinConditions.forEach((skinName, condition) -> {
                List<String> deobfuscatedCondition = condition.stream().map(c -> mapping.map(c)).toList();
                if (!config.skinConditions.containsKey(deobfuscatedCondition)) {
                    config.skinConditions.put(deobfuscatedCondition, skinName);
                }
            });
            config.conditions = config.skinConditions.keySet();
        } catch (Exception e) {
            e.printStackTrace();
            ConditionalSkin.LOGGER.info("failed to load config");
        }
        return config;
    }

    public static class RawConfig {
        public Map<String, String> skins = new HashMap<>();
        public Map<String, List<String>> skinConditions = new LinkedHashMap<>();
    }
}
