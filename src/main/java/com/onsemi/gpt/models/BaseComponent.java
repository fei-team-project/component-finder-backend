package com.onsemi.gpt.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseComponent {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "Product (WPN)")
    private String productWPN;

    @Column(name = "Datasheet")
    private String datasheet;

    @Column(name = "OPN")
    private String opn;

    @Column(name = "Price")
    private Float price;

    @Column(name = "Description", length = 512)
    private String description;

    @Column(name = "AEC Qualified")
    private Boolean aecQualified;

    @Column(name = "PPAP Capable")
    private Boolean ppapCapable;

    @Column(name = "Halide Free")
    private Boolean halideFree;

    @Column(name = "Lead Free")
    private Boolean leadFree;

    @Column(name = "Status")
    private String status;

    protected abstract Component getComponent();

    protected abstract String getAttributesAsString();

    protected abstract List<String> getColumns();
}
