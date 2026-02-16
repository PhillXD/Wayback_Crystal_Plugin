package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerWarpBackLocationComponent implements Component<EntityStore> {
    public static final BuilderCodec<PlayerWarpBackLocationComponent> CODEC = BuilderCodec.builder(
                    PlayerWarpBackLocationComponent.class,
                    PlayerWarpBackLocationComponent::new
            )
            .addField(new KeyedCodec<>("WarpData",new MapCodec<PlayerWarpBackData, HashMap<String,PlayerWarpBackData>>(PlayerWarpBackData.CODEC, () -> new HashMap<>())), (component, map) -> component.data = new HashMap<>(map), component -> component.data)
            .build();

    private HashMap<String,PlayerWarpBackData> data;

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    @Override
    public @Nullable Component<EntityStore> clone() {
        PlayerWarpBackLocationComponent clone = new PlayerWarpBackLocationComponent();
        if(this.data != null) {
            clone.data = new HashMap<String, PlayerWarpBackData>();
            for (Map.Entry<String, PlayerWarpBackData> entry : this.data.entrySet()) {
                clone.data.put(entry.getKey(), entry.getValue().clone());
            }
        }
        return clone;
    }

    public PlayerWarpBackLocationComponent(){
        data = new HashMap<String,PlayerWarpBackData>();
    }

    public PlayerWarpBackLocationComponent(Ref<EntityStore> player, String key){
        Store<EntityStore> playerStore = player.getStore();
        TransformComponent transformComp = playerStore.getComponent(player, TransformComponent.getComponentType());
        if(transformComp == null) return;
        Vector3d location = transformComp.getTransform().getPosition();

        if(data == null){
            data = new HashMap<String,PlayerWarpBackData>();
        }

        PlayerWarpBackData warpData;
        if(data.containsKey(key)) {
            warpData = data.get(key);
        }else {
            warpData = new PlayerWarpBackData();
        }

        warpData.location = location.clone();
        HeadRotation headRotation = (HeadRotation)playerStore.getComponent(player, HeadRotation.getComponentType());
        Vector3f rotation = headRotation != null ? headRotation.getRotation() : new Vector3f(0.0F, 0.0F, 0.0F);
        warpData.rotation = rotation.clone();

        warpData.world = playerStore.getExternalData().getWorld().getName();

        data.put(key,warpData);
    }

    public PlayerWarpBackLocationComponent(PlayerWarpBackLocationComponent other){

        this.data = new HashMap<String,PlayerWarpBackData>();
        if(other.data != null) {
            for (Map.Entry<String, PlayerWarpBackData> entry : other.data.entrySet()) {
                this.data.put(entry.getKey(), entry.getValue().clone());
            }
        }
    }

    public void ChangeWarp(Ref<EntityStore> player, String key){

        if(data == null){
            data = new HashMap<String,PlayerWarpBackData>();
        }

        Store<EntityStore> playerStore = player.getStore();
        TransformComponent transformComp = playerStore.getComponent(player, TransformComponent.getComponentType());
        if(transformComp == null) return;
        Vector3d location = transformComp.getTransform().getPosition();

        PlayerWarpBackData warpData;
        if(data.containsKey(key)) {
            warpData = data.get(key);
        }else {
            warpData = new PlayerWarpBackData();
        }

        warpData.location = location.clone();
        HeadRotation headRotation = (HeadRotation)playerStore.getComponent(player, HeadRotation.getComponentType());
        Vector3f rotation = headRotation != null ? headRotation.getRotation() : new Vector3f(0.0F, 0.0F, 0.0F);
        warpData.rotation = rotation.clone();

        warpData.world = playerStore.getExternalData().getWorld().getName();
        data.put(key,warpData);
    }

    public boolean hasWarp(String key){
        if (data == null) data = new HashMap<>();
        return data.containsKey(key);
    }

    @Nullable
    public Vector3d getLocation(String key){
        if (data == null || !data.containsKey(key)) return null;
        return data.get(key).location.clone();
    }
    
    public Vector3f getRotation(String key){
        if (data == null || !data.containsKey(key)) return new Vector3f(0.0F, 0.0F, 0.0F);
        return data.get(key).rotation.clone();
    }

    @Nullable
    public World getWorld(String key){
        if (data == null || !data.containsKey(key)) return null;
        return Universe.get().getWorld(data.get(key).world);
    }
}
