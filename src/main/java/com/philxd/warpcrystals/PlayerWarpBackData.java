package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3i;

import javax.annotation.Nonnull;

public class PlayerWarpBackData {
    public static final BuilderCodec<PlayerWarpBackData> CODEC = BuilderCodec.builder(
                    PlayerWarpBackData.class,
                    PlayerWarpBackData::new
            )
            .addField(new KeyedCodec<>("WarpLocation", Vector3d.CODEC), (Data, value) -> Data.location = value, (Data) -> Data.location)
            .addField(new KeyedCodec<>("WarpRotation", Vector3f.CODEC), (Data, value) -> Data.rotation = value, (Data) -> Data.rotation)
            .addField(new KeyedCodec<>("WarpWorld", Codec.STRING), (Data, value) -> Data.world = value, (Data) -> Data.world)
            .build();

    
    public Vector3d location;
    public Vector3f rotation;
    public String world;

    @Nonnull
    public PlayerWarpBackData clone()
    {
        PlayerWarpBackData clone = new PlayerWarpBackData();
        clone.location = this.location.clone();
        clone.rotation = this.rotation.clone();
        clone.world = this.world;
        return clone;
    }
}
