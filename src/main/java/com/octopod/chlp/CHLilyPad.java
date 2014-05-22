package com.octopod.chlp;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.commandhelper.CommandHelperPlugin;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import java.io.UnsupportedEncodingException;

/**
 * @author Octopod
 *         Created on 5/21/14
 */
@MSExtension("com.octopod.chlp.CHLilyPad")
public class CHLilyPad extends AbstractExtension {

    public static CHLilyPad self = null;
    public static Connect connection = null;

    @Override
    public Version getVersion() {
        return new SimpleVersion(0, 1, 0);
    }

    @Override
    public void onStartup() {
        self = this;
        connection = CommandHelperPlugin.self.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connection.registerEvents(this);
    }

    @Override
    public void onShutdown() {
        connection.unregisterEvents(this);
    }

    @EventListener
    public void messageListener(MessageEvent event) {
        try {
            Events.actionMessageRecieved(event.getSender(), event.getChannel(), event.getMessageAsString());
        } catch (UnsupportedEncodingException e) {}
    }

}
