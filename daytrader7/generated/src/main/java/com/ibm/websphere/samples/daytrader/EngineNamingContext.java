package com.ibm.websphere.samples.daytrader;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.Supplier;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

/**
 * The engine naming context (engine-authored, listed in reserved_to_emitter.engine_files;
 * not patchable; RFC-042 section 11, tier Isolated). A read-only table of the names the
 * engine proved: lookup answers the bound object and NameNotFoundException for any other
 * name; every mutating operation throws OperationNotSupportedException. Sub-contexts,
 * listing and name composition are not built and answer NameNotFoundException.
 */
final class EngineNamingContext implements Context {

    private final Map<String, Supplier<Object>> entries;
    private final Hashtable<Object, Object> environment;

    EngineNamingContext(Map<String, Supplier<Object>> entries, Hashtable<?, ?> environment) {
        this.entries = entries;
        this.environment = environment == null ? new Hashtable<>() : new Hashtable<>(environment);
    }

    @Override
    public Object lookup(String name) throws NamingException {
        Supplier<Object> entry = entries.get(name);
        if (entry == null) {
            throw new NameNotFoundException(name);
        }
        try {
            return entry.get();
        } catch (RuntimeException e) {
            NamingException failure = new NamingException(name);
            failure.setRootCause(e);
            throw failure;
        }
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return lookup(name);
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return lookup(name);
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        throw readOnly();
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        throw readOnly();
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        throw readOnly();
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        throw readOnly();
    }

    @Override
    public void unbind(Name name) throws NamingException {
        throw readOnly();
    }

    @Override
    public void unbind(String name) throws NamingException {
        throw readOnly();
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        throw readOnly();
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        throw readOnly();
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        throw readOnly();
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        throw readOnly();
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        throw readOnly();
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        throw readOnly();
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        throw readOnly();
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        throw readOnly();
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        throw new NameNotFoundException(name.toString());
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        throw new NameNotFoundException(name);
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        throw new NameNotFoundException(name.toString());
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        throw new NameNotFoundException(name);
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        throw new NameNotFoundException(name.toString());
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        throw new NameNotFoundException(name);
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        throw new NameNotFoundException(name.toString());
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        throw new NameNotFoundException(name);
    }

    @Override
    public Hashtable<?, ?> getEnvironment() {
        return new Hashtable<>(environment);
    }

    @Override
    public void close() {
    }

    @Override
    public String getNameInNamespace() {
        return "";
    }

    private static OperationNotSupportedException readOnly() {
        return new OperationNotSupportedException("the engine naming context is read-only");
    }
}
