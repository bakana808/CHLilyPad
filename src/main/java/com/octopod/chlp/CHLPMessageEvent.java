package com.octopod.chlp; /**
 * @author Octopod
 *         Created on 5/21/14
 */

import com.laytonsmith.core.events.BindableEvent;

public class CHLPMessageEvent implements BindableEvent{

    String sender, channel, message;

    public CHLPMessageEvent(String sender, String channel, String message) {

        this.sender = sender;
        this.channel = channel;
        this.message = message;

    }

    public String getSender() {return sender;}
    public String getChannel() {return channel;}
    public String getMessage() {return message;}

    public Object _GetObject() {

        return null;

    }


}