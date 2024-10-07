package com.onsemi.gpt.models.entitites;

import com.onsemi.gpt.models.BaseComponent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUDIO_AMPLIFIER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AudioAmplifier extends BaseComponent {

    @Column(name = "Product Group")
    private String productGroup;

    @Column(name = "Status")
    private String status;

    @Column(name = "Compliance")
    private String compliance;

    @Column(name = "Class")
    private String amplifierClass;

    @Column(name = "Output Power Typ (W)")
    private Float outputPowerTyp;

    @Column(name = "VCC Max (V)")
    private Float vccMax;

    @Column(name = "Output Type")
    private String outputType;

    @Column(name = "ton Typ (ms)")
    private Float tonTyp;

    @Column(name = "THD + N Typ (%)")
    private Float thdNTyp;

    @Column(name = "IQ Typ (mA)")
    private Float iqTyp;

    @Column(name = "ISD Typ (nA)")
    private Float isdTyp;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "MSL Type")
    private Integer mslType;

    @Column(name = "MSL Temp (°C)")
    private Integer mslTemp;

    @Column(name = "Pricing ($/Unit)")
    private Float pricing;

    public static String getAttributesAsString() {
        return "Product Group, Status, Compliance, Class, Output Power Typ (W), VCC Max (V), " +
                "Output Type, ton Typ (ms), THD + N Typ (%), IQ Typ (mA), ISD Typ (nA), Package Type, " +
                "MSL Type, MSL Temp (°C), Pricing ($/Unit)";
    }


}
