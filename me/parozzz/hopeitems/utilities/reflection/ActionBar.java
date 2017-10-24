/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.parozzz.hopeitems.utilities.reflection;

import me.parozzz.hopeitems.utilities.Utils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.parozzz.hopeitems.utilities.reflection.API.ReflectionUtils;
import org.bukkit.entity.Player;

public final class ActionBar 
{
    private Object nmsChatMessageType;
    private Constructor<?> PacketPlayOutChat;
    protected ActionBar()
    {
        nmsChatMessageType = (byte)2;
        
        Class<?> ChatMessageType=byte.class;
        if(Utils.bukkitVersion("1.12"))
        {
            ChatMessageType=ReflectionUtils.getNMSClass("ChatMessageType");
            nmsChatMessageType=Arrays.stream(ChatMessageType.getEnumConstants()).filter(o -> o.toString().equals("GAME_INFO")).findFirst().orElseThrow(() -> new NullPointerException("ActionBar init problem"));
        }
        
        try 
        {
            PacketPlayOutChat=ReflectionUtils.getNMSClass("PacketPlayOutChat").getConstructor(ReflectionUtils.getNMSClass("IChatBaseComponent"), ChatMessageType);
        }
        catch(NoSuchMethodException | SecurityException ex) 
        {
            Logger.getLogger(ActionBar.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace(); 
        }
    }
    
    public void send(final Player p, final String message) 
    {
        try 
        {
            API.getPacketManager().sendPacket(p, PacketPlayOutChat.newInstance(ReflectionUtils.getStringSerialized(message), nmsChatMessageType));
        } 
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | SecurityException | InvocationTargetException ex)  
        { 
            ex.printStackTrace(); 
        }
    }
}