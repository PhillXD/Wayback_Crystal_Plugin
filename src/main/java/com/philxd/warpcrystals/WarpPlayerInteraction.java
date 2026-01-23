package com.philxd.warpcrystals;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.FormattedMessage;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.protocol.packets.interface_.ChatMessage;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class WarpPlayerInteraction extends SimpleInstantInteraction {
    public static final BuilderCodec<WarpPlayerInteraction> CODEC = BuilderCodec.builder(
            WarpPlayerInteraction.class, WarpPlayerInteraction::new, SimpleInstantInteraction.CODEC
    )
    .addField(new KeyedCodec<>("WarpKey", Codec.STRING), (Data, value) -> Data.key = value, (data) -> data.key)
    .build();

    public String key = "default";
    
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
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
            interactionContext.getState().state = InteractionState.Failed;
            return;
        }
        
        if(!warpLocationComponent.hasWarp(key)){
            interactionContext.getState().state = InteractionState.Failed;
            return;
        }

        if(Config.getInstance().doTwoWayTravel()) {
            PlayerWarpBackLocationComponent warpBackLocationComponent = playerStore.getComponent(playerRef, WarpCrystals.get().getPlayerWarpBackComponentType());
            if(warpBackLocationComponent == null){
                PlayerWarpBackLocationComponent newwarpBackLocationComponent = new PlayerWarpBackLocationComponent(playerRef, key);
                commandBuffer.addComponent(playerRef, WarpCrystals.get().getPlayerWarpBackComponentType(),newwarpBackLocationComponent);
            }else {
                warpBackLocationComponent.ChangeWarp(playerRef, key);
            }
        }
        
        Teleport teleport = new Teleport(
                warpLocationComponent.getWorld(key),
                warpLocationComponent.getLocation(key),
                warpLocationComponent.getRotation(key)
        );

        String message = Config.getInstance().getWarpMessage();
        if(!message.equals("")) {
            Nameplate nameplate = playerStore.getComponent(playerRef, Nameplate.getComponentType());
            if(nameplate != null) {
                message = message.replace("{playername}", nameplate.getText());
                Message sendMessage = Message.raw(message);
                Universe.get().getPlayers().forEach(player -> player.sendMessage(sendMessage));
            }
        }
        
        commandBuffer.addComponent(playerRef,Teleport.getComponentType(),teleport);
    }
}
