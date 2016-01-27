package br.com.oliversys.babettenosalao.valueobject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;

import br.com.oliversys.babettenosalao.Constants;
import br.com.oliversys.mobilecommons.IValueObject;
import br.com.oliversys.mobilecommons.Utils;


/**
 * Created by William on 6/19/2015.
 */
public class PerfilUsuarioVO implements IValueObject, Parcelable {

    // para fotos salvas pelo app
    private Bitmap foto;
    private String nomeRua;
    private String numRua;
    private String complemento;
    private String cidade;
    private String estado;
    private String bairro;
    private String cep;

    // campos do facebook. nomes n podem ser alterados por causa do parser json
    private String name;
    private String birthday;
    private String email;
    private String gender;
    private String id;

    // url da foto do Facebook.
    // Nao eh deserializado pelo Gson pq eh um JsonObject aninhado no JSONObject retornado pelo Facebook.
    private String fotoURL;

    public PerfilUsuarioVO(){}

    public PerfilUsuarioVO(Person p){
        this.name = p.getName().getGivenName();
        this.birthday = p.getBirthday();
    }

    public PerfilUsuarioVO(Bundle b){
        fotoURL = b.getString(Constants.FOTO_URL_FACEBOOK);
        id = b.getString(Constants.ID_FACEBOOK);
        gender = b.getString(Constants.SEXO);
        name = b.getString(Constants.NOME);
        birthday = b.getString(Constants.DT_NASCIMENTO);
        nomeRua = b.getString(Constants.NOME_RUA);
        numRua = b.getString(Constants.NUM_RUA);
        complemento = b.getString(Constants.COMPLEMENTO);
        cidade = b.getString(Constants.CIDADE);
        estado = b.getString(Constants.ESTADO);
        bairro = b.getString(Constants.BAIRRO);
        cep = b.getString(Constants.CEP);
        email = b.getString(Constants.EMAIL);
    }

    protected PerfilUsuarioVO(Parcel in) {
        fotoURL = in.readString();
        id = in.readString();
        gender = in.readString();
        foto = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        name = in.readString();
        birthday = in.readString();
        nomeRua = in.readString();
        numRua = in.readString();
        complemento = in.readString();
        cidade = in.readString();
        estado = in.readString();
        bairro = in.readString();
        cep = in.readString();
        email = in.readString();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        String dataConvertida = Utils.toBrazilianDate(birthday);
        this.birthday = dataConvertida;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }

    public String getNumRua() {
        return numRua;
    }

    public void setNumRua(String numRua) {
        this.numRua = numRua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoURL() {
        return fotoURL;
    }

    public void setFotoURL(String fotoURL) {
        this.fotoURL = fotoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(foto);
        dest.writeString(name);
        dest.writeString(birthday);
        dest.writeString(nomeRua);
        dest.writeString(numRua);
        dest.writeString(complemento);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(bairro);
        dest.writeString(cep);
        dest.writeString(email);
        dest.writeString(fotoURL);
        dest.writeString(id);
        dest.writeString(gender);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PerfilUsuarioVO> CREATOR = new Parcelable.Creator<PerfilUsuarioVO>() {
        @Override
        public PerfilUsuarioVO createFromParcel(Parcel in) {
            return new PerfilUsuarioVO(in);
        }

        @Override
        public PerfilUsuarioVO[] newArray(int size) {
            return new PerfilUsuarioVO[size];
        }
    };

    @Override
    public String toString() {
        return "PerfilUsuarioVO{" +
                ", foto=" + foto +
                ", nomeRua='" + nomeRua + '\'' +
                ", numRua='" + numRua + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                ", fotoURL='" + fotoURL + '\'' +
                '}';
    }
}

