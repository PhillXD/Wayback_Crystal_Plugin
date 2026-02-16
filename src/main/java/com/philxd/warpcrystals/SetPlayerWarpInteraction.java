package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class SetPlayerWarpInteraction extends SimpleInstantInteraction {
    public static final BuilderCodec<SetPlayerWarpInteraction> CODEC = BuilderCodec.builder(
            SetPlayerWarpInteraction.class, SetPlayerWarpInteraction::new, SimpleInstantInteraction.CODEC
    )
    .addField(new KeyedCodec<>("WarpKey", Codec.STRING), (Data, value) -> Data.key = value, (data) -> data.key)
    .build();

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public String key = "default";
    
    @Override
    protected void firstRun(@NonNullDecl InteractionType interactionType, @NonNullDecl InteractionContext interactionContext, @NonNullDecl CooldownHandler cooldownHandler) {
        CommandBuffer<EntityStore> commandBuffer = interactionContext.getCommandBuffer();
        if (commandBuffer == null) {
            interactionContext.getState().state = InteractionState.Failed;
            LOGGER.atInfo().log("CommandBuffer is null");
            return;
        }
        
        Store<EntityStore> playerStore = commandBuffer.getExternalData().getStore();
        Ref<EntityStore> playerRef = interactionContext.getEntity();

        PlayerWarpLocationComponent warpLocationComponent = playerStore.getComponent(playerRef, WarpCrystals.get().getPlayerWarpComponentType());
        if(warpLocationComponent == null){
            PlayerWarpLocationComponent newwarpLocationComponent = new PlayerWarpLocationComponent(playerRef, interactionContext.getTargetBlock(),key);
            commandBuffer.addComponent(playerRef, WarpCrystals.get().getPlayerWarpComponentType(),newwarpLocationComponent);
            return;
        }
        
        if(WarpCrystals.CONFIG.get().doTwoWayTravel() &&  interactionContext.getTargetBlock() != null && interactionContext.getTargetBlock().equals(warpLocationComponent.getBlockLocation(key))) {

            PlayerWarpBackLocationComponent warpBackLocationComponent = playerStore.getComponent(playerRef, WarpCrystals.get().getPlayerWarpBackComponentType());
            if(warpBackLocationComponent != null && warpBackLocationComponent.hasWarp(key)){

                Vector3d location = warpBackLocationComponent.getLocation(key);
                World world = warpBackLocationComponent.getWorld(key);
                
                if(location == null || world == null) {
                    LOGGER.atWarning().log("Warp back component data is incomplete | key: " + key);
                    interactionContext.getState().state = InteractionState.Failed;
                    return;
                }
                
                //teleport player to warp back location
                Teleport teleport = new Teleport(
                        world,
                        location,
                        WarpCrystals.reduceRotationToYaw(warpBackLocationComponent.getRotation(key))
                );
                commandBuffer.addComponent(playerRef,Teleport.getComponentType(),teleport);

                String message = WarpCrystals.CONFIG.get().getWarpBackMessage();
                if(!message.equals("")) {
                    Nameplate nameplate = playerStore.getComponent(playerRef, Nameplate.getComponentType());
                    if(nameplate != null) {
                        message = message.replace("{playername}", nameplate.getText());
                        Message sendMessage = Message.raw(message);
                        Universe.get().getPlayers().forEach(player -> player.sendMessage(sendMessage));
                    }
                }
                
                
                return;
            }else {
                interactionContext.getState().state = InteractionState.Failed;
                return;
            }
            
        }else {
            warpLocationComponent.ChangeWarp(playerRef, interactionContext.getTargetBlock(), key);
        }
        

        String message = WarpCrystals.CONFIG.get().getSetWarpMessage();
        if(!message.equals("")) {
            Nameplate nameplate = playerStore.getComponent(playerRef, Nameplate.getComponentType());
            if(nameplate != null) {
                message = message.replace("{playername}", nameplate.getText());
                Message sendMessage = Message.raw(message);
                Universe.get().getPlayers().forEach(player -> player.sendMessage(sendMessage));
            }
        }
    }    
}
