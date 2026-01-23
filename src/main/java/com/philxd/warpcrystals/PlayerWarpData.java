package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;

import javax.annotation.Nonnull;

public class PlayerWarpData{
    public static final BuilderCodec<PlayerWarpData> CODEC = BuilderCodec.builder(
            PlayerWarpData.class,
            PlayerWarpData::new
    )
    .addField(new KeyedCodec<>("WarpLocation", Vector3d.CODEC), (Data,value) -> Data.location = value, (Data) -> Data.location)
    .addField(new KeyedCodec<>("WarpRotation", Vector3f.CODEC), (Data,value) -> Data.rotation = value, (Data) -> Data.rotation)
    .addField(new KeyedCodec<>("WarpWorld", Codec.STRING), (Data, value) -> Data.world = value, (Data) -> Data.world)
    .addField(new KeyedCodec<>("WarpBlockLocation", Vector3i.CODEC), (Data,value) -> Data.blockLocation = value, (Data) -> Data.blockLocation)
    .build();
    
    
    public Vector3i blockLocation;
    public Vector3d location;
    public Vector3f rotation;
    public String world;

    @Nonnull
    public PlayerWarpData clone()
    {
        PlayerWarpData clone = new PlayerWarpData();
        clone.location = this.location.clone();
        clone.rotation = this.rotation.clone();
        clone.world = this.world;
        clone.blockLocation = this.blockLocation.clone();
        return clone;
    }
}
