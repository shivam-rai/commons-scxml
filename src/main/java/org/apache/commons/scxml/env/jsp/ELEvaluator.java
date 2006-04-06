/*
 *
 *   Copyright 2005-2006 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.scxml.env.jsp;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.FunctionMapper;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.scxml.Builtin;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.w3c.dom.Node;

/**
 * Evaluator implementation enabling use of EL expressions in
 * SCXML documents.
 *
 */
public class ELEvaluator implements Evaluator {

    /** Implementation independent log category. */
    private Log log = LogFactory.getLog(Evaluator.class);
    /** Function Mapper for SCXML expressions. */
    private FunctionMapper functionMapper = new BuiltinFunctionMapper();
    /** Pattern for recognizing the SCXML In() special predicate. */
    private static Pattern inFct = Pattern.compile("In\\(");
    /** Pattern for recognizing the Commons SCXML Data() builtin function. */
    private static Pattern dataFct = Pattern.compile("Data\\(");

    /** The expression evaluator implementation for the JSP/EL environment. */
    private ExpressionEvaluator ee = null;

    /**
     * Constructor.
     */
    public ELEvaluator() {
        ee = new ExpressionEvaluatorImpl();
    }

    /**
     * Evaluate an expression.
     *
     * @param ctx variable context
     * @param expr expression
     * @return a result of the evaluation
     * @throws SCXMLExpressionException For a malformed expression
     * @see Evaluator#eval(Context, String)
     */
    public Object eval(final Context ctx, final String expr)
    throws SCXMLExpressionException {
        if (expr == null) {
            return null;
        }
        VariableResolver vr = null;
        if (ctx instanceof VariableResolver) {
            vr = (VariableResolver) ctx;
        } else {
            vr = new ContextWrapper(ctx);
        }
        try {
            String evalExpr = inFct.matcher(expr).
                replaceAll("In(_ALL_STATES, ");
            Object rslt = ee.evaluate(evalExpr, Object.class, vr,
                functionMapper);
            if (log.isTraceEnabled()) {
                log.trace(expr + " = " + String.valueOf(rslt));
            }
            return rslt;
        } catch (ELException e) {
            throw new SCXMLExpressionException(e);
        }
    }

    /**
     * @see Evaluator#evalCond(Context, String)
     */
    public Boolean evalCond(final Context ctx, final String expr)
    throws SCXMLExpressionException {
        if (expr == null) {
            return null;
        }
        VariableResolver vr = null;
        if (ctx instanceof VariableResolver) {
            vr = (VariableResolver) ctx;
        } else {
            vr = new ContextWrapper(ctx);
        }
        try {
            String evalExpr = inFct.matcher(expr).
                replaceAll("In(_ALL_STATES, ");
            Boolean rslt = (Boolean) ee.evaluate(evalExpr, Boolean.class,
                vr, functionMapper);
            if (log.isDebugEnabled()) {
                log.debug(expr + " = " + String.valueOf(rslt));
            }
            return rslt;
        } catch (ELException e) {
            throw new SCXMLExpressionException(e);
        }
    }

    /**
     * @see Evaluator#evalLocation(Context, String)
     */
    public Node evalLocation(final Context ctx, final String expr)
    throws SCXMLExpressionException {
        if (expr == null) {
            return null;
        }
        VariableResolver vr = null;
        if (ctx instanceof VariableResolver) {
            vr = (VariableResolver) ctx;
        } else {
            vr = new ContextWrapper(ctx);
        }
        try {
            String evalExpr = inFct.matcher(expr).
                replaceAll("In(_ALL_STATES, ");
            evalExpr = dataFct.matcher(evalExpr).
                replaceFirst("LData(");
            Node rslt = (Node) ee.evaluate(evalExpr, Node.class,
                vr, functionMapper);
            if (log.isDebugEnabled()) {
                log.debug(expr + " = " + String.valueOf(rslt));
            }
            return rslt;
        } catch (ELException e) {
            throw new SCXMLExpressionException(e);
        }
    }

    /**
     * Create a new child context.
     *
     * @param parent parent context
     * @return new child context
     * @see Evaluator#newContext(Context)
     */
    public Context newContext(final Context parent) {
        return new ELContext(parent);
    }

    /**
     * Set the log used by this <code>Evaluator</code> instance.
     *
     * @param log The new log.
     */
    protected void setLog(final Log log) {
        this.log = log;
    }

    /**
     * Get the log used by this <code>Evaluator</code> instance.
     *
     * @return Log The log being used.
     */
    protected Log getLog() {
        return log;
    }

    /**
     * A Context wrapper that implements VariableResolver.
     */
    static class ContextWrapper implements VariableResolver {
        /** Context to be wrapped. */
        private Context ctx = null;
        /** The log. */
        private Log log = LogFactory.getLog(ContextWrapper.class);
        /**
         * Constructor.
         * @param ctx The Context to be wrapped.
         */
        ContextWrapper(final Context ctx) {
            this.ctx = ctx;
        }
        /** @see VariableResolver#resolveVariable(String) */
        public Object resolveVariable(final String pName) throws ELException {
            Object rslt = ctx.get(pName);
            if (rslt == null) {
                log.info("Variable \"" + pName + "\" does not exist!");
            }
            return rslt;
        }
    }

    /**
     * A simple function mapper for SCXML defined functions.
     */
    static class BuiltinFunctionMapper implements FunctionMapper {
        /** The log. */
        private Log log = LogFactory.getLog(BuiltinFunctionMapper.class);
        /**
         * @see FunctionMapper#resolveFunction(String, String)
         */
        public Method resolveFunction(final String prefix,
                final String localName) {
            if (localName.equals("In")) {
                Class[] attrs = new Class[] {Set.class, String.class};
                try {
                    return Builtin.class.getMethod("isMember", attrs);
                } catch (SecurityException e) {
                    log.error("resolving isMember(Set, String)", e);
                } catch (NoSuchMethodException e) {
                    log.error("resolving isMember(Set, String)", e);
                }
            } else if (localName.equals("Data")) {
                // rvalue in expressions, coerce to String
                Class[] attrs = new Class[] {Object.class, String.class};
                try {
                    return Builtin.class.getMethod("data", attrs);
                } catch (SecurityException e) {
                    log.error("resolving data(Node, String)", e);
                } catch (NoSuchMethodException e) {
                    log.error("resolving data(Node, String)", e);
                }
            } else if (localName.equals("LData")) {
                // lvalue in expressions, retain as Node
                Class[] attrs = new Class[] {Object.class, String.class};
                try {
                    return Builtin.class.getMethod("dataNode", attrs);
                } catch (SecurityException e) {
                    log.error("resolving data(Node, String)", e);
                } catch (NoSuchMethodException e) {
                    log.error("resolving data(Node, String)", e);
                }
            }
            return null;
        }
    }

    /**
     * Get the FunctionMapper.
     *
     * @return functionMapper The FunctionMapper
     */
    protected FunctionMapper getFunctionMapper() {
        return functionMapper;
    }

}

