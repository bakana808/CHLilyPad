package com.octopod.chlp;

/**
 * @author Octopod
 *         Created on 5/22/14
 */

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.events.AbstractEvent;
import com.laytonsmith.core.events.BindableEvent;
import com.laytonsmith.core.events.Driver;
import com.laytonsmith.core.events.EventUtils;
import com.laytonsmith.core.exceptions.EventException;
import com.laytonsmith.core.exceptions.PrefilterNonMatchException;
import com.octopod.chlp.event.CHLPMessageEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Octopod
 *         Created on 5/21/14
 */
public class Events {

    public static void actionMessageRecieved(String sender, String channel, String message) {
        EventUtils.TriggerListener(Driver.EXTENSION, "lp_message_recieved", new CHLPMessageEvent(sender, channel, message));
    }

    @api
    public static class lp_message_recieved extends AbstractEvent {

        public String getName() {
            return "lp_message_recieved";
        }

        public BindableEvent convert(CArray arg0) {
            return null;
        }

        public String docs() {
            return "{} ";
        }

        public Driver driver() {
            return Driver.EXTENSION;
        }

        public Map<String, Construct> evaluate(BindableEvent event) throws EventException
        {
            if(event instanceof CHLPMessageEvent)
            {
                Map<String, Construct> map = new HashMap<>();

                CHLPMessageEvent custom = (CHLPMessageEvent)event;

                map.put("sender", new CString(custom.getSender(), Target.UNKNOWN));
                map.put("channel", new CString(custom.getChannel(), Target.UNKNOWN));
                map.put("message", new CString(custom.getMessage(), Target.UNKNOWN));

                return map;
            }

            throw new EventException("Couldn't convert event to MessageEvent");
        }

        public boolean matches(Map<String, Construct> prefilter, BindableEvent event) throws PrefilterNonMatchException
        {
            if(event instanceof CHLPMessageEvent) {

                CHLPMessageEvent custom = (CHLPMessageEvent)event;

                if(prefilter.containsKey("channel")) {
                    if(!prefilter.get("channel").val().equals(custom.getChannel())) {
                        return false;
                    }
                }

                if(prefilter.containsKey("sender")) {
                    if(!prefilter.get("sender").val().equals(custom.getSender())) {
                        return false;
                    }
                }

            }

            return true;
        }

        public boolean modifyEvent(String key, Construct value, BindableEvent event) {
            return false;
        }

        public Version since() {
            return CHVersion.V3_3_1;
        }
    }
}
