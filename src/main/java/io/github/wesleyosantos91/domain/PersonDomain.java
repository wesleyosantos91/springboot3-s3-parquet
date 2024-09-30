package io.github.wesleyosantos91.domain;

public class PersonDomain {
    private String id;
    private String nome;
    private Integer codigoInterno;
    private String dataNascimento;
    private String dataHoraCriacaoRegistro;

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCodigoInterno() {
        return codigoInterno;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getDataHoraCriacaoRegistro() {
        return dataHoraCriacaoRegistro;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigoInterno(Integer codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataHoraCriacaoRegistro(String dataHoraCriacaoRegistro) {
        this.dataHoraCriacaoRegistro = dataHoraCriacaoRegistro;
    }
}