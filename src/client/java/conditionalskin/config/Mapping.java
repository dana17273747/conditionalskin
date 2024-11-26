package conditionalskin.config;

import conditionalskin.ConditionalSkin;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.gson.Gson;

public class Mapping {
    public Map<String, Intermediary> mapping;
    private MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

    public static Mapping load() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream is = classLoader.getResourceAsStream("mapping.json");
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(reader, Mapping.class);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            ConditionalSkin.LOGGER.info(sw.toString());
            return null;
        }
    }

    public String map(String name) {
        Intermediary i = this.mapping.get(name);
        if (i.type.equals("FIELD")) {
            String ret = resolver.mapFieldName("intermediary", i.intermediaryClassName, i.intermediaryName, "Z");
            ConditionalSkin.LOGGER.info(name + ": " + i.intermediaryName + ": " + ret);
            return ret;
        } else if (i.type.equals("METHOD")) {
            String ret = resolver.mapMethodName("intermediary", i.intermediaryClassName, i.intermediaryName, "()Z");
            ConditionalSkin.LOGGER.info(name + ": " + i.intermediaryName + ": " + ret);
            return ret;
        }
        return "";
    }

    public static class Intermediary {
        public String intermediaryClassName;
        public String intermediaryName;
        public String type;
    }
}
