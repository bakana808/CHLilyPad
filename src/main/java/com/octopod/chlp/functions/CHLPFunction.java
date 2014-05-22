package com.octopod.chlp.functions;

import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;

/**
 * @author Octopod
 *         Created on 5/21/14
 */
public abstract class CHLPFunction extends AbstractFunction {

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
