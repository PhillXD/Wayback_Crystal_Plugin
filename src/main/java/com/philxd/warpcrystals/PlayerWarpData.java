package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.math.vector.Vector3iUtil;
import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.annotation.Nonnull;

public class PlayerWarpData{
    public static final BuilderCodec<PlayerWarpData> CODEC = BuilderCodec.builder(
            PlayerWarpData.class,
            PlayerWarpData::new
    )
    .addField(new KeyedCodec<>("WarpLocation", Vector3dUtil.CODEC), (Data, value) -> Data.location = value, (Data) -> Data.location)
    .addField(new KeyedCodec<>("WarpRotation", Rotation3f.CODEC), (Data,value) -> Data.rotation = value, (Data) -> Data.rotation)
    .addField(new KeyedCodec<>("WarpWorld", Codec.STRING), (Data, value) -> Data.world = value, (Data) -> Data.world)
    .addField(new KeyedCodec<>("WarpBlockLocation", Vector3iUtil.CODEC), (Data, value) -> Data.blockLocation = value, (Data) -> Data.blockLocation)
    .build();
    
    
    public Vector3i blockLocation;
    public Vector3d location;
    public Rotation3f rotation;
    public String world;

    @Nonnull
    public PlayerWarpData clone()
    {
        PlayerWarpData clone = new PlayerWarpData();
        clone.location = new Vector3d(this.location);
        clone.rotation = this.rotation.clone();
        clone.world = this.world;
        clone.blockLocation = new Vector3i(this.blockLocation);
        return clone;
    }
}
