package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.math.vector.Vector3fUtil;
import org.joml.Vector3d;
import org.joml.Vector3f;

import javax.annotation.Nonnull;

public class PlayerWarpBackData {
    public static final BuilderCodec<PlayerWarpBackData> CODEC = BuilderCodec.builder(
                    PlayerWarpBackData.class,
                    PlayerWarpBackData::new
            )
            .addField(new KeyedCodec<>("WarpLocation", Vector3dUtil.CODEC), (Data, value) -> Data.location = value, (Data) -> Data.location)
            .addField(new KeyedCodec<>("WarpRotation", Rotation3f.CODEC), (Data, value) -> Data.rotation = value, (Data) -> Data.rotation)
            .addField(new KeyedCodec<>("WarpWorld", Codec.STRING), (Data, value) -> Data.world = value, (Data) -> Data.world)
            .build();

    
    public Vector3d location;
    public Rotation3f rotation;
    public String world;

    @Nonnull
    public PlayerWarpBackData clone()
    {
        PlayerWarpBackData clone = new PlayerWarpBackData();
        clone.location = new Vector3d(this.location);
        clone.rotation = this.rotation.clone();
        clone.world = this.world;
        return clone;
    }
}
