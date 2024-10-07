package com.onsemi.gpt.models.entitites;

import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.Component;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "MOSFET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mosfet extends BaseComponent {

    @Column(name = "Channel Polarity")
    private String channelPolarity;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "V(BR)DSS Min (V)")
    private Integer vbrDSSMin;

    @Column(name = "RDS(on) @ VGS = 10 V (mΩ)†")
    private Float rdsOnVGS10;

    @Column(name = "ID Max (A)")
    private Float idMax;

    @Column(name = "Qg @ VGS = 10 V (nC)†")
    private Float qgVGS10;

    @Column(name = "VGS Max (V)")
    private Integer vgsMax;

    @Column(name = "Technology")
    private String technology;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Ciss Typ (pF)")
    private Integer cissTyp;

    @Column(name = "VGS(th) Max (V)")
    private Float vgsThMax;

    @Column(name = "PD Max (W)")
    private Float pdMax;

    @Column(name = "RDS(on) Max @ VGS = 2.5 V  (mΩ)")
    private Float rdsOnMaxVGS2_5;

    @Column(name = "RDS(on) Max @ VGS = 4.5 V  (mΩ)")
    private Float rdsOnMaxVGS4_5;

    @Column(name = "Qg Typ @ VGS = 4.5 V (nC)")
    private Float qgTypVGS4_5;

    @Column(name = "Coss Typ (pF)")
    private Float cossTyp;

    @Column(name = "Crss Typ (pF)")
    private Float crssTyp;

    @Column(name = "Qgd Typ @ VGS = 4.5 V (nC)")
    private Float qgdTypVGS4_5;

    @Column(name = "Qrr Typ (nC)")
    private Float qrrTyp;

    @Column(name = "Qualification")
    private String qualification;

    @Column(name = "Tj Max (°C)")
    private Integer tjMax;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, Channel Polarity, Configuration, MINIMUM - V(BR)DSS Min (V)," +
                "MAXIMUM - V(BR)DSS Min (V), MINIMUM - RDS(on) @ VGS = 10 V (mΩ)†,MAXIMUM - RDS(on) @ VGS = 10 V (mΩ)†, " +
                "MINIMUM - ID Max (A), MAXIMUM - ID Max (A), MINIMUM - Qg @ VGS = 10 V (nC)†, " +
                "MAXIMUM - Qg @ VGS = 10 V (nC)†,MINIMUM - VGS Max (V), MAXIMUM - VGS Max (V), Technology, " +
                "Package Type, MINIMUM - Ciss Typ (pF), MAXIMUM - Ciss Typ (pF), MINIMUM - VGS(th) Max (V), " +
                "MAXIMUM - VGS(th) Max (V), MINIMUM - PD Max (W), MAXIMUM - PD Max (W)" +
                "MINIMUM - RDS(on) Max @ VGS = 2.5 V  (mΩ), MAXIMUM - RDS(on) Max @ VGS = 2.5 V  (mΩ)," +
                "MINIMUM - RDS(on) Max @ VGS = 4.5 V  (mΩ), MAXIMUM - RDS(on) Max @ VGS = 4.5 V  (mΩ)," +
                "MINIMUM - Qg Typ @ VGS = 4.5 V (nC), MAXIMUM - Qg Typ @ VGS = 4.5 V (nC)," +
                "MINIMUM - Coss Typ (pF), MAXIMUM - Coss Typ (pF)" +
                "MINIMUM - Crss Typ (pF), MAXIMUM - Crss Typ (pF)" +
                "MINIMUM - Qgd Typ @ VGS = 4.5 V (nC), MAXIMUM - Qgd Typ @ VGS = 4.5 V (nC)" +
                "MINIMUM - Qrr Typ (nC), Qualification, MAXIMUM - Qrr Typ (nC), Qualification" +
                "MINIMUM - Tj Max (°C), MAXIMUM - Tj Max (°C)" +
                "AEC Qualified, PPAP Capable, Halide Free, Lead Free, Status";
    }

    @Override
    public List<String> getColumns(){
        return new ArrayList<>(List.of(
                "Product (WPN)",
                "Datasheet",
                "OPN",
                "Price",
                "Description",
                "AEC Qualified",
                "PPAP Capable",
                "Halide Free",
                "Lead Free",
                "Status",
                "Channel Polarity",
                "Configuration",
                "V(BR)DSS Min (V)",
                "RDS(on) @ VGS = 10 V (mΩ)†",
                "ID Max (A)",
                "Qg @ VGS = 10 V (nC)†",
                "VGS Max (V)",
                "Technology",
                "Package Type",
                "Ciss Typ (pF)",
                "VGS(th) Max (V)",
                "PD Max (W)",
                "RDS(on) Max @ VGS = 2.5 V  (mΩ)",
                "RDS(on) Max @ VGS = 4.5 V  (mΩ)",
                "Qg Typ @ VGS = 4.5 V (nC)",
                "Coss Typ (pF)",
                "Crss Typ (pF)",
                "Qgd Typ @ VGS = 4.5 V (nC)",
                "Qrr Typ (nC)",
                "Qualification",
                "Tj Max (°C)"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.MOSFET;
    }
}

