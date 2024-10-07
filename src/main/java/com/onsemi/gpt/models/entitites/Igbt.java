package com.onsemi.gpt.models.entitites;

import com.onsemi.gpt.models.BaseComponent;
import com.onsemi.gpt.models.Component;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "IGBT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Igbt extends BaseComponent {

    @Column(name = "V(BR)CES Typ (V)")
    private Integer vbrCesTypV;

    @Column(name = "IC Max (A)")
    private Float icMaxA;

    @Column(name = "VCE(sat) Typ (V)")
    private Float vceSatTypV;

    @Column(name = "PD Max (W)")
    private Float pdMaxW;

    @Column(name = "Co-Packaged Diode")
    private Boolean coPackagedDiode;

    @Column(name = "Short Circuit Withstand (µs)")
    private Integer shortCircuitWithstandUs;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "VF Typ (V)")
    private Float vfTypV;

    @Column(name = "Eon Typ (mJ)")
    private Float eonTypMJ;

    @Column(name = "Eoff Typ (mJ)")
    private Float eoffTypMJ;

    @Column(name = "Trr Typ (ns)")
    private Float trrTypNs;

    @Column(name = "Irr Typ (A)")
    private String irrTypA;

    @Column(name = "QG Typ (nC)")
    private Float qgTypnC;

    @Column(name = "EAS Typ (mJ)")
    private Integer easTypMJ;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "OPN, MINIMUM - Price, MAXIMUM - Price, " +
                "MINIMUM - V(BR)CES Typ (V), MAXIMUM - V(BR)CES Typ (V), " +
                "MINIMUM - IC Max (A), MAXIMUM - IC Max (A), " +
                "MINIMUM - VCE(sat) Typ (V), MAXIMUM - VCE(sat) Typ (V), " +
                "MINIMUM - PD Max (W), MAXIMUM - PD Max (W), Co-Packaged Diode, " +
                "MINIMUM - Short Circuit Withstand (µs), MAXIMUM - Short Circuit Withstand (µs), " +
                "Package Type, MINIMUM - VF Typ (V), MAXIMUM - VF Typ (V), " +
                "MINIMUM - Eon Typ (mJ), MAXIMUM - Eon Typ (mJ), " +
                "MINIMUM - Eoff Typ (mJ), MAXIMUM - Eoff Typ (mJ), " +
                "MINIMUM - Trr Typ (ns), MAXIMUM - Trr Typ (ns), " +
                "Irr Typ (A), MINIMUM - QG Typ (nC), MAXIMUM - QG Typ (nC), " +
                "MAXIMUM - EAS Typ (mJ), MINIMUM - EAS Typ (mJ), " +
                "Qualification, AEC Qualified, PPAP Capable, Halide Free, Lead Free, Status";
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
                "V(BR)CES Typ (V)",
                "IC Max (A)",
                "VCE(sat) Typ (V)",
                "PD Max (W)",
                "Co-Packaged Diode",
                "Short Circuit Withstand (µs)",
                "Package Type",
                "VF Typ (V)",
                "Eon Typ (mJ)",
                "Eoff Typ (mJ)",
                "Trr Typ (ns)",
                "Irr Typ (A)",
                "QG Typ (nC)",
                "EAS Typ (mJ)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.IGBT;
    }
}

