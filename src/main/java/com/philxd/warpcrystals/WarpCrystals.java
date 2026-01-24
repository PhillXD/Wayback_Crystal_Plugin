package com.philxd.warpcrystals;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;

import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;

import java.io.File;

public class WarpCrystals extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static Config<WarpedConfig> CONFIG;
    
    private static WarpCrystals instance;
    private ComponentType<EntityStore, PlayerWarpLocationComponent> warpComponent;
    private ComponentType<EntityStore, PlayerWarpBackLocationComponent> warpBackComponent;
    
    public WarpCrystals(JavaPluginInit init) {
        super(init);
        instance = this;
        CONFIG = this.withConfig("Warp_Crystals_Config",WarpedConfig.CODEC);
    }

    @Override
    protected void setup() {
        super.setup();
        
        CONFIG.save();
        
        this.warpComponent = this.getEntityStoreRegistry().registerComponent(PlayerWarpLocationComponent.class, "Player_Warp_Location_Component" ,PlayerWarpLocationComponent.CODEC);
        this.warpBackComponent = this.getEntityStoreRegistry().registerComponent(PlayerWarpBackLocationComponent.class, "Player_Warp_Back_Location_Component" ,PlayerWarpBackLocationComponent.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("Warp_Player_Interaction", WarpPlayerInteraction.class, WarpPlayerInteraction.CODEC);
        this.getCodecRegistry(Interaction.CODEC).register("Set_Player_Warp_Interaction", SetPlayerWarpInteraction.class, SetPlayerWarpInteraction.CODEC);
        
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, e -> {
            Store<EntityStore> entityStore = e.getPlayerRef().getStore();
            entityStore.ensureComponent(e.getPlayerRef(), warpComponent);
            entityStore.ensureComponent(e.getPlayerRef(), warpBackComponent);
        });
    }
    
    @Override
    protected void start() {
        File configDir = new File("mods/WarpCrystals");
        File configFile = new File(configDir, "config.json");
    }

    public static WarpCrystals get() {
        return instance;
    }

    public ComponentType<EntityStore, PlayerWarpLocationComponent> getPlayerWarpComponentType() {
        return warpComponent;
    }

    public ComponentType<EntityStore, PlayerWarpBackLocationComponent> getPlayerWarpBackComponentType() {
        return warpBackComponent;
    }
    
    public static Vector3f reduceRotationToYaw(Vector3f rotation) {
        return new Vector3f(0, rotation.getYaw(), 0);
    }
}
