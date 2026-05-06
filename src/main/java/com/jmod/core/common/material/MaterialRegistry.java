package com.jmod.core.common.material;

import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {
    private final List<Material> materials;

    public MaterialRegistry(){
        this.materials = new ArrayList<>();
    }

    public void register(Material material){
        this.materials.add(material);
    }

    public List<Material> toList(){
        return this.materials;
    }

    public int size(){
        return this.materials.size();
    }
}
