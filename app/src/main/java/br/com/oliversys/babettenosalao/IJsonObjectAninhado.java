package br.com.oliversys.babettenosalao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IJsonObjectAninhado {
    // atributo serializado como JsonObject aninhado no JSONObject retornado pelo Facebook.
}
