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
@Table(name = "JFET")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Jfet extends BaseComponent {

    @Column(name = "V(BR)GSS Min (V)")
    private Integer vbrgssMin;

    @Column(name = "IDSS Min (mA)")
    private Float idssMin;

    @Column(name = "IDSS Max (mA)")
    private Float idssMax;

    @Column(name = "Channel Polarity")
    private String channelPolarity;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Ciss Max (pF)")
    private Float cissMax;

    @Column(name = "Crss Max (pF)")
    private Float crssMax;

    @Column(name = "Qualification")
    private String qualification;

    @Override
    public String getAttributesAsString() {
        return "MAXIMUM - Price, MINIMUM - Price, " +
                "MAXIMUM - V(BR)GSS Min (V), MINIMUM - V(BR)GSS Min (V), " +
                "MAXIMUM - IDSS Min (mA), MINIMUM - IDSS Min (mA), " +
                "MAXIMUM - IDSS Max (mA), MINIMUM - IDSS Max (mA), " +
                "Channel Polarity, Package Type, " +
                "MAXIMUM - Ciss Max (pF), MINIMUM - Ciss Max (pF), " +
                "MAXIMUM - Crss Max (pF), MINIMUM - Crss Max (pF), " +
                "Qualification, AEC Qualified, PPAP Capable, " +
                "Halide Free, Lead Free, Status";
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
                "V(BR)GSS Min (V)",
                "IDSS Min (mA)",
                "IDSS Max (mA)",
                "Channel Polarity",
                "Package Type",
                "Ciss Max (pF)",
                "Crss Max (pF)",
                "Qualification"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.JFET;
    }
}

