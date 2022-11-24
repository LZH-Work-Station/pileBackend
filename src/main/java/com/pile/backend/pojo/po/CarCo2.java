package com.pile.backend.pojo.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("car_co2")
@Data
public class CarCo2 {
    @TableId(type = IdType.AUTO)
    private int id;

    @TableField(value = "marque")
    private String marque;

    @TableField(value = "modele_dossier")
    private String modeleDossier;

    @TableField(value = "modele_commercial")
    private String modeleCommercial;

    @TableField(value = "designation_commerciale")
    private String designationCommerciale;

    @TableField(value = "cnit")
    private String cnit;

    @TableField(value = "tvv")
    private String tvv;

    @TableField(value = "carburant")
    private String carburant;

    @TableField(value = "hybride")
    private String hybride;

    @TableField(value = "puissance_administrative")
    private int puissanceAdministrative;

    @TableField(value = "puissance_maximale")
    private double puissanceMaximale;

    @TableField(value = "boite_de_vitesse")
    private String boiteDeVitesse;

    @TableField(value = "consommation_urbaine")
    private double consommationUrbaine;

    @TableField(value = "consommation_extra_urbaine")
    private double consommationExtraUrbaine;

    @TableField(value = "consommation_mixte")
    private double consommationMixte;

    @TableField(value = "co2")
    private double co2;

    @TableField(value = "co_type_I")
    private double coTypeI;

    @TableField(value = "hc")
    private double hc;

    @TableField(value = "nox")
    private double nox;

    @TableField(value = "hc_nox")
    private double hcNox;

    @TableField(value = "particules")
    private double particules;

    @TableField(value = "masse_vide_euro_min")
    private int masseVideEuroMin;

    @TableField(value = "masse_vide_euro_max")
    private int masseVideEuroMax;

    @TableField(value = "champ_v9")
    private String champV9;

    @TableField(value = "annee")
    private String annee;

    @TableField(value = "carrosserie")
    private String carrosserie;

    @TableField(value = "gamme")
    private String gamme;
}
