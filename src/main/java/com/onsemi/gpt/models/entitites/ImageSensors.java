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
@Table(name = "IMAGE_SENSORS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageSensors extends BaseComponent {

    @Column(name = "Shutter Type")
    private String shutterType;

    @Column(name = "Megapixels (MP)")
    private Float megapixels;

    @Column(name = "Pixel Size (µm)")
    private Float pixelSize;

    @Column(name = "Frame Rate (fps)")
    private Float frameRate;

    @Column(name = "Optical Format")
    private String opticalFormat;

    @Column(name = "Optical Format (inch)")
    private Float opticalFormatInch;

    @Column(name = "Color")
    private String color;

    @Column(name = "Output Interface")
    private String outputInterface;

    @Column(name = "Package Type")
    private String packageType;

    @Column(name = "Qualification")
    private String qualification;

    @Column(name = "MSL Type")
    private Integer mslType;

    @Column(name = "MSL Temp °C")
    private Integer mslTempC;

    @Override
    public String getAttributesAsString() {
        return "MINIMUM - Price, MAXIMUM - Price, Shutter Type, " +
                "MINIMUM - Megapixels (MP), MAXIMUM - Megapixels (MP), " +
                "MINIMUM - Pixel Size (µm), MAXIMUM - Pixel Size (µm), " +
                "MINIMUM - Frame Rate (fps), MAXIMUM - Frame Rate (fps), " +
                "Optical Format, MINIMUM - Optical Format (inch), MAXIMUM - Optical Format (inch), " +
                "Color, Output Interface, Package Type, " +
                "Qualification, MSL Type, MSL Temp °C, " +
                "AEC Qualified, PPAP Capable, Halide Free, Lead Free";
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
                "Shutter Type",
                "Megapixels (MP)",
                "Pixel Size (µm)",
                "Frame Rate (fps)",
                "Optical Format",
                "Optical Format (inch)",
                "Color",
                "Output Interface",
                "Package Type",
                "Qualification",
                "MSL Type",
                "MSL Temp °C"
        ));
    }

    @Override
    protected Component getComponent() {
        return Component.IMAGE_SENSORS;
    }

}
