package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Component;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class PlayerWarpLocationComponent implements Component<EntityStore> {
    public static final BuilderCodec<PlayerWarpLocationComponent> CODEC = BuilderCodec.builder(
        PlayerWarpLocationComponent.class,
        PlayerWarpLocationComponent::new
    )
    .addField(new KeyedCodec<>("WarpData",new MapCodec<PlayerWarpData,HashMap<String,PlayerWarpData>>(PlayerWarpData.CODEC, () -> new HashMap<>())), (component, map) -> component.data = new HashMap<>(map), component -> component.data)
    .build();
    
    private HashMap<String,PlayerWarpData> data;

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    @Override
    public @Nullable Component<EntityStore> clone() {
        PlayerWarpLocationComponent clone = new PlayerWarpLocationComponent();
        clone.data = new HashMap<String,PlayerWarpData>();
        for (Map.Entry<String, PlayerWarpData> entry : this.data.entrySet()) {
            clone.data.put(entry.getKey(), entry.getValue().clone());
        }
        return clone;
    }

    public PlayerWarpLocationComponent(){
    }
    
    public PlayerWarpLocationComponent(Ref<EntityStore> player, BlockPosition blockPosition, String key){
        Store<EntityStore> playerStore = player.getStore();
        TransformComponent transformComp = playerStore.getComponent(player, TransformComponent.getComponentType());
        if(transformComp == null) return;
        Vector3d location = transformComp.getTransform().getPosition();
        
        if(data == null){
            data = new HashMap<String,PlayerWarpData>();
        }
        
        PlayerWarpData warpData;
        if(data.containsKey(key)) {
            warpData = data.get(key);
        }else {
            warpData = new PlayerWarpData();
        }
        
        warpData.location = location.clone();
        HeadRotation headRotation = (HeadRotation)playerStore.getComponent(player, HeadRotation.getComponentType());
        Vector3f rotation = headRotation != null ? headRotation.getRotation() : new Vector3f(0.0F, 0.0F, 0.0F);
        warpData.rotation = rotation.clone();
        
        warpData.blockLocation = new Vector3i(blockPosition.x,blockPosition.y,blockPosition.z);
        
        warpData.world = playerStore.getExternalData().getWorld().getName();
        
        data.put(key,warpData);
    }
    
    public PlayerWarpLocationComponent(PlayerWarpLocationComponent other){

        this.data = new HashMap<String,PlayerWarpData>();
        for (Map.Entry<String, PlayerWarpData> entry : other.data.entrySet()) {
            this.data.put(entry.getKey(), entry.getValue().clone());
        }
    }
    
    public void ChangeWarp(Ref<EntityStore> player, BlockPosition blockPosition, String key){

        if(data == null){
            data = new HashMap<String,PlayerWarpData>();
        }
        
        Store<EntityStore> playerStore = player.getStore();
        TransformComponent transformComp = playerStore.getComponent(player, TransformComponent.getComponentType());
        if(transformComp == null) return;
        Vector3d location = transformComp.getTransform().getPosition();

        PlayerWarpData warpData;
        if(data.containsKey(key)) {
            warpData = data.get(key);
        }else {
            warpData = new PlayerWarpData();
        }
        
        warpData.location = location.clone();
        HeadRotation headRotation = (HeadRotation)playerStore.getComponent(player, HeadRotation.getComponentType());
        Vector3f rotation = headRotation != null ? headRotation.getRotation() : new Vector3f(0.0F, 0.0F, 0.0F);
        warpData.rotation = rotation.clone();

        warpData.blockLocation = new Vector3i(blockPosition.x,blockPosition.y,blockPosition.z);
        
        warpData.world = playerStore.getExternalData().getWorld().getName();
        data.put(key,warpData);
    }
    
    public boolean hasWarp(String key){
        return data.containsKey(key);
    }
    
    public Vector3d getLocation(String key){
        return data.get(key).location.clone();
    }
    
    public BlockPosition getBlockLocation(String key){
        Vector3i position = data.get(key).blockLocation;
        if(position == null) return null;
        BlockPosition blockPosition = new BlockPosition(position.x, position.y, position.z);
        
        return blockPosition;
    }
    
    public Vector3f getRotation(String key){
        return data.get(key).rotation.clone();
    }
    
    public World getWorld(String key){
        return Universe.get().getWorld(data.get(key).world);
    }
}

