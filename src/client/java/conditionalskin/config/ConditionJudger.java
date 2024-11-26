package conditionalskin.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.client.network.ClientPlayerEntity;

public class ConditionJudger {
    private Map<String, Predicate<ClientPlayerEntity>> availableConditions = new HashMap<>();

    public ConditionJudger() {
        Class<?> clazz = ClientPlayerEntity.class;

        while (clazz != null) {

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(boolean.class)) {
                    availableConditions.put(field.getName(), player -> {
                        try {
                            return (boolean) field.get(player);
                        } catch (Exception e) {
                            return false;
                        }
                    });
                }
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getReturnType().equals(boolean.class) && method.getParameterCount() == 0) {
                    availableConditions.put(method.getName(), player -> {
                        try {
                            return (boolean) method.invoke(player);
                        } catch (Exception e) {
                            return false;
                        }
                    });
                }
            }

            clazz = clazz.getSuperclass();
        }

    }

    public boolean judge(ClientPlayerEntity player, List<String> condition) {
        for (String c : condition) {
            Predicate<ClientPlayerEntity> predicate = availableConditions.get(c);
            if (predicate == null || !predicate.test(player)) {
                return false;
            }
        }
        return true;
    }
}
