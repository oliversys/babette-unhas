package br.com.oliversys.mobilecommons;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static void showHashKey(Context ctx){
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            for(android.content.pm.Signature s : info.signatures){
                MessageDigest m = MessageDigest.getInstance("SHA");
                m.update(s.toByteArray());
                Log.d("KEY HASH", "KEY HASH: " + Base64.encodeToString(m.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static IValueObject fromJsonObjToVO(String json, Class concreteVoClass){
        IValueObject out = (IValueObject)new Gson().fromJson(json,concreteVoClass);
        return out;
    }

    public static List<?> fromJsonArrayToListVO(String json,Class<?> voAPreencherClass){
        GsonBuilder b = new GsonBuilder();
        Type tipo = new TypeToken<ArrayList<?>>(){}.getType();
        List<?> listaVO = (ArrayList<IValueObject>) b.create().fromJson(json,tipo);
        return listaVO;
    }

    public static String getElementFromJson(String json,String elementName)
    {
        String out = null;
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        if (jsonObject.has(elementName)) {
            out = jsonObject.get(elementName).getAsString();
        }
        return out;
    }

    public static String toBrazilianDate(String dt){
        Locale brasil = new Locale("pt","BR");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, brasil);
        if (!Locale.getDefault().equals(brasil)) {
            try {
                Date data = df.parse(dt); //  6/19/1983
                String dataConvertida = df.format(data); //  19/6/1983
                return dataConvertida;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void putStringOnSharedPrefs(Context ctx, String name, String value) {
        SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("PADRAO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String getFromSharedPrefs(Context ctx,String propName){
        SharedPreferences settings = ctx.getSharedPreferences("PADRAO", Context.MODE_PRIVATE);
        return settings.getString(propName,"");
    }

    public static String toJson(IValueObject obj)
    {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(obj);
        return jsonStr;
    }

    public static JSONObject toJsonFile(IValueObject obj){
        String jsonStr = toJson(obj);
        try {
            return new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(16)
    public static void animate(View v){
        LayoutTransition transition = new LayoutTransition();
//        ObjectAnimator a1 = ObjectAnimator.ofFloat(null, View.SCALE_X, 0, 1);
//        ObjectAnimator a2 = ObjectAnimator.ofFloat(null, View.SCALE_Y, 0, 1);
//        ObjectAnimator a3 = ObjectAnimator.ofFloat(null, View.ALPHA, 0, 1);

//        AnimatorSet animator=  new AnimatorSet();
//        animator.setStartDelay(0);
//        animator.playTogether(a1, a2, a3);
//
//        transition.setAnimator(LayoutTransition.APPEARING, animator);
//        transition.setStagger(LayoutTransition.CHANGE_APPEARING, 1000);
//        transition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 1000);
//        transition.setStagger(LayoutTransition.APPEARING, 1000);
//        transition.setStagger(LayoutTransition.DISAPPEARING, 1000);

        transition.enableTransitionType(LayoutTransition.CHANGING);
        transition.setDuration(5000);

        ((FrameLayout)v).setLayoutTransition(transition);
    }

//    public static void animate(View v){
//        if (v == null) return;
//
//        SlideInAnimation e = new SlideInAnimation(v);
//        e.setDuration(1000);
//        e.setDirection(Animation.DIRECTION_RIGHT);
//
////        e.setListener(new Animation.AnimationListener() {
////            @Override
////            public void onAnimationEnd(Animation animation) {
////            }
////        });
//        e.animate();
//    }

    public static Dialog showProgressDialog(Context ctx){
        ProgressDialog progress = new ProgressDialog(ctx, ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
//        progress.setMessage(ctx.getString(R.string.aguarde));
        progress.show();

        return progress;
    }
}
