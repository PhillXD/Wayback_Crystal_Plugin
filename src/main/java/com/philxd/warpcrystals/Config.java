package com.philxd.warpcrystals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.logger.HytaleLogger;

import javax.xml.stream.events.Comment;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    
    private static Config instance;
    
    private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    
    static Config getInstance(){
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

    public static void load(File configFile){
        if (!configFile.exists()) {
            instance = new Config();
            instance.save(configFile);
        }else {
            
            try (FileReader reader = new FileReader(configFile)) {
                instance = GSON.fromJson(reader, Config.class);
                if(instance == null) {
                    LOGGER.atWarning().log("[WarpCrystals] Something went wrong reading the config file");
                    instance = new Config();
                }
            } catch (Exception e) {
                LOGGER.atWarning().log("[WarpCrystals] Error during config load: " + e.getMessage());
            }
            
        }
    }

    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    
    public void save(File configFile) {
        try {
            File parentDir = configFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            LOGGER.atWarning().log("[WarpCrystals] Error during config save: " + e.getMessage());
        }
    }
    
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
