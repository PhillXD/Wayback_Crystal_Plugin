package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class WarpedConfig {
    public static final BuilderCodec<WarpedConfig> CODEC = BuilderCodec.builder(
            WarpedConfig.class, WarpedConfig::new
    )
    .addField(new KeyedCodec<>("WarpMessage", Codec.STRING), (warpedConfig, value) -> warpedConfig.WarpMessage = value, (warpedConfig) -> warpedConfig.WarpMessage)
    .addField(new KeyedCodec<>("SetWarpMessage", Codec.STRING), (warpedConfig, value) -> warpedConfig.SetWarpMessage = value, (warpedConfig) -> warpedConfig.SetWarpMessage)
    .addField(new KeyedCodec<>("WarpBackMessage", Codec.STRING), (warpedConfig, value) -> warpedConfig.WarpBackMessage = value, (warpedConfig) -> warpedConfig.WarpBackMessage)
    .addField(new KeyedCodec<>("Comment", Codec.STRING), (warpedConfig, value) -> warpedConfig.Comment = value, (warpedConfig) -> warpedConfig.Comment)
    .addField(new KeyedCodec<>("TwoWayTravel", Codec.BOOLEAN), (warpedConfig, value) -> warpedConfig.TwoWayTravel = value, (warpedConfig) -> warpedConfig.TwoWayTravel)
    .build();
    
    private String WarpMessage = "";
    private String SetWarpMessage = "";
    private String WarpBackMessage = "";
    private String Comment = "To Show the playername in the warp message use {playername}. E.g. '{playername} Warped'"; 
    private boolean TwoWayTravel = false;
    
    
    public String getWarpMessage() {return WarpMessage;}
    public String getSetWarpMessage() {return SetWarpMessage;}
    public String getWarpBackMessage() {return WarpBackMessage;}
    public boolean doTwoWayTravel() {return TwoWayTravel;}
}
