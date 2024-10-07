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
@Table(name = "PIM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pim extends BaseComponent {

    @Column(name = "PIM Technology")
    private String pimTechnology;

    @Column(name = "VBR Max (V)")
    private Integer vbrMax;

    @Column(name = "IOUT Max (A)")
    private Float ioutMax;

    @Column(name = "VCE(sat) Typ (V)")
    private Float vceSatTyp;

    @Column(name = "RDS(on) Typ (mΩ)")
    private Float rdsOnTyp;

    @Column(name = "VF Typ (V)")
    private Float vfTyp;

    @Column(name = "Configuration")
    private String configuration;

    @Column(name = "Application")
    private String application;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "OPN, MINIMUM - Price, MAXIMUM - Price, Description, PIM Technology, " +
                "MINIMUM - VBR Max (V), MAXIMUM - VBR Max (V), " +
                "MINIMUM - IOUT Max (A), MAXIMUM - IOUT Max (A), " +
                "MINIMUM - VCE(sat) Typ (V), MAXIMUM - VCE(sat) Typ (V), " +
                "MINIMUM - RDS(on) Typ (mΩ), MAXIMUM - RDS(on) Typ (mΩ), " +
                "MINIMUM - VF Typ (V), MAXIMUM - VF Typ (V), " +
                "Configuration, Application, Package Type, " +
                "Qualification, AEC Qualified, PPAP Capable, Halide Free, Lead Free";
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
                "PIM Technology",
                "VBR Max (V)",
                "IOUT Max (A)",
                "VCE(sat) Typ (V)",
                "RDS(on) Typ (mΩ)",
                "VF Typ (V)",
                "Configuration",
                "Application",
                "Package Type",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.PIM;
    }
}

