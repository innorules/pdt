/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org.eclipse.php.internal.core.ast.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.php.internal.core.PHPVersion;
import org.eclipse.php.internal.core.ast.match.ASTMatcher;
import org.eclipse.php.internal.core.ast.visitor.Visitor;

/**
 * Represents the static statement
 * 
 * <pre>
 * e.g.
 * 
 * <pre>
 * static $a static $a, $b=5;
 */
public class StaticStatement extends Statement {

	private ASTNode.NodeList<Expression> expressions = new ASTNode.NodeList<Expression>(EXPRESSIONS_PROPERTY);

	/**
	 * The "expressions" structural property of this node type.
	 */
	public static final ChildListPropertyDescriptor EXPRESSIONS_PROPERTY = new ChildListPropertyDescriptor(
			StaticStatement.class, "expressions", Expression.class, CYCLE_RISK); //$NON-NLS-1$

	/**
	 * A list of property descriptors (element type:
	 * {@link StructuralPropertyDescriptor}), or null if uninitialized.
	 */
	private static final List<StructuralPropertyDescriptor> PROPERTY_DESCRIPTORS;

	static {
		List<StructuralPropertyDescriptor> properyList = new ArrayList<StructuralPropertyDescriptor>(2);
		properyList.add(EXPRESSIONS_PROPERTY);
		PROPERTY_DESCRIPTORS = Collections.unmodifiableList(properyList);
	}

	public StaticStatement(AST ast) {
		super(ast);
	}

	public StaticStatement(int start, int end, AST ast, List<Expression> expressions) {
		super(start, end, ast);

		if (expressions == null) {
			throw new IllegalArgumentException();
		}
		this.expressions.addAll(expressions);
	}

	/**
	 * @return the variables that participate in the static call
	 */
	public Variable[] getVariables() {

		List<Variable> vars = new LinkedList<Variable>();
		for (Expression node : this.expressions) {
			if (node instanceof Variable) {
				vars.add((Variable) node);
			} else {
				assert node instanceof Assignment;
				Assignment ass = (Assignment) node;
				vars.add((Variable) ass.getLeftHandSide());
			}
		}
		return vars.toArray(new Variable[vars.size()]);
	}

	public void accept0(Visitor visitor) {
		final boolean visit = visitor.visit(this);
		if (visit) {
			childrenAccept(visitor);
		}
		visitor.endVisit(this);
	}

	public void childrenAccept(Visitor visitor) {
		for (ASTNode node : this.expressions) {
			node.accept(visitor);
		}
	}

	public void traverseTopDown(Visitor visitor) {
		accept(visitor);
		for (ASTNode node : this.expressions) {
			node.traverseTopDown(visitor);
		}
	}

	public void traverseBottomUp(Visitor visitor) {
		for (ASTNode node : this.expressions) {
			node.traverseBottomUp(visitor);
		}
		accept(visitor);
	}

	public void toString(StringBuffer buffer, String tab) {
		buffer.append(tab).append("<StaticStatement"); //$NON-NLS-1$
		appendInterval(buffer);
		buffer.append(">\n"); //$NON-NLS-1$
		for (ASTNode node : this.expressions) {
			node.toString(buffer, TAB + tab);
			buffer.append("\n"); //$NON-NLS-1$
		}
		buffer.append(tab).append("</StaticStatement>"); //$NON-NLS-1$
	}

	public int getType() {
		return ASTNode.STATIC_STATEMENT;
	}

	/**
	 * @return expression list of the static statement
	 */
	public List<Expression> expressions() {
		return this.expressions;
	}

	final List internalGetChildListProperty(ChildListPropertyDescriptor property) {
		if (property == EXPRESSIONS_PROPERTY) {
			return expressions();
		}
		// allow default implementation to flag the error
		return super.internalGetChildListProperty(property);
	}

	/*
	 * Method declared on ASTNode.
	 */
	public boolean subtreeMatch(ASTMatcher matcher, Object other) {
		// dispatch to correct overloaded match method
		return matcher.match(this, other);
	}

	@Override
	ASTNode clone0(AST target) {
		final List<Expression> expressions = ASTNode.copySubtrees(target, this.expressions());
		return new StaticStatement(this.getStart(), this.getEnd(), target, expressions);
	}

	@Override
	List<StructuralPropertyDescriptor> internalStructuralPropertiesForType(PHPVersion apiLevel) {
		return PROPERTY_DESCRIPTORS;
	}
}
