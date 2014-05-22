package com.octopod.chlp;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

import java.io.UnsupportedEncodingException;

/**
 * @author Octopod
 *         Created on 5/21/14
 */
public class Functions {

    public static abstract class CHLPFunction extends AbstractFunction {

        @Override
        public ExceptionType[] thrown() {
            return new ExceptionType[0];
        }

        @Override
        public boolean isRestricted() {
            return false;
        }

        @Override
        public Boolean runAsync() {
            return false;
        }

        @Override
        public Version since() {
            return CHVersion.V3_3_1;
        }
    }

    private static Connect getConnection(Target t)
    {
        Connect connection = CHLilyPad.connection;

        if(connection == null)
            throw new ConfigRuntimeException("LilyPad is not loaded on this server", ExceptionType.InvalidPluginException, t);
        if(!connection.isConnected())
            throw new ConfigRuntimeException("LilyPad is not connected", ExceptionType.PluginInternalException, t);

        return connection;
    }

    private static Result sendRequest(Connect connection, Request request, Target t) {
        Result result;
        try {
            result = connection.request(request).awaitUninterruptibly(500);
        } catch (RequestException e) {
            throw new ConfigRuntimeException("Unable to send request to LilyPad", ExceptionType.PluginInternalException, t);
        }

        if(result.getStatusCode() != StatusCode.SUCCESS) {
            throw new ConfigRuntimeException("An error has occured while sending this request", ExceptionType.PluginInternalException, t);
        }
        return result;
    }

    @api
    public static class lp_redirect extends CHLPFunction {

        @Override
        public String getName() {
            return "lp_redirect";
        }

        @Override
        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException
        {
            Connect connection = getConnection(t);

            String username = args[0].val();
            MCPlayer player = Static.GetPlayer(args[1], t);

            RedirectRequest request = new RedirectRequest(username, player.getName());

            sendRequest(connection, request, t);

            return CVoid.GenerateCVoid(t);
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        @Override
        public String docs() {
            return "void {username, player} Redirects a player to a server by username.";
        }
    }

    @api
    public static class lp_message extends CHLPFunction {

        @Override
        public String getName() {
            return "lp_message";
        }

        @Override
        public Construct exec(Target t, Environment env, Construct... args) throws ConfigRuntimeException
        {
            Connect connection = getConnection(t);

            String username = args[0].val();
            String channel = args[1].val();
            String message = args[2].val();

            MessageRequest request;

            try {
                request = new MessageRequest(username, channel, message);
            } catch (UnsupportedEncodingException e) {
                throw new ConfigRuntimeException("Unable to encode message", ExceptionType.CastException, t);
            }

            sendRequest(connection, request, t);

            return CVoid.GenerateCVoid(t);
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{3};
        }

        @Override
        public String docs() {
            return "void {username, channel, message} Sends a message through LilyPad to another server by username.";
        }
    }
}
