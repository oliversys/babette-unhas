package br.com.oliversys.babettenosalao.valueobject.quiz;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import babette.oliversys.com.br.babettenosalao.R;
import br.com.oliversys.babettenosalao.QuizActivity;
import br.com.oliversys.mobilecommons.IValueObject;
import br.com.oliversys.mobilecommons.Utils;

/**
 *  Representa uma pergunta do quiz, com seu enunciado e suas respostas.
 *
 *  NAO SERIALIZA A PROPRIEDADE rootContainer PQ A CLASSE VIEW NAO IMPLEMENTA SERIALIZABLE
 */
public class PerguntaVO implements Parcelable,IValueObject {
    private String enunciado;
    private List<String> respostas;
    private String textoRadioSelecionado;

    public PerguntaVO(String enunciado, List<String> radioAnswers){
        this.enunciado = enunciado;
        this.respostas = radioAnswers;
    }

    public PerguntaVO(){}

    public View gerarViewFromVO(int numPerguntaAtual,View root,Activity a){
/***************** <TextView> ****************************/
        final TextView enunciadoView = (TextView) root.findViewById(R.id.enunciado);
        enunciadoView.setText(this.getEnunciado());
/**************** <RadioGroup> *************************/
        final RadioGroup respostasRadioGroup = (RadioGroup)root.findViewById(R.id.radioGroupRespostas);
/**** CRIA OS RADIO BUTTONS  ******************************/
        if (respostasRadioGroup != null)
            this.addRadiosFromStringArray(a, respostasRadioGroup, numPerguntaAtual);
        // inicializa a primeira resposta como padrao
        ((RadioButton)respostasRadioGroup.getChildAt(0)).setChecked(true);
/** PREPARA P/ ANIMACAO **/
        LayoutTransition lt = new LayoutTransition();
        lt.setDuration(1000);
        ((FrameLayout)root).setLayoutTransition(lt);
        Utils.animate(root);
        return root;
    }

    public void addRadiosFromStringArray(final Activity quizActv,RadioGroup radioGroup,final int numPerguntaAtual){
        if (this.respostas != null){
            for(String s:this.respostas){
                RadioButton r = new RadioButton(quizActv);
                r.setText(s);
                r.setTextSize(16);
                radioGroup.addView(r);

                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((QuizActivity) quizActv).onQuestionAnswered
                                (((RadioButton) v).getText().toString(), PerguntaVO.this.enunciado, numPerguntaAtual);
                    }
                });
            }
        }
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getRespostas() {
        return respostas;
    }

    public String getTextoRadioSelecionado() {
        return textoRadioSelecionado;
    }

    public void setTextoRadioSelecionado(String t) {
        this.textoRadioSelecionado = t;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public void setRespostas(List<String> respostas) {
        this.respostas = respostas;
    }

    @Override
    public String toString() {
        return "Pergunta{" +
                "enunciado=" + enunciado +
                ", respostas=" + respostas.toString() +
                ", resposta selecionada=" + textoRadioSelecionado +
                '}';
    }

    protected PerguntaVO(Parcel in) {
        enunciado = in.readString();
        if (in.readByte() == 0x01) {
            respostas = new ArrayList<String>();
            in.readList(respostas, String.class.getClassLoader());
        } else {
            respostas = null;
        }
        textoRadioSelecionado = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enunciado);
        if (respostas == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(respostas);
        }
        dest.writeString(textoRadioSelecionado);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PerguntaVO> CREATOR = new Parcelable.Creator<PerguntaVO>() {
        @Override
        public PerguntaVO createFromParcel(Parcel in) {
            return new PerguntaVO(in);
        }

        @Override
        public PerguntaVO[] newArray(int size) {
            return new PerguntaVO[size];
        }
    };
}