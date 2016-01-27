package br.com.oliversys.babettenosalao;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class JsonObjAninExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(IJsonObjectAninhado.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
